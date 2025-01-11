package com.hottabych04.app.controller.task.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record TaskCreateDto(
        @NotBlank
        String name,
        String description,
        @NotBlank
        String status,
        String priority,
        List<@Email String> performers
) {
}
