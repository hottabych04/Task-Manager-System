package com.hottabych04.app.controller.comment.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;


@Schema(description = "Запрос для создания комментария")
public record CommentCreateDto(

        @Schema(description = "Текст комментария",
                example = "Текст комментария")
        @NotEmpty(message = "{comment.request.message.not_empty}")
        String message,

        @Schema(description = "Идентификатор задачи для которой требуется создать комментарий",
                example = "1")
        @Positive(message = "{comment.request.task.positive}")
        @NotNull(message = "{comment.request.task.not_null}")
        @JsonProperty("taskId")
        Long task
) {
}
