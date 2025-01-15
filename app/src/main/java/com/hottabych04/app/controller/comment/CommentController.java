package com.hottabych04.app.controller.comment;

import com.hottabych04.app.controller.comment.payload.CommentCreateDto;
import com.hottabych04.app.controller.comment.payload.CommentGetDto;
import com.hottabych04.app.service.comment.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "JWT")
@RequestMapping("/api/v1/comments")
@Tag(name = "Comment controller", description = "API для работы с комментариями")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "Создание комментария",
            description = "Создает новый комментарий."
                    + " Доступно только администраторам либо исполнителям данной задачи")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное создание комментария",
                    content = @Content(schema = @Schema(implementation = CommentGetDto.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка создания комментария",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "403", description = "Ошибка аутентификации",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Задача не найдена",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public CommentGetDto create(
            @RequestBody @Validated CommentCreateDto comment,
            Authentication authentication
    ){
        return commentService.create(comment, authentication);
    }

    @Operation(summary = "Получение комментария",
            description = "Возвращает комментарий по его id."
                    + " Доступно только администраторам либо исполнителям данной задачи")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение комментария",
                    content = @Content(schema = @Schema(implementation = CommentGetDto.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка получения комментария",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "403", description = "Ошибка аутентификации",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Задача или комментарий не найдена",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public CommentGetDto getComment(
            @PathVariable Long id,
            Authentication authentication
    ){
        return commentService.getComment(id, authentication);
    }

    @Operation(summary = "Получение комментариев задачи",
            description = "Возвращает все комментарии по id задачи."
                    + " Доступно только администраторам либо исполнителям данной задачи")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение комментариев",
                    content = @Content(schema = @Schema(implementation = CommentGetDto.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка получения комментариев",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "403", description = "Ошибка аутентификации",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Задача не найдена",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping("/task/{id}")
    public Page<CommentGetDto> getComments(
            @PathVariable Long id,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "5") Integer size,
            Authentication authentication
    ){
        return commentService.getComments(id,page, size, authentication);
    }

    @Operation(summary = "Удаляет комментарий",
            description = "Удаляет комментарий по его id."
                    + " Доступно только администраторам либо авторам данного комментария")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Успешное удаление комментария",
                    content = @Content(schema = @Schema(implementation = CommentGetDto.class))),
            @ApiResponse(responseCode = "403", description = "Ошибка аутентификации",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Комментарий не найден",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long id,
            Authentication authentication
    ){
        commentService.delete(id, authentication);
    }
}
