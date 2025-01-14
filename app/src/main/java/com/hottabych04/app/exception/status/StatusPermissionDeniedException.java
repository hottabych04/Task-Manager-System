package com.hottabych04.app.exception.status;

import com.hottabych04.app.exception.PermissionsDeniedException;

public class StatusPermissionDeniedException extends PermissionsDeniedException {
    public StatusPermissionDeniedException() {
        super("task.status.permission.denied");
    }
}
