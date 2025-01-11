package com.hottabych04.app.controller.task;

import com.hottabych04.app.controller.task.payload.PriorityDto;
import com.hottabych04.app.controller.task.payload.StatusDto;
import com.hottabych04.app.controller.task.payload.TaskCreateDto;
import com.hottabych04.app.controller.task.payload.TaskGetDto;
import com.hottabych04.app.service.task.PerformerService;
import com.hottabych04.app.service.task.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskService taskService;
    private final PerformerService performerService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public TaskGetDto createTask(
            @RequestBody @Validated TaskCreateDto task,
            Authentication authentication
    ){
        return taskService.createTask(task, authentication);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public TaskGetDto getTask(@PathVariable Long id){
        return taskService.getTask(id);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public Page<TaskGetDto> getTasks(
            @RequestParam(value = "author", required = false) String author,
            @RequestParam(value = "performer", required = false) String performer,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "5") Integer size
    ){
        return taskService.handleGetRequest(author, performer, page, size);
    }

    @PatchMapping("/{id}/performers")
    @PreAuthorize("hasRole('ADMIN')")
    public TaskGetDto addPerformer(
            @PathVariable Long id,
            @RequestBody List<String> performers
    ){
        return performerService.addPerformers(id, performers);
    }

    @DeleteMapping("/{id}/performers")
    @PreAuthorize("hasRole('ADMIN')")
    public TaskGetDto deletePerformer(
            @PathVariable Long id,
            @RequestParam("email") String performer
    ){
        return performerService.deletePerformer(id, performer);
    }

    @PatchMapping("/{id}/priorities")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public TaskGetDto updatePriority(
            @PathVariable Long id,
            @RequestBody @Validated PriorityDto priority,
            Authentication authentication
            ){
        return taskService.updateTaskPriority(id, priority, authentication);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public TaskGetDto updatePriority(
            @PathVariable Long id,
            @RequestBody @Validated StatusDto status,
            Authentication authentication
            ){
        return taskService.updateTaskStatus(id, status, authentication);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        taskService.delete(id);
    }

}
