package com.hottabych04.app.exception.priority;

import com.hottabych04.app.exception.EntityNotFoundException;

public class PriorityNotFoundException extends EntityNotFoundException {
    public PriorityNotFoundException(String entityDetail) {
        super("priority.not.found", entityDetail);
    }
}
