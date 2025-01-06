package com.hottabych04.app.controller.user.payload;

public record UserCreateDto(
        String email,
        String password
) {
}
