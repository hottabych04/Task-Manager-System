package com.hottabych04.app.controller.task.payload;

import com.hottabych04.app.controller.user.payload.UserIdsDto;

import java.util.List;

public record TaskGetDto(
        Long id,
        String name,
        String description,
        String status,
        String priority,
        UserIdsDto author,
        List<UserIdsDto> performers
) {
}
