package com.hottabych04.app.controller.jwt.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @Email(message = "{user.request.valid.email}")
        String email,
        @NotBlank(message = "{user.request.password.not_empty}")
        @Size(min = 8, max = 20, message = "{user.request.password.size}")
        String password
) {
}
