package com.hottabych04.app.exception.user;

import com.hottabych04.app.exception.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException {
    public UserNotFoundException(String message, String entityDetail) {
        super(message, entityDetail);
    }

    public UserNotFoundException(String message, Throwable cause, String entityDetail) {
        super(message, cause, entityDetail);
    }
}
