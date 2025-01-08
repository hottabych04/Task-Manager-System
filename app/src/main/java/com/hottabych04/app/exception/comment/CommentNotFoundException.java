package com.hottabych04.app.exception.comment;

import com.hottabych04.app.exception.EntityNotFoundException;

public class CommentNotFoundException extends EntityNotFoundException {
    public CommentNotFoundException(String message, String entityDetail) {
        super(message, entityDetail);
    }

    public CommentNotFoundException(String message, Throwable cause, String entityDetail) {
        super(message, cause, entityDetail);
    }
}
