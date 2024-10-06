package ru.scoring.camunda_app.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import org.camunda.spin.impl.json.jackson.format.JacksonJsonDataFormat;
import org.camunda.spin.spi.DataFormatConfigurator;

public class JacksonJsonDataFormatConfigurator implements DataFormatConfigurator<JacksonJsonDataFormat> {
    @Override
    public Class<JacksonJsonDataFormat> getDataFormatClass() {
        return JacksonJsonDataFormat.class;
    }

    @Override
    public void configure(JacksonJsonDataFormat jacksonJsonDataFormat) {
        jacksonJsonDataFormat.getObjectMapper()
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }
}
