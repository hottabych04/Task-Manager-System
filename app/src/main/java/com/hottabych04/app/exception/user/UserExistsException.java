package com.hottabych04.app.exception.user;

import com.hottabych04.app.exception.EntityExistsException;

public class UserExistsException extends EntityExistsException {
    public UserExistsException(String message, String entityDetails) {
        super(message, entityDetails);
    }

    public UserExistsException(String message, Throwable cause, String entityDetails) {
        super(message, cause, entityDetails);
    }
}
