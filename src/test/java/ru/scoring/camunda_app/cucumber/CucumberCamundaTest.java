package ru.scoring.camunda_app.cucumber;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.impl.bpmn.parser.BpmnParseListener;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.extension.process_test_coverage.engine.ElementCoverageParseListener;
import org.camunda.bpm.extension.process_test_coverage.engine.ExecutionContextModelProvider;
import org.camunda.bpm.extension.process_test_coverage.model.DefaultCollector;
import org.camunda.bpm.extension.process_test_coverage.model.Run;
import org.camunda.bpm.extension.process_test_coverage.util.CoverageReportUtil;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Import;
import ru.scoring.camunda_app.ApplicationTest;

import java.util.List;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;

@Suite
@SelectClasspathResource("features")
@IncludeEngines("cucumber")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "ru.scoring.camunda_app.cucumber")
@CucumberContextConfiguration
@Import(CoverageTestConfiguration.class)
@EnableAutoConfiguration
@Slf4j
public class CucumberCamundaTest extends ApplicationTest {

    @Autowired
    public ProcessEngineConfiguration processEngineConfiguration;

    @Before
    public void before() {
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setPort(port)
                .build();
        wireMockServer.resetAll();
    }

    DefaultCollector coverageCollector = new DefaultCollector(new ExecutionContextModelProvider());

    private boolean suiteInitialized;

    @Before
    public void initializeListenersAndSuite() {
        ProcessEngineConfigurationImpl impl = (ProcessEngineConfigurationImpl) processEngineConfiguration;
        List<BpmnParseListener> list = impl.getCustomPostBPMNParseListeners();

        list.forEach(l -> {
            if (l instanceof ElementCoverageParseListener) {
                ((ElementCoverageParseListener) l).setCoverageState(coverageCollector);
            }
        });
    }

    @Before
    public void initializeSuit(Scenario scenario) {
        if (!suiteInitialized) {
            String suiteId = scenario.getId();
            coverageCollector.createSuite(new org.camunda.bpm.extension.process_test_coverage.model.Suite(suiteId, scenario.getName()));
            coverageCollector.activateSuite(suiteId);
            suiteInitialized = true;
        }

        String runId = scenario.getName();
        coverageCollector.createRun(new Run(runId, scenario.getName()), coverageCollector.getActiveSuite().getId());
        coverageCollector.activateRun(runId);
    }

    @After
    public void afterTestClass() {
        org.camunda.bpm.extension.process_test_coverage.model.Suite suite = coverageCollector.getActiveSuite();
        double suiteCoveragePercentage = suite.calculateCoverage(coverageCollector.getModels());
        log.info("Покрытие класса {} составляет: {}", suite.getName(), String.format("%.2f%%", suiteCoveragePercentage * 100));

        CoverageReportUtil.createReport(coverageCollector);
        CoverageReportUtil.createJsonReport(coverageCollector);
    }

    @After
    public void afterTestMethod(Scenario scenario) {
        org.camunda.bpm.extension.process_test_coverage.model.Suite suite = coverageCollector.getActiveSuite();
        Run run = suite.getRun(scenario.getName());
        if (run != null) {
            double percentage = run.calculateCoverage(coverageCollector.getModels());
            log.info("Покрытие метода {} составляет: {}", run.getName(), String.format("%.2f%%", percentage * 100));
        }
    }
}
