package com.hottabych04.app.service.jwt.entity;

public record Tokens(
        String accessToken, String accessTokenExpiry,
        String refreshToken, String refreshTokenExpiry
) {
}
