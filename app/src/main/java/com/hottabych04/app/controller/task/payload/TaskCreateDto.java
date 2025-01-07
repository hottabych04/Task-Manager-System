package com.hottabych04.app.controller.task.payload;

import java.util.List;

public record TaskCreateDto(
        String name,
        String description,
        String status,
        String priority,
        List<String> performers
) {
}
