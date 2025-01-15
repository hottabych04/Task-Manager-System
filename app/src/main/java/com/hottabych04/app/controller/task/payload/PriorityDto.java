package com.hottabych04.app.controller.task.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Запрос для обновления приоритета")
public record PriorityDto(

        @Schema(description = "Приоритет",
                example = "LOW",
                allowableValues = {"LOW", "MIDDLE", "HIGH"})
        @NotBlank(message = "{priority.request.priority.not_empty}")
        String priority
) {
}
