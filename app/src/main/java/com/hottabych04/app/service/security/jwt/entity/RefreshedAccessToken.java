package com.hottabych04.app.service.security.jwt.entity;

public record RefreshedAccessToken(
        String accessToken, String accessTokenExpiry
) {
}
