package com.hottabych04.app.controller.task.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record TaskCreateDto(
        @NotBlank(message = "{task.request.name.not_empty}")
        String name,
        String description,
        @NotBlank(message = "{task.request.status.not_empty}")
        String status,
        String priority,
        List<@Email(message = "{task.request.performers.valid.email}") String> performers
) {
}
