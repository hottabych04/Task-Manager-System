package com.hottabych04.app.controller.task.payload;


import jakarta.validation.constraints.NotBlank;

public record StatusDto(
        @NotBlank
        String status
) {
}
