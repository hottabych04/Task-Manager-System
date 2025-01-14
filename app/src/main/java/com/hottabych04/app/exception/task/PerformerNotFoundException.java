package com.hottabych04.app.exception.task;

import com.hottabych04.app.exception.EntityNotFoundException;

public class PerformerNotFoundException extends EntityNotFoundException {
    public PerformerNotFoundException(String entityDetail) {
        super("task.performer.not.found", entityDetail);
    }
}
