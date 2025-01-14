package com.hottabych04.app.exception.status;

import com.hottabych04.app.exception.EntityNotFoundException;

public class StatusNotFoundException extends EntityNotFoundException {
    public StatusNotFoundException(String entityDetail) {
        super("status.not.found", entityDetail);
    }
}
