package com.hottabych04.app.exception;

import org.springframework.http.HttpStatus;

public class PermissionsDeniedException extends TaskManagerRuntimeException {
    public PermissionsDeniedException(String message) {
        super(message, HttpStatus.FORBIDDEN, null);
    }
}
