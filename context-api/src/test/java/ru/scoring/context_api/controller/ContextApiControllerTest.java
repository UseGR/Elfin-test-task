package ru.scoring.context_api.controller;

import io.restassured.RestAssured;
import net.javacrumbs.jsonunit.core.Option;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.scoring.context_api.ApplicationTest;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ContextApiControllerTest extends ApplicationTest {
    private static final String CONTEXT_SAVE_REQUEST = loadResourceData("models/context_save/request.json");
    private static final String CONTEXT_SAVE_PATH = "/api/v1.0/context/save";
    private static final String CONTEXT_GET_PATH = "/api/v1.0/context/get";
    private static final String PROCESS_ID_PARAM_KEY = "processId";
    private static final String PROCESS_ID_VALUE = "458548fb-c9ad-473f-8962-e71f0eeba775";
    private static final String NOT_FOUND_PROCESS_ID_VALUE = "b6bef692-835d-11ef-ae7f-0242ac1b0005";

    @Test
    @DisplayName("Сохранение контекста с последующим получением - статус 200")
    void saveAndGetContextSuccess() {
        int responseStatusCode = RestAssured.given()
                .body(CONTEXT_SAVE_REQUEST)
                .post(CONTEXT_SAVE_PATH)
                .then()
                .extract()
                .statusCode();

        assertEquals(SC_OK, responseStatusCode);

        String contextGetResponse = RestAssured.given()
                .queryParam(PROCESS_ID_PARAM_KEY, PROCESS_ID_VALUE)
                .get(CONTEXT_GET_PATH)
                .then()
                .extract()
                .body()
                .asPrettyString();

        assertThatJson(contextGetResponse).when(Option.IGNORING_ARRAY_ORDER).isEqualTo(CONTEXT_SAVE_REQUEST);
    }

    @Test
    @DisplayName("Поиск несуществующего процесса - статус 404")
    void getContextNotFound() {
        int responseStatusCode = RestAssured.given()
                .queryParam(PROCESS_ID_PARAM_KEY, NOT_FOUND_PROCESS_ID_VALUE)
                .get(CONTEXT_GET_PATH)
                .then()
                .extract()
                .statusCode();

        assertEquals(SC_NOT_FOUND, responseStatusCode);
    }
}
