package com.hottabych04.app.service.task;

import com.hottabych04.app.controller.task.payload.TaskCreateDto;
import com.hottabych04.app.controller.task.payload.TaskGetDto;
import com.hottabych04.app.database.entity.Task;
import com.hottabych04.app.database.entity.User;
import com.hottabych04.app.database.repository.TaskRepository;
import com.hottabych04.app.exception.task.TaskNotFoundException;
import com.hottabych04.app.service.priority.PriorityService;
import com.hottabych04.app.service.status.StatusService;
import com.hottabych04.app.service.task.mapper.TaskMapper;
import com.hottabych04.app.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Log4j2
@Transactional
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    private final StatusService statusService;
    private final PriorityService priorityService;
    private final UserService userService;

    public TaskGetDto createTask(TaskCreateDto taskDto, Authentication authentication){
        Task task = Task.builder()
                .name(taskDto.name())
                .description(taskDto.description())
                .status(
                        statusService.getStatus(taskDto.status())
                )
                .author(
                        userService.getUserEntity(authentication.getName())
                )
                .build();

        if (taskDto.priority() != null){
            task.setPriority(
                    priorityService.getPriority(taskDto.priority())
            );
        }

        if (taskDto.performers() != null && !taskDto.performers().isEmpty()){
            task.setPerformers(
               taskDto.performers().stream()
                       .map(userService::getUserEntity)
                       .toList()
            );
        }

        Task newTask = taskRepository.save(task);

        return taskMapper.toTaskGetDto(newTask);
    }

    public TaskGetDto getTask(Long id){
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Task by id: " + id + " is not found");
                    return new TaskNotFoundException("Task not found", id.toString());
                });

        return taskMapper.toTaskGetDto(task);
    }

    public Page<TaskGetDto> handleGetRequest(String author, String performer, Integer page, Integer size){

        PageRequest request = PageRequest.of(page, size);

        if (author != null){
            return getTaskByAuthor(author, request);
        }

        if (performer != null) {
            return getTasksByPerformer(performer, request);
        }

        return getTasks(request);
    }

    public Page<TaskGetDto> getTaskByAuthor(String email, Pageable request){
        User author = userService.getUserEntity(email);
        Page<Task> tasks = taskRepository.findByAuthor(author, request);

        return tasks.map(taskMapper::toTaskGetDto);
    }

    public Page<TaskGetDto> getTasksByPerformer(String email, Pageable request){
        User performer = userService.getUserEntity(email);
        Page<Task> tasks = taskRepository.findByPerformersContains(performer, request);

        return tasks.map(taskMapper::toTaskGetDto);
    }

    public Page<TaskGetDto> getTasks(Pageable request){
        Page<Task> tasks = taskRepository.findAll(request);
        return tasks.map(taskMapper::toTaskGetDto);
    }

    public void delete(Long id){
        taskRepository.deleteById(id);
    }

}
