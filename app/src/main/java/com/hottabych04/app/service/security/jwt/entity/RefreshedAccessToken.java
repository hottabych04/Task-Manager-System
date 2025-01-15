package com.hottabych04.app.service.security.jwt.entity;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "Обновленный access токен")
public record RefreshedAccessToken(

        @Schema(description = "Access токен",
                example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk...")
        String accessToken,

        @Schema(description = "Срок окончания действия access токена",
                implementation = Instant.class)
        String accessTokenExpiry
) {
}
