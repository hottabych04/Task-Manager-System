package com.hottabych04.app.controller.task;

import com.hottabych04.app.controller.task.payload.PriorityDto;
import com.hottabych04.app.controller.task.payload.StatusDto;
import com.hottabych04.app.controller.task.payload.TaskCreateDto;
import com.hottabych04.app.controller.task.payload.TaskGetDto;
import com.hottabych04.app.service.task.PerformerService;
import com.hottabych04.app.service.task.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "JWT")
@RequestMapping("/api/v1/tasks")
@Tag(name = "Task controller", description = "API для работы с задачами")
public class TaskController {

    private final TaskService taskService;
    private final PerformerService performerService;

    @Operation(summary = "Создание новой задачи",
            description = "Создает новую задачу."
                    + " Доступно только администраторам")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное создание задачи",
                    content = @Content(schema = @Schema(implementation = TaskGetDto.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка создания задачи",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "403", description = "Ошибка аутентификации",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public TaskGetDto createTask(
            @RequestBody
            @Validated TaskCreateDto task,
            Authentication authentication
    ){
        return taskService.createTask(task, authentication);
    }

    @Operation(summary = "Получение задачи по id",
            description = "Возвращает задачу по указанному id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение задачи",
                    content = @Content(schema = @Schema(implementation = TaskGetDto.class))),
            @ApiResponse(responseCode = "403", description = "Ошибка аутентификации",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Задача не найдена",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public TaskGetDto getTask(
            @PathVariable
            @Parameter(description = "Идентификатор задачи", required = true)
            Long id
    ){
        return taskService.getTask(id);
    }

    @Operation(summary = "Получение всех задач",
            description = "Возвращает все задачи по заданным параметрам."
                + " Позволяет найти задачи с указанным автором, исполнителем или вернуть просто все задачи. "
                    + " При указании нескольких параметров приоритет будет у поиска по автору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение задач"),
            @ApiResponse(responseCode = "403", description = "Ошибка аутентификации",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public Page<TaskGetDto> getTasks(
            @Parameter(description = "Email автора задачи")
            @RequestParam(value = "author", required = false) String author,
            @Parameter(description = "Email исполнителя задачи")
            @RequestParam(value = "performer", required = false) String performer,
            @Parameter(description = "Номер страницы")
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @Parameter(description = "Размер страницы")
            @RequestParam(value = "size", defaultValue = "5") Integer size
    ){
        return taskService.handleGetRequest(author, performer, page, size);
    }

    @Operation(summary = "Добавление исполнителей",
            description = "Добавляет к задаче новых исполнителей."
                    + " Доступно только администраторам")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное добаление исполнителей",
                    content = @Content(schema = @Schema(implementation = TaskGetDto.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка добавления исполнителя",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "403", description = "Ошибка аутентификации",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Задача или исполнитель не найдены",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PatchMapping("/{id}/performers")
    @PreAuthorize("hasRole('ADMIN')")
    public TaskGetDto addPerformer(
            @PathVariable
            @Parameter(description = "Идентификатор задачи", required = true)
            Long id,
            @RequestBody
            @Validated List<@Email String> performers
    ){
        return performerService.addPerformers(id, performers);
    }

    @Operation(summary = "Удаление исполнителя",
            description = "Удаляет у задачи исполнителя."
                    + " Доступно только администраторам")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное удаление исполнителя",
                    content = @Content(schema = @Schema(implementation = TaskGetDto.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка добавления исполнителя",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "403", description = "Ошибка аутентификации",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Задача или исполнитель не найдены",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @DeleteMapping("/{id}/performers")
    @PreAuthorize("hasRole('ADMIN')")
    public TaskGetDto deletePerformer(
            @PathVariable
            @Parameter(description = "Идентификатор задачи", required = true)
            Long id,
            @RequestParam("email")
            @Parameter(description = "Email исполнителя", required = true)
            String performer
    ){
        return performerService.deletePerformer(id, performer);
    }

    @Operation(summary = "Изменение приоритета задачи",
            description = "Изменяет приоритет у заданной задачи."
                    + " Доступно только администраторам")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное изменение приоритета",
                    content = @Content(schema = @Schema(implementation = TaskGetDto.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка изменения приоритета",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "403", description = "Ошибка аутентификации",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Задача или приоритет не найдены",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PatchMapping("/{id}/priorities")
    @PreAuthorize("hasRole('ADMIN')")
    public TaskGetDto updatePriority(
            @PathVariable
            @Parameter(description = "Идентификатор задачи", required = true)
            Long id,
            @RequestBody
            @Validated PriorityDto priority
    ){
        return taskService.updateTaskPriority(id, priority);
    }

    @Operation(summary = "Изменение статуса задачи",
            description = "Изменяет статус у заданной задачи."
                    + " Доступно только администраторам или исполнителям данной задачи")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное изменение статуса",
                    content = @Content(schema = @Schema(implementation = TaskGetDto.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка изменения статуса",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "403", description = "Ошибка аутентификации",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Задача или статус не найдены",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public TaskGetDto updateStatus(
            @PathVariable
            @Parameter(description = "Идентификатор задачи", required = true)
            Long id,
            @RequestBody
            @Validated StatusDto status,
            Authentication authentication
    ){
        return taskService.updateTaskStatus(id, status, authentication);
    }

    @Operation(summary = "Удаление задачи",
            description = "Удаляет указаную задачу."
                    + " Доступно только администраторам")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Успешное удаление задачи",
                    content = @Content(schema = @Schema(implementation = TaskGetDto.class))),
            @ApiResponse(responseCode = "403", description = "Ошибка аутентификации",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Задача не найдена",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable
            @Parameter(description = "Идентификатор задачи", required = true)
            Long id
    ){
        taskService.delete(id);
    }

}
