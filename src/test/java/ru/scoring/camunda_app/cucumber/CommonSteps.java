package ru.scoring.camunda_app.cucumber;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.cucumber.java.ru.*;
import io.restassured.RestAssured;
import net.javacrumbs.jsonunit.core.Option;
import org.awaitility.core.ConditionFactory;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricVariableInstance;
import org.springframework.beans.factory.annotation.Autowired;
import ru.scoring.api_specifications.process_controller._1_0_0.server.model.ScoringStartResponse;
import ru.scoring.camunda_app.ApplicationTest;
import ru.scoring.camunda_app.process.main.variable.Context;

import java.time.Duration;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static net.javacrumbs.jsonunit.core.Option.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static ru.scoring.camunda_app.ApplicationTest.*;
import static ru.scoring.camunda_app.ApplicationTest.WIREMOCK_HOST;
import static ru.scoring.camunda_app.ApplicationTest.loadResourceData;
import static ru.scoring.camunda_app.process.main.variable.MainProcessVariables.CONTEXT;
import static ru.scoring.camunda_app.process.main.variable.MainProcessVariables.IS_SCORING_FAILED;

public class CommonSteps {
    public static final ConditionFactory WAIT = await()
            .atMost(Duration.ofSeconds(10))
            .pollInterval(Duration.ofMillis(500))
            .pollDelay(Duration.ofMillis(100));

    private String scenarioFilesPath = "";
    private String processId;
    @Autowired
    HistoryService historyService;
    @Autowired
    ObjectMapper objectMapper;

    @Дано("все файлы этого сценария расположены в директории {string}")
    public void всеФайлыЭтогоЭтапаСценарияРасполоденыВДиректории(String filesPath) {
        scenarioFilesPath = filesPath;
    }

    @Если("сервис обратится на {string} с телом {string} он получит код {int}")
    public void сервисОбратитсяНаContextApiApiVContextSaveСТеломContextApiContext_save_requestJsonОнПолучитКод(String url, String requestBodyJsonResource, int code) {
        configureFor(WIREMOCK_HOST, wireMockServer.port());
        stubFor(any(urlEqualTo(url))
                .withRequestBody(equalToJson(loadResourceData(scenarioFilesPath + requestBodyJsonResource), true, true))
                .willReturn(aResponse().withStatus(code))
        );

    }

    @Когда("сервис получает запрос на запуск процесса скоринг-оценки с методом {string} по адресу {string} и json телом {string}, а в ответ отдает код {int}")
    public void сервисПолучаетЗапросНаЗапускПроцессаСкорингОценкиСМетодомPOSTПоАдресуApiVScoringStartИJsonТеломStart_scoring_requestJsonАВОтветОтдаетКод(String httpMethod, String url, String pathToRequestBodyJson, int responseCode) throws JsonProcessingException {
        ScoringStartResponse response = RestAssured.given()
                .body(loadResourceData(scenarioFilesPath + pathToRequestBodyJson))
                .request(httpMethod, url)
                .then()
                .statusCode(responseCode)
                .extract()
                .body()
                .as(ScoringStartResponse.class);

        this.processId = response.getId().toString();
    }

    @И("был сделан post запрос на {string} с телом {string} {int} раз")
    public void былСделанPostЗапросНаContextApiApiVContextSaveСТеломContextApiContext_save_requestJsonРаз(String url, String bodyPath, Integer count) {
        WAIT.untilAsserted(() -> verify(count, postRequestedFor(urlEqualTo(url))
                .withRequestBody(equalToJson(loadResourceData(scenarioFilesPath + bodyPath), true, true))));
    }

    @Тогда("процесс должен быть завершен")
    public void процессДолженБытьЗавершен() {
        WAIT.untilAsserted(() -> {
            HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(processId)
                    .singleResult();

            assertThat(processInstance.getState()).isEqualTo(HistoricProcessInstance.STATE_COMPLETED);
        });
    }

    @И("переменная {string} должна быть равна {string}")
    public void переменнаяIs_scoring_failedДолжнаБытьРавнаFalse(String variable, String value) {
        WAIT.untilAsserted(() -> {
            HistoricVariableInstance result = historyService.createHistoricVariableInstanceQuery()
                    .processInstanceId(processId)
                    .variableName(IS_SCORING_FAILED.key())
                    .singleResult();

            assertThat(result).isNotNull();
            assertThat(result.getValue()).isEqualTo(Boolean.valueOf(value));
        });
    }

    @И("состояние контекста должно быть идентично json представлению {string}")
    public void состояниеКонтекстаДолжноБытьИдентичноJsonПредставлениюContextJson(String pathToJson) {
        WAIT.untilAsserted(() -> {
            String json = historyService.createHistoricVariableInstanceQuery()
                    .processInstanceId(processId)
                    .variableName(CONTEXT.key())
                    .singleResult()
                    .getValue()
                    .toString();

            Context context = objectMapper.readValue(json, Context.class);

            String expectedContext = loadResourceData(scenarioFilesPath + pathToJson);

            assertThatJson(context).when(IGNORING_ARRAY_ORDER).isEqualTo(expectedContext);
        });
    }
}
