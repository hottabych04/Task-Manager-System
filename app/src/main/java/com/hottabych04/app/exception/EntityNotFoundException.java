package com.hottabych04.app.exception;

public class EntityNotFoundException extends TaskManagerRuntimeException{

    private final String entityDetail;

    public EntityNotFoundException(String message, String entityDetail) {
        super(message);
        this.entityDetail = entityDetail;
    }

    public EntityNotFoundException(String message, Throwable cause, String entityDetail) {
        super(message, cause);
        this.entityDetail = entityDetail;
    }
}
