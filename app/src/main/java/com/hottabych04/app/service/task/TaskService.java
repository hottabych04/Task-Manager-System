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
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public List<TaskGetDto> handleGetRequest(String author, String performer){
        if (author != null){
            return getTaskByAuthor(author);
        }

        if (performer != null) {
            return getTasksByPerformer(performer);
        }

        return getTasks();
    }

    public List<TaskGetDto> getTaskByAuthor(String email){
        User author = userService.getUserEntity(email);
        List<Task> tasks = taskRepository.findByAuthor(author);

        return taskMapper.toTasksGetDto(tasks);
    }

    public List<TaskGetDto> getTasksByPerformer(String email){
        User performer = userService.getUserEntity(email);
        List<Task> tasks = taskRepository.findByPerformersContains(performer);

        return taskMapper.toTasksGetDto(tasks);
    }

    public List<TaskGetDto> getTasks(){
        List<Task> tasks = taskRepository.findAll();
        return taskMapper.toTasksGetDto(tasks);
    }

    public void delete(Long id){
        taskRepository.deleteById(id);
    }

}
