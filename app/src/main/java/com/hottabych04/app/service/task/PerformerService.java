package com.hottabych04.app.service.task;

import com.hottabych04.app.controller.task.payload.TaskGetDto;
import com.hottabych04.app.database.entity.Task;
import com.hottabych04.app.database.entity.User;
import com.hottabych04.app.database.repository.TaskRepository;
import com.hottabych04.app.exception.task.PerformerNotFoundException;
import com.hottabych04.app.exception.task.TaskNotFoundException;
import com.hottabych04.app.service.task.mapper.TaskMapper;
import com.hottabych04.app.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Log4j2
@Transactional
@RequiredArgsConstructor
public class PerformerService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    private final UserService userService;

    public TaskGetDto addPerformers(Long id, List<String> emails){
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Task by id: " + id + " is not found");
                    return new TaskNotFoundException("Task not found", id.toString());
                });

        if (emails != null && !emails.isEmpty()){
            emails.forEach(it -> addPerformer(task, it));
            Task saved = taskRepository.save(task);
            return taskMapper.toTaskGetDto(saved);
        }

        return taskMapper.toTaskGetDto(task);
    }

    public void addPerformer(Task task, String email){
        User performer = userService.getUserEntity(email);
        task.getPerformers().add(performer);
    }

    public TaskGetDto deletePerformer(Long id, String email){
        User performer = userService.getUserEntity(email);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Task by id: " + id + " is not found");
                    return new TaskNotFoundException("Task not found", id.toString());
                });

        if (!task.getPerformers().remove(performer)){
            log.error("Performer: " + performer.getEmail() + " not fond in task: " + task.getId());
            throw new PerformerNotFoundException("Performer not found in this task", performer.getEmail());
        }

        return taskMapper.toTaskGetDto(task);
    }
}
