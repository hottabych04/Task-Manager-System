package com.hottabych04.app.service.task.mapper;

import com.hottabych04.app.controller.task.payload.TaskGetDto;
import com.hottabych04.app.controller.user.payload.UserIdsDto;
import com.hottabych04.app.database.entity.Priority;
import com.hottabych04.app.database.entity.Status;
import com.hottabych04.app.database.entity.Task;
import com.hottabych04.app.database.entity.User;
import com.hottabych04.app.service.priority.mapper.PriorityMapper;
import com.hottabych04.app.service.status.mapper.StatusMapper;
import com.hottabych04.app.service.user.mapper.UserMapper;
import com.hottabych04.app.IntegrationTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class TaskMapperTest {

    private static final String DUMMY = "dummy";

    private UserMapper userMapper = Mockito.mock(UserMapper.class);
    private PriorityMapper priorityMapper = Mockito.mock(PriorityMapper.class);
    private StatusMapper statusMapper = Mockito.mock(StatusMapper.class);

    private TaskMapper taskMapper = new TaskMapperImpl(userMapper, priorityMapper, statusMapper);

    @Test
    @DisplayName("Map Task entity to TaskGetDto")
    public void toTaskGetDto(){
        Status dummyStatus = Status.builder()
                .id(1)
                .name(DUMMY)
                .build();
        Priority dummyPriority = Priority.builder()
                .id(1)
                .name(DUMMY)
                .build();
        User dummyUser = User.builder()
                .id(1L)
                .email(DUMMY)
                .hashedPassword(DUMMY)
                .build();
        UserIdsDto userIdsDto = new UserIdsDto(
                dummyUser.getId(),
                dummyUser.getEmail()
        );

        Mockito.when(statusMapper.toStatusString(dummyStatus)).thenReturn(dummyStatus.getName());
        Mockito.when(priorityMapper.toPriorityString(dummyPriority)).thenReturn(dummyPriority.getName());
        Mockito.when(userMapper.toUserIdsDto(dummyUser)).thenReturn(userIdsDto);

        Task task = Task.builder()
                .id(1L)
                .name(DUMMY)
                .description(DUMMY)
                .status(dummyStatus)
                .priority(dummyPriority)
                .author(dummyUser)
                .performers(List.of(dummyUser))
                .build();

        TaskGetDto taskGetDto = taskMapper.toTaskGetDto(task);

        assertAll(() -> {
            assertThat(taskGetDto).isNotNull();
            assertThat(taskGetDto.name()).isEqualTo(task.getName());
            assertThat(taskGetDto.description()).isEqualTo(task.getDescription());
            assertThat(taskGetDto.status()).isEqualTo(dummyStatus.getName());
            assertThat(taskGetDto.priority()).isEqualTo(dummyPriority.getName());
            assertThat(taskGetDto.author()).isEqualTo(userIdsDto);
            assertThat(taskGetDto.performers()).isEqualTo(List.of(userIdsDto));
        });
    }

    @Test
    @DisplayName("Map Task entity to her id")
    public void getTaskId(){
        Task task = Task.builder().id(1L).build();

        Long taskId = taskMapper.getTaskId(task);
        assertThat(taskId).isEqualTo(task.getId());
    }
}
