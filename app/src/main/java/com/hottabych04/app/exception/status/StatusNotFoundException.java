package com.hottabych04.app.exception.status;

import com.hottabych04.app.exception.EntityNotFoundException;

public class StatusNotFoundException extends EntityNotFoundException {
    public StatusNotFoundException(String message, String entityDetail) {
        super(message, entityDetail);
    }

    public StatusNotFoundException(String message, Throwable cause, String entityDetail) {
        super(message, cause, entityDetail);
    }
}
