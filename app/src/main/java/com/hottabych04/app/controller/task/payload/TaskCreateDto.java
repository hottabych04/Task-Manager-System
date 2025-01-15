package com.hottabych04.app.controller.task.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Schema(description = "Запрос для создания задачи")
public record TaskCreateDto(

        @Schema(description = "Название задачи",
                example = "Название")
        @NotBlank(message = "{task.request.name.not_empty}")
        String name,

        @Schema(description = "Описание задачи",
                example = "Текст описания", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        String description,

        @Schema(description = "Статус задачи",
                example = "WAIT", defaultValue = "WAIT",
                allowableValues = {"WAIT", "IN_PROGRESS", "DONE"})
        @NotBlank(message = "{task.request.status.not_empty}")
        String status,

        @Schema(description = "Приоритет задачи",
                example = "LOW", requiredMode = Schema.RequiredMode.NOT_REQUIRED,
                allowableValues = {"LOW", "MIDDLE", "HIGH"})
        String priority,

        @Schema(description = "Список исполнителей",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        List<@Email(message = "{task.request.performers.valid.email}") String> performers
) {
}
