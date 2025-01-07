package com.hottabych04.app.exception.task;

import com.hottabych04.app.exception.EntityNotFoundException;

public class TaskNotFoundException extends EntityNotFoundException {
    public TaskNotFoundException(String message, String entityDetail) {
        super(message, entityDetail);
    }

    public TaskNotFoundException(String message, Throwable cause, String entityDetail) {
        super(message, cause, entityDetail);
    }
}
