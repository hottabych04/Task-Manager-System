package com.hottabych04.app.controller.comment.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hottabych04.app.controller.user.payload.UserIdsDto;

import java.time.LocalDateTime;

public record CommentGetDto(
        Long id,
        String message,
        UserIdsDto author,
        @JsonProperty("taskId")
        Long task,
        LocalDateTime createdAt
) {
}
