package com.hottabych04.app.controller.comment.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CommentCreateDto(
        String message,
        @JsonProperty("taskId")
        Long task
) {
}
