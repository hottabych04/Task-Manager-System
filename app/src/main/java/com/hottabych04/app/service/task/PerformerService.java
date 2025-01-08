package com.hottabych04.app.service.task;

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

    public void addPerformers(Long id, List<String> emails){
        if (emails != null && !emails.isEmpty()){
            emails.forEach(it -> addPerformer(id, it));
        }
    }

    public void addPerformer(Long id, String email){
        User performer = userService.getUserEntity(email);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Task by id: " + id + " is not found");
                    return new TaskNotFoundException("Task not found", id.toString());
                });

        task.getPerformers().add(performer);

        taskRepository.save(task);
    }

    public void deletePerformer(Long id, String email){
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
    }
}
