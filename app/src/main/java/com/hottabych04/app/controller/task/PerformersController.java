package com.hottabych04.app.controller.task;

import com.hottabych04.app.service.task.PerformerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tasks/performers")
public class PerformersController {

    private final PerformerService performerService;

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void add(
            @PathVariable Long id,
            @RequestBody List<String> performers
    ){
        performerService.addPerformers(id, performers);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(
            @PathVariable Long id,
            @RequestParam("email") String performer
    ){
        performerService.deletePerformer(id, performer);
    }
}

