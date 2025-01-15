package com.hottabych04.app.controller.user.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Запрос для регистрации пользователя")
public record UserCreateDto(

        @Schema(description = "Эл. почта пользователя",
                example = "user@example.com")
        @Email(message = "{user.request.valid.email}")
        String email,

        @Schema(description = "Пароль пользователя",
                example = "useruser",
                minLength = 8, maxLength = 20)
        @NotBlank(message = "{user.request.password.not_empty}")
        @Size(min = 8, max = 20, message = "{user.request.password.size}")
        String password
) {
}
