package com.hottabych04.app.exception;

import org.springframework.http.HttpStatus;

public class EntityExistsException extends TaskManagerRuntimeException {

    public EntityExistsException(String message, String entityDetails) {
        super(message, HttpStatus.BAD_REQUEST, new Object[]{entityDetails});
    }
}
