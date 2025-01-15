package com.hottabych04.app.controller.user.payload;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Структура для получения идентификаторов пользователя")
public record UserIdsDto(

        @Schema(description = "Идентификатор пользователя",
                example = "1",
                accessMode = Schema.AccessMode.READ_ONLY)
        Long id,

        @Schema(description = "Эл. почта пользователя",
                example = "user@example.com")
        String email
) {
}
