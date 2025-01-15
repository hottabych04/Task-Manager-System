package com.hottabych04.app.controller.comment.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hottabych04.app.controller.user.payload.UserIdsDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Структура для получения комментария")
public record CommentGetDto(

        @Schema(description = "Идентификатор комментария",
                example = "1",
                accessMode = Schema.AccessMode.READ_ONLY)
        Long id,

        @Schema(description = "Текст комментария",
                example = "Текст комментария")
        String message,

        @Schema(description = "Идентификаторы автора")
        UserIdsDto author,

        @Schema(description = "Идентификатор задачи",
                example = "1")
        @JsonProperty("taskId")
        Long task,

        @Schema(description = "Время создания комментария",
                accessMode = Schema.AccessMode.READ_ONLY)
        LocalDateTime createdAt
) {
}
