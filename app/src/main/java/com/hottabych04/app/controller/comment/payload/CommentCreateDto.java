package com.hottabych04.app.controller.comment.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;


public record CommentCreateDto(
        @NotEmpty
        String message,
        @Positive
        @NotNull
        @JsonProperty("taskId")
        Long task
) {
}
