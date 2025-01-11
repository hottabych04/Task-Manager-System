package com.hottabych04.app.controller.user.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record UserCreateDto(
        @Email
        String email,
        @NotEmpty
        @Size(min = 8, max = 20)
        String password
) {
}
