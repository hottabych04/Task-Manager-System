package com.hottabych04.app.controller.task.payload;

import com.hottabych04.app.controller.user.payload.UserIdsDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Структура для получения задачи")
public record TaskGetDto(

        @Schema(description = "Идентификатор задачи",
                example = "1",
                accessMode = Schema.AccessMode.READ_ONLY)
        Long id,

        @Schema(description = "Название задачи",
                example = "Название")
        String name,

        @Schema(description = "Описание задачи",
                example = "Описание", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        String description,

        @Schema(description = "Статус задачи",
                example = "WAIT",
                allowableValues = {"WAIT", "IN_PROGRESS", "DONE"})
        String status,

        @Schema(description = "Приоритет задачи",
                example = "LOW", requiredMode = Schema.RequiredMode.NOT_REQUIRED,
                allowableValues = {"LOW", "MIDDLE", "HIGH"})
        String priority,

        @Schema(description = "Автор задачи")
        UserIdsDto author,

        @Schema(description = "Исполнители задачи",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        List<UserIdsDto> performers
) {
}
