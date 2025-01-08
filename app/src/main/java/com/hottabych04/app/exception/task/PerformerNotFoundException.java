package com.hottabych04.app.exception.task;

import com.hottabych04.app.exception.EntityNotFoundException;

public class PerformerNotFoundException extends EntityNotFoundException {
    public PerformerNotFoundException(String message, String entityDetail) {
        super(message, entityDetail);
    }

    public PerformerNotFoundException(String message, Throwable cause, String entityDetail) {
        super(message, cause, entityDetail);
    }
}
