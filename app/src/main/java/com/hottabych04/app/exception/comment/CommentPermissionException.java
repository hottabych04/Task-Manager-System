package com.hottabych04.app.exception.comment;

import com.hottabych04.app.exception.PermissionsDeniedException;

public class CommentPermissionException extends PermissionsDeniedException {
    public CommentPermissionException() {
        super("comment.permission.denied");
    }
}
