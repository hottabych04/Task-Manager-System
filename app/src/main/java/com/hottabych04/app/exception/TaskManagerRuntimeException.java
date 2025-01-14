package com.hottabych04.app.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class TaskManagerRuntimeException extends RuntimeException{

    private final HttpStatus httpStatus;
    private final Object[] args;

    public TaskManagerRuntimeException(String message, HttpStatus httpStatus, Object[] args) {
        super(message);
        this.httpStatus = httpStatus;
        this.args = args;
    }
}
