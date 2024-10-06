package ru.scoring.context_api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;
import ru.scoring.context_api.config.container.ContextElasticSearchContainer;
import ru.scoring.context_api.entity.Context;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationTest {
    @LocalServerPort
    protected int port;

    @Autowired
    public ObjectMapper objectMapper;

    private static final ElasticsearchContainer container = new ContextElasticSearchContainer();

    @Autowired
    private ElasticsearchTemplate template;

    @BeforeEach
    void initRequestSpecification() {
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .setPort(port)
                .build();
    }

    @SneakyThrows
    public static String loadResourceData(String resourceFile) {
        return IOUtils.toString(new ClassPathResource(resourceFile).getInputStream(), StandardCharsets.UTF_8.displayName());
    }

    @BeforeAll
    static void setUp() {
        container.start();
    }

    @AfterAll
    static void destroy() {
        container.stop();
    }

    @BeforeEach
    void testIsContainerRunning() {
        assertTrue(container.isRunning());
        recreateIndex();
    }

    private void recreateIndex() {
        if (template.indexOps(Context.class).exists()) {
            template.indexOps(Context.class).delete();
            template.indexOps(Context.class).create();
        }
    }
}
