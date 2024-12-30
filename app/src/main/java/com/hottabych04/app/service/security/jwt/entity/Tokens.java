package com.hottabych04.app.service.security.jwt.entity;

public record Tokens(
        String accessToken, String accessTokenExpiry,
        String refreshToken, String refreshTokenExpiry
) {
}
