package com.hottabych04.app.service.task;

import com.hottabych04.app.controller.task.payload.PriorityDto;
import com.hottabych04.app.controller.task.payload.StatusDto;
import com.hottabych04.app.controller.task.payload.TaskCreateDto;
import com.hottabych04.app.controller.task.payload.TaskGetDto;
import com.hottabych04.app.controller.user.payload.UserIdsDto;
import com.hottabych04.app.database.entity.Priority;
import com.hottabych04.app.database.entity.Status;
import com.hottabych04.app.database.entity.Task;
import com.hottabych04.app.database.entity.User;
import com.hottabych04.app.database.repository.TaskRepository;
import com.hottabych04.app.exception.task.TaskNotFoundException;
import com.hottabych04.app.service.priority.PriorityService;
import com.hottabych04.app.service.security.AuthorizationUtil;
import com.hottabych04.app.service.status.StatusService;
import com.hottabych04.app.service.task.mapper.TaskMapper;
import com.hottabych04.app.service.user.UserService;
import com.hottabych04.app.IntegrationTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class TaskServiceTest {

    private static final String DUMMY_EMAIL = "dummy@example.com";
    private static final String DUMMY = "dummy";

    private TaskRepository taskRepository = Mockito.mock(TaskRepository.class);
    private TaskMapper taskMapper = Mockito.mock(TaskMapper.class);
    private StatusService statusService = Mockito.mock(StatusService.class);
    private PriorityService priorityService = Mockito.mock(PriorityService.class);
    private UserService userService = Mockito.mock(UserService.class);
    private AuthorizationUtil authorizationUtil = Mockito.mock(AuthorizationUtil.class);

    private TaskService taskService = new TaskService(
            taskRepository,
            taskMapper,
            statusService,
            priorityService,
            userService,
            authorizationUtil
    );

    @Test
    @DisplayName("Success create new task")
    public void createTask(){

        Authentication authentication = new PreAuthenticatedAuthenticationToken(DUMMY_EMAIL, null);

        TaskCreateDto taskCreateDto = new TaskCreateDto(DUMMY, DUMMY, DUMMY, DUMMY, List.of(DUMMY_EMAIL));

        Priority dummyPriority = new Priority(1, DUMMY);
        Status dummyStatus = new Status(1, DUMMY);

        User user = new User(
                1L,
                authentication.getName(),
                "dummyPassword",
                null,
                null
        );
        UserIdsDto userIdsDto = new UserIdsDto(1L, DUMMY_EMAIL);

        Task dummyTask = Task.builder()
                .name(taskCreateDto.name())
                .description(taskCreateDto.description())
                .status(dummyStatus)
                .priority(dummyPriority)
                .author(user)
                .performers(List.of(user))
                .build();
        TaskGetDto taskGetDto = new TaskGetDto(
                dummyTask.getId(),
                dummyTask.getName(),
                dummyTask.getDescription(),
                dummyStatus.getName(),
                dummyPriority.getName(),
                userIdsDto,
                List.of(userIdsDto)
        );

        Mockito.when(statusService.getStatus(taskCreateDto.status())).thenReturn(dummyStatus);
        Mockito.when(userService.getUserEntity(authentication.getName())).thenReturn(user);
        Mockito.when(priorityService.getPriority(taskCreateDto.priority())).thenReturn(dummyPriority);
        Mockito.when(taskRepository.save(dummyTask)).thenReturn(dummyTask);
        Mockito.when(taskMapper.toTaskGetDto(dummyTask)).thenReturn(taskGetDto);

        TaskGetDto task = taskService.createTask(taskCreateDto, authentication);

        assertThat(task).isEqualTo(taskGetDto);
    }

    @Test
    @DisplayName("Success get TaskGetDto by id")
    public void getTask(){
        Long dummyId = 1L;

        Task dummyTask = Task.builder()
                .name(DUMMY)
                .description(DUMMY)
                .build();
        TaskGetDto taskGetDto = new TaskGetDto(
                dummyTask.getId(),
                dummyTask.getName(),
                dummyTask.getDescription(),
                null,
                null,
                null,
                null
        );

        Mockito.when(taskRepository.findById(dummyId)).thenReturn(Optional.of(dummyTask));
        Mockito.when(taskMapper.toTaskGetDto(dummyTask)).thenReturn(taskGetDto);

        TaskGetDto task = taskService.getTask(dummyId);

        assertThat(task).isEqualTo(taskGetDto);
    }

    @Test
    @DisplayName("Success get tasks by author")
    public void getTasksByAuthor(){
        User user = new User(
                1L,
                DUMMY_EMAIL,
                "dummyPassword",
                null,
                null
        );
        UserIdsDto userIdsDto = new UserIdsDto(user.getId(), user.getEmail());

        Integer page = 1, size = 1;
        Pageable pageable = PageRequest.of(page, size);

        Task dummyTask = Task.builder()
                .id(1L)
                .name(DUMMY)
                .description(DUMMY)
                .author(user)
                .build();
        TaskGetDto taskGetDto = new TaskGetDto(
                dummyTask.getId(),
                dummyTask.getName(),
                dummyTask.getDescription(),
                null,
                null,
                userIdsDto,
                null
        );

        Page<Task> taskPage = new PageImpl<Task>(List.of(dummyTask), pageable, 1L);
        Page<TaskGetDto> taskGetDtos = taskPage.map(it -> taskGetDto);

        Mockito.when(userService.getUserEntity(DUMMY_EMAIL)).thenReturn(user);
        Mockito.when(taskRepository.findByAuthor(user, pageable)).thenReturn(taskPage);
        Mockito.when(taskMapper.toTaskGetDto(dummyTask)).thenReturn(taskGetDto);

        Page<TaskGetDto> tasks = taskService.getTasksByAuthor(DUMMY_EMAIL, page, size);

        assertThat(tasks).isNotEmpty().isEqualTo(taskGetDtos);
    }

    @Test
    @DisplayName("Success get tasks by performer")
    public void getTasksByPerformer(){
        User performer = new User(
                1L,
                DUMMY_EMAIL,
                "dummyPassword",
                null,
                null
        );
        UserIdsDto performerIdsDto = new UserIdsDto(performer.getId(), performer.getEmail());

        Integer page = 1, size = 1;
        Pageable pageable = PageRequest.of(page, size);

        Task dummyTask = Task.builder()
                .id(1L)
                .name(DUMMY)
                .description(DUMMY)
                .performers(List.of(performer))
                .build();
        TaskGetDto taskGetDto = new TaskGetDto(
                dummyTask.getId(),
                dummyTask.getName(),
                dummyTask.getDescription(),
                null,
                null,
                null,
                List.of(performerIdsDto)
        );

        Page<Task> taskPage = new PageImpl<Task>(List.of(dummyTask), pageable, 1L);
        Page<TaskGetDto> taskGetDtos = taskPage.map(it -> taskGetDto);

        Mockito.when(userService.getUserEntity(DUMMY_EMAIL)).thenReturn(performer);
        Mockito.when(taskRepository.findByPerformersContains(performer, pageable)).thenReturn(taskPage);
        Mockito.when(taskMapper.toTaskGetDto(dummyTask)).thenReturn(taskGetDto);

        Page<TaskGetDto> tasks = taskService.getTasksByPerformer(DUMMY_EMAIL, page, size);

        assertThat(tasks).isNotEmpty().isEqualTo(taskGetDtos);
    }

    @Test
    @DisplayName("Success get all tasks")
    public void getTasks(){
        Integer page = 1, size = 1;
        Pageable pageable = PageRequest.of(page, size);

        Task dummyTask = Task.builder()
                .id(1L)
                .name(DUMMY)
                .description(DUMMY)
                .build();
        TaskGetDto taskGetDto = new TaskGetDto(
                dummyTask.getId(),
                dummyTask.getName(),
                dummyTask.getDescription(),
                null,
                null,
                null,
                null
        );

        Page<Task> taskPage = new PageImpl<Task>(List.of(dummyTask), pageable, 1L);
        Page<TaskGetDto> taskGetDtos = taskPage.map(it -> taskGetDto);

        Mockito.when(taskRepository.findAll(pageable)).thenReturn(taskPage);
        Mockito.when(taskMapper.toTaskGetDto(dummyTask)).thenReturn(taskGetDto);

        Page<TaskGetDto> tasks = taskService.getTasks(page, size);

        assertThat(tasks).isNotEmpty().isEqualTo(taskGetDtos);
    }

    @Test
    @DisplayName("Success update task status for admin")
    public void updateTaskStatusForAdmin(){
        Long taskId = 1L;
        StatusDto statusDto = new StatusDto(DUMMY);
        Authentication authentication = new PreAuthenticatedAuthenticationToken(DUMMY_EMAIL,null);

        Status dummyStatus = new Status(1, DUMMY);
        Task dummyTask = Task.builder()
                .id(1L)
                .name(DUMMY)
                .description(DUMMY)
                .build();
        Task dummyTaskWithStatus = Task.builder()
                .id(dummyTask.getId())
                .name(dummyTask.getName())
                .description(dummyTask.getDescription())
                .status(dummyStatus)
                .build();
        TaskGetDto taskGetDto = new TaskGetDto(
                dummyTaskWithStatus.getId(),
                dummyTaskWithStatus.getName(),
                dummyTaskWithStatus.getDescription(),
                dummyTaskWithStatus.getStatus().getName(),
                null,
                null,
                null
        );

        Mockito.when(statusService.getStatus(dummyStatus.getName())).thenReturn(dummyStatus);
        Mockito.when(taskRepository.findById(dummyTask.getId())).thenReturn(Optional.of(dummyTask));
        Mockito.when(authorizationUtil.isAdmin(authentication)).thenReturn(true);
        Mockito.when(taskRepository.save(dummyTaskWithStatus)).thenReturn(dummyTaskWithStatus);
        Mockito.when(taskMapper.toTaskGetDto(dummyTaskWithStatus)).thenReturn(taskGetDto);

        TaskGetDto updatedTaskStatus = taskService.updateTaskStatus(taskId, statusDto, authentication);

        assertThat(updatedTaskStatus).isEqualTo(taskGetDto);
    }

    @Test
    @DisplayName("Success update task status for performer")
    public void updateTaskStatusForPerformer(){
        Long taskId = 1L;
        StatusDto statusDto = new StatusDto(DUMMY);
        Authentication authentication = new PreAuthenticatedAuthenticationToken(DUMMY_EMAIL,null);

        Status dummyStatus = new Status(
                1,
                DUMMY
        );
        User performer = User.builder()
                .id(1L)
                .email(authentication.getName())
                .hashedPassword(DUMMY)
                .build();
        UserIdsDto performerIdsDto = new UserIdsDto(
                performer.getId(),
                performer.getEmail()
        );
        Task dummyTask = Task.builder()
                .id(1L)
                .name(DUMMY)
                .description(DUMMY)
                .performers(List.of(performer))
                .build();
        Task dummyTaskWithStatus = Task.builder()
                .id(dummyTask.getId())
                .name(dummyTask.getName())
                .description(dummyTask.getDescription())
                .status(dummyStatus)
                .performers(dummyTask.getPerformers())
                .build();
        TaskGetDto taskGetDto = new TaskGetDto(
                dummyTaskWithStatus.getId(),
                dummyTaskWithStatus.getName(),
                dummyTaskWithStatus.getDescription(),
                dummyTaskWithStatus.getStatus().getName(),
                null,
                null,
                List.of(performerIdsDto)
        );

        Mockito.when(statusService.getStatus(dummyStatus.getName())).thenReturn(dummyStatus);
        Mockito.when(taskRepository.findById(dummyTask.getId())).thenReturn(Optional.of(dummyTask));
        Mockito.when(authorizationUtil.isAdmin(authentication)).thenReturn(false);
        Mockito.when(taskRepository.save(dummyTaskWithStatus)).thenReturn(dummyTaskWithStatus);
        Mockito.when(taskMapper.toTaskGetDto(dummyTaskWithStatus)).thenReturn(taskGetDto);

        TaskGetDto updatedTaskStatus = taskService.updateTaskStatus(taskId, statusDto, authentication);

        assertThat(updatedTaskStatus).isEqualTo(taskGetDto);
    }

    @Test
    @DisplayName("Success update task priority")
    public void updateTaskPriority(){
        Long taskId = 1L;
        PriorityDto priorityDto = new PriorityDto(DUMMY);

        Priority dummyPriority = Priority.builder()
                .id(1)
                .name(priorityDto.priority())
                .build();
        Task dummyTask = Task.builder()
                .id(1L)
                .name(DUMMY)
                .description(DUMMY)
                .build();
        Task dummyTaskWithPriority = Task.builder()
                .id(dummyTask.getId())
                .name(dummyTask.getName())
                .description(dummyTask.getDescription())
                .priority(dummyPriority)
                .performers(dummyTask.getPerformers())
                .build();
        TaskGetDto taskGetDto = new TaskGetDto(
                dummyTaskWithPriority.getId(),
                dummyTaskWithPriority.getName(),
                dummyTaskWithPriority.getDescription(),
                null,
                dummyTaskWithPriority.getPriority().getName(),
                null,
                null
        );

        Mockito.when(priorityService.getPriority(priorityDto.priority())).thenReturn(dummyPriority);
        Mockito.when(taskRepository.findById(dummyTask.getId())).thenReturn(Optional.of(dummyTask));
        Mockito.when(taskRepository.save(dummyTaskWithPriority)).thenReturn(dummyTaskWithPriority);
        Mockito.when(taskMapper.toTaskGetDto(dummyTaskWithPriority)).thenReturn(taskGetDto);

        TaskGetDto updateTaskPriority = taskService.updateTaskPriority(taskId, priorityDto);

        assertThat(updateTaskPriority).isEqualTo(taskGetDto);
    }

    @Test
    @DisplayName("Success get task entity by id")
    public void getTaskEntity(){
        Task dummyTask = Task.builder()
                .id(1L)
                .name(DUMMY)
                .description(DUMMY)
                .build();

        Mockito.when(taskRepository.findById(dummyTask.getId())).thenReturn(Optional.of(dummyTask));

        Task task = taskService.getTaskEntity(dummyTask.getId());

        assertThat(task).isEqualTo(dummyTask);
    }

    @Test
    @DisplayName("Get non existing task entity by id")
    public void throwTaskNotFoundWhenGetTaskEntity(){
        Long taskId = 1L;

        Mockito.when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskEntity(taskId));
    }

    @Test
    @DisplayName("Success delete task by id")
    public void deleteTask(){
        Long taskId = 1L;

        Mockito.doNothing().when(taskRepository).deleteById(taskId);

        taskService.delete(taskId);
    }
}
