package com.hottabych04.app.exception.priority;

import com.hottabych04.app.exception.EntityNotFoundException;

public class PriorityNotFoundException extends EntityNotFoundException {
    public PriorityNotFoundException(String message, String entityDetail) {
        super(message, entityDetail);
    }

    public PriorityNotFoundException(String message, Throwable cause, String entityDetail) {
        super(message, cause, entityDetail);
    }
}
