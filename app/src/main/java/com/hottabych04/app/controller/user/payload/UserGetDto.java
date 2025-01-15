package com.hottabych04.app.controller.user.payload;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Структура для получения пользователя")
public record UserGetDto(

   @Schema(description = "Идентификатор пользователя",
           example = "1",
           accessMode = Schema.AccessMode.READ_ONLY)
   Long id,

   @Schema(description = "Эл. почта пользователя",
           example = "user@example.com")
   String email,

   @Schema(description = "Роли пользователя",
           allowableValues = {"USER", "ADMIN"})
   List<String> roles
) {
}
