package com.hottabych04.app.controller.comment.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;


public record CommentCreateDto(
        @NotEmpty(message = "{comment.request.message.not_empty}")
        String message,
        @Positive(message = "{comment.request.task.positive}")
        @NotNull(message = "{comment.request.task.not_null}")
        @JsonProperty("taskId")
        Long task
) {
}
