package com.hottabych04.app.exception.role;

import com.hottabych04.app.exception.EntityNotFoundException;

public class RoleNotFoundException extends EntityNotFoundException {
    public RoleNotFoundException(String entityDetail) {
        super("role.not.found", entityDetail);
    }
}
