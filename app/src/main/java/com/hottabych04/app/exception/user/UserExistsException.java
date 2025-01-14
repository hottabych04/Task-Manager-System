package com.hottabych04.app.exception.user;

import com.hottabych04.app.exception.EntityExistsException;

public class UserExistsException extends EntityExistsException {
    public UserExistsException(String entityDetails) {
        super("user.exists", entityDetails);
    }
}
