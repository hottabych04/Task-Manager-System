package com.hottabych04.app.exception;

import org.springframework.http.HttpStatus;

public class EntityNotFoundException extends TaskManagerRuntimeException{

    public EntityNotFoundException(String message, String entityDetails) {
        super(message, HttpStatus.NOT_FOUND, new Object[]{entityDetails});
    }
}
