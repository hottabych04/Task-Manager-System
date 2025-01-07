package com.hottabych04.app.controller.task;

import com.hottabych04.app.controller.task.payload.TaskCreateDto;
import com.hottabych04.app.controller.task.payload.TaskGetDto;
import com.hottabych04.app.service.task.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public TaskGetDto createTask(@RequestBody TaskCreateDto task, Authentication authentication){
        return taskService.createTask(task, authentication);
    }

    @GetMapping("/{id}")
    public TaskGetDto getTask(@PathVariable Long id){
        return taskService.getTask(id);
    }

    @GetMapping
    public List<TaskGetDto> getTasks(){
        return taskService.getTasks();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        taskService.delete(id);
    }

}
