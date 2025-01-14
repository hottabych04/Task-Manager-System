package com.hottabych04.app.exception.token;

import com.hottabych04.app.exception.TaskManagerRuntimeException;
import org.springframework.http.HttpStatus;

public class TokenSerializeException extends TaskManagerRuntimeException {
    public TokenSerializeException(String message) {
        super("token.create.error", HttpStatus.BAD_REQUEST, null);
    }
}
