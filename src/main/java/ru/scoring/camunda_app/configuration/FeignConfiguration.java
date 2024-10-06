package ru.scoring.camunda_app.configuration;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = {"ru.scoring.api_specifications"})
public class FeignConfiguration {
}
