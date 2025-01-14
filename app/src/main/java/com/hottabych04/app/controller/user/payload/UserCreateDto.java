package com.hottabych04.app.controller.user.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record UserCreateDto(
        @Email(message = "{user.request.valid.email}")
        String email,
        @NotEmpty(message = "{user.request.password.not_empty}")
        @Size(min = 8, max = 20, message = "{user.request.password.size}")
        String password
) {
}
