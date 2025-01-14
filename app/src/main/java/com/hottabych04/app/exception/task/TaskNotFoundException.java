package com.hottabych04.app.exception.task;

import com.hottabych04.app.exception.EntityNotFoundException;

public class TaskNotFoundException extends EntityNotFoundException {
    public TaskNotFoundException(String entityDetail) {
        super("task.not.found", entityDetail);
    }
}
