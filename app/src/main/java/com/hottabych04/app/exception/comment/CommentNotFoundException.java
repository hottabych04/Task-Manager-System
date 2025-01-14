package com.hottabych04.app.exception.comment;

import com.hottabych04.app.exception.EntityNotFoundException;

public class CommentNotFoundException extends EntityNotFoundException {
    public CommentNotFoundException(String entityDetail) {
        super("comment.not.found", entityDetail);
    }
}
