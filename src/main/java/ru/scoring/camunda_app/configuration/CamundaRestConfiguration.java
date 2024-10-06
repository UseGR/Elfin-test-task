package ru.scoring.camunda_app.configuration;

import jakarta.servlet.Filter;
import org.camunda.bpm.engine.rest.security.auth.ProcessEngineAuthenticationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CamundaRestConfiguration {

    @Bean
    public FilterRegistrationBean<Filter> engineRestAuthorizationConfiguration(ProcessEngineAuthenticationFilter processEngineAuthenticationFilter) {
        FilterRegistrationBean<Filter> filter = new FilterRegistrationBean<>();

        filter.setName("camunda-auth");
        filter.setFilter(processEngineAuthenticationFilter);
        filter.addInitParameter("authentication-provider",
                "org.camunda.bpm.engine.rest.security.auth.impl.HttpBasicAuthenticationProvider");
        filter.addUrlPatterns("/engine-rest/*");
        filter.setOrder(101);

        return filter;
    }

    @Bean
    public ProcessEngineAuthenticationFilter processEngineAuthenticationFilter() {
        return new ProcessEngineAuthenticationFilter();
    }
}
