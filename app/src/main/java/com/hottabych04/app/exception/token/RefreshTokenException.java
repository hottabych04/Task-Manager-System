package com.hottabych04.app.exception.token;

import com.hottabych04.app.exception.TaskManagerRuntimeException;
import org.springframework.http.HttpStatus;

public class RefreshTokenException extends TaskManagerRuntimeException {
    public RefreshTokenException() {
        super("token.refresh.not_refresh", HttpStatus.FORBIDDEN, null);
    }
}
