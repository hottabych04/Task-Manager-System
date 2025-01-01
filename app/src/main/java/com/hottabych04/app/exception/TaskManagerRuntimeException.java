package com.hottabych04.app.exception;

public class TaskManagerRuntimeException extends RuntimeException{
    public TaskManagerRuntimeException(String message) {
        super(message);
    }

    public TaskManagerRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
