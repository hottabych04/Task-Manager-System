package com.hottabych04.app.exception.user;

import com.hottabych04.app.exception.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException {
    public UserNotFoundException(String entityDetail) {
        super("user.not.found", entityDetail);
    }
}
