package com.hottabych04.app.controller.task.payload;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Запрос для обновления статуса")
public record StatusDto(

        @Schema(description = "Статус",
                example = "WAIT",
                allowableValues = {"WAIT", "IN_PROGRESS", "DONE"})
        @NotBlank(message = "{status.request.status.not_empty}")
        String status
) {
}
