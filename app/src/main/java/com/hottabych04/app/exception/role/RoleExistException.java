package com.hottabych04.app.exception.role;

import com.hottabych04.app.exception.EntityExistsException;

public class RoleExistException extends EntityExistsException {
    public RoleExistException(String entityDetails) {
        super("role.exists", entityDetails);
    }
}
