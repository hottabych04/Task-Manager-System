package com.hottabych04.app.exception.role;

import com.hottabych04.app.exception.EntityNotFoundException;

public class RoleNotFoundException extends EntityNotFoundException {
    public RoleNotFoundException(String message, String entityDetail) {
        super(message, entityDetail);
    }

    public RoleNotFoundException(String message, Throwable cause, String entityDetail) {
        super(message, cause, entityDetail);
    }
}
