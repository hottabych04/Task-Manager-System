package com.hottabych04.app.exception;

public class EntityExistsException extends TaskManagerRuntimeException {

    private final String entityDetails;

    public EntityExistsException(String message, String entityDetails) {
        super(message);
        this.entityDetails = entityDetails;
    }

    public EntityExistsException(String message, Throwable cause, String entityDetails) {
        super(message, cause);
        this.entityDetails = entityDetails;
    }
}
