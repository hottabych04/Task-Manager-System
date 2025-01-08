package com.hottabych04.app.exception.task;

import com.hottabych04.app.exception.EntityExistsException;

public class PerformerExistsException extends EntityExistsException {
    public PerformerExistsException(String message, String entityDetails) {
        super(message, entityDetails);
    }

    public PerformerExistsException(String message, Throwable cause, String entityDetails) {
        super(message, cause, entityDetails);
    }
}
