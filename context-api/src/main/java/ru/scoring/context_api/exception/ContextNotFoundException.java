package ru.scoring.context_api.exception;

public class ContextNotFoundException extends RuntimeException {
    public ContextNotFoundException(String message) {
        super(message);
    }
}
