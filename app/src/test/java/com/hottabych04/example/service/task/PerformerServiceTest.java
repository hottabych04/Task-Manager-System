package com.hottabych04.example.service.task;

import com.hottabych04.app.controller.task.payload.TaskGetDto;
import com.hottabych04.app.controller.user.payload.UserIdsDto;
import com.hottabych04.app.database.entity.Task;
import com.hottabych04.app.database.entity.User;
import com.hottabych04.app.database.repository.TaskRepository;
import com.hottabych04.app.exception.task.TaskNotFoundException;
import com.hottabych04.app.service.task.PerformerService;
import com.hottabych04.app.service.task.mapper.TaskMapper;
import com.hottabych04.app.service.user.UserService;
import com.hottabych04.example.IntegrationTestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class PerformerServiceTest extends IntegrationTestBase {

    private static final String DUMMY_EMAIL = "dummy@example.com";
    private static final String DUMMY = "dummy";

    @Mock
    private TaskRepository taskRepository;
    @Mock
    private TaskMapper taskMapper;
    @Mock
    private UserService userService;

    @InjectMocks
    private PerformerService performerService;

    @Test
    @DisplayName("Success add performers to task")
    public void addPerformers(){
        List<String> dummyEmails = List.of(DUMMY_EMAIL);
        User performer = new User(
                1L,
                DUMMY_EMAIL,
                DUMMY,
                null,
                null
        );
        UserIdsDto performerIdsDto = new UserIdsDto(performer.getId(), performer.getEmail());
        Task dummyTask = Task.builder()
                .id(1L)
                .name(DUMMY)
                .description(DUMMY)
                .performers(new ArrayList<User>())
                .build();
        Task dummyTaskWithPerformer = Task.builder()
                .id(dummyTask.getId())
                .name(dummyTask.getName())
                .description(dummyTask.getDescription())
                .performers(List.of(performer))
                .build();
        TaskGetDto taskGetDto = new TaskGetDto(
                dummyTaskWithPerformer.getId(),
                dummyTaskWithPerformer.getName(),
                dummyTaskWithPerformer.getDescription(),
                null,
                null,
                null,
                List.of(performerIdsDto)
        );

        Mockito.when(taskRepository.findById(dummyTask.getId())).thenReturn(Optional.of(dummyTask));
        Mockito.when(userService.getUserEntity(performer.getEmail())).thenReturn(performer);
        Mockito.when(taskRepository.save(dummyTaskWithPerformer)).thenReturn(dummyTaskWithPerformer);
        Mockito.when(taskMapper.toTaskGetDto(dummyTaskWithPerformer)).thenReturn(taskGetDto);

        TaskGetDto addedPerformers = performerService.addPerformers(dummyTask.getId(), dummyEmails);

        assertThat(addedPerformers).isEqualTo(taskGetDto);
    }

    @Test
    @DisplayName("Try update performers in non existing task")
    public void throwTaskNotFoundExceptionWhenUpdatePerformers(){
        Long taskId = 1L;
        List<String> dummyEmails = List.of(DUMMY_EMAIL);

        Mockito.when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> performerService.addPerformers(taskId, dummyEmails));
    }

    @Test
    @DisplayName("Success delete performer")
    public void deletePerformer(){
        User performer = new User(
                1L,
                DUMMY_EMAIL,
                DUMMY,
                null,
                null
        );
        Task dummyTask = Task.builder()
                .id(1L)
                .name(DUMMY)
                .description(DUMMY)
                .performers(new ArrayList<User>())
                .build();
        dummyTask.getPerformers().add(performer);
        Task dummyTaskWithoutPerformer = Task.builder()
                .id(1L)
                .name(DUMMY)
                .description(DUMMY)
                .performers(List.of())
                .build();
        TaskGetDto taskGetDto = new TaskGetDto(
                dummyTask.getId(),
                dummyTask.getName(),
                dummyTask.getDescription(),
                null,
                null,
                null,
                List.of()
        );

        Mockito.when(userService.getUserEntity(performer.getEmail())).thenReturn(performer);
        Mockito.when(taskRepository.findById(dummyTask.getId())).thenReturn(Optional.of(dummyTask));
        Mockito.when(taskRepository.save(dummyTaskWithoutPerformer)).thenReturn(dummyTaskWithoutPerformer);
        Mockito.when(taskMapper.toTaskGetDto(dummyTaskWithoutPerformer)).thenReturn(taskGetDto);

        TaskGetDto deletePerformer = performerService.deletePerformer(dummyTask.getId(), performer.getEmail());

        assertThat(deletePerformer).isEqualTo(taskGetDto);
    }

    @Test
    @DisplayName("Try delete performer in non existing task")
    public void throwTaskNotFoundExceptionWhenDeletePerformer(){
        Long taskId = 1L;

        Mockito.when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> performerService.deletePerformer(taskId, DUMMY_EMAIL));
    }
}
