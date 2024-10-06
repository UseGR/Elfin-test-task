package ru.scoring.camunda_app.engine.variables;

import lombok.Builder;

@Builder
public record ProcessVariable<T>(String key, Class<T> type) implements MetaData<T> {

}
