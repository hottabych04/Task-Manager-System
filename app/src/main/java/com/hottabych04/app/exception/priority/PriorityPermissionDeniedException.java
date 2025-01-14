package com.hottabych04.app.exception.priority;

import com.hottabych04.app.exception.PermissionsDeniedException;

public class PriorityPermissionDeniedException extends PermissionsDeniedException {
    public PriorityPermissionDeniedException() {
        super("task.priority.permission.denied");
    }
}
