package ru.scoring.camunda_app.engine.variables;

public interface MetaData<T> {
    Class<T> type();
    String key();
}
