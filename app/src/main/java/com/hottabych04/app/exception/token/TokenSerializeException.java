package com.hottabych04.app.exception.token;

import com.hottabych04.app.exception.TaskManagerRuntimeException;

public class TokenSerializeException extends TaskManagerRuntimeException {
    public TokenSerializeException(String message) {
        super(message);
    }

    public TokenSerializeException(String message, Throwable cause) {
        super(message, cause);
    }
}
