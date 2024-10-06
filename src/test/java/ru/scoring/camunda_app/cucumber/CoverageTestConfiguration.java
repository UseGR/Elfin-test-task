package ru.scoring.camunda_app.cucumber;

import jakarta.ws.rs.BeanParam;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.extension.process_test_coverage.engine.ProcessCoverageConfigurator;
import org.camunda.bpm.spring.boot.starter.configuration.impl.AbstractCamundaConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class CoverageTestConfiguration {

    @Bean
    public AbstractCamundaConfiguration camundaConfiguration() {
        return new AbstractCamundaConfiguration() {
            @Override
            public void preInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
                super.preInit(processEngineConfiguration);
                ProcessCoverageConfigurator.initializeProcessCoverageExtensions(processEngineConfiguration);
            }
        };
    }
}
