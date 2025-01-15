package com.hottabych04.app.controller.user;

import com.hottabych04.app.controller.user.payload.UserCreateDto;
import com.hottabych04.app.controller.user.payload.UserGetDto;
import com.hottabych04.app.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping("/api/v1/users")
@Tag(name = "User controller", description = "API для работы с пользователями")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Регистрация нового пользователя",
            description = "Создает нового пользователя но не аутентифицирует его."
                    + " Доступно только не аутентифицированным пользователям")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешная регистрация пользователя",
                    content = @Content(schema = @Schema(implementation = UserGetDto.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка создания пользователя",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "403", description = "Ошибка аутентификации",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping
    @PreAuthorize("isAnonymous()")
    public UserGetDto createUser(@RequestBody @Validated UserCreateDto user){
        return userService.createUser(user);
    }

    @Operation(summary = "Получение пользователя по id",
            description = "Возвращает информацию о новом пользователе по его id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение пользователя",
                    content = @Content(schema = @Schema(implementation = UserGetDto.class))),
            @ApiResponse(responseCode = "403", description = "Ошибка аутентификации",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public UserGetDto getUser(
            @PathVariable
            @Parameter(description = "Идентификатор пользователя", required = true)
            Long id
    ){
        return userService.getUser(id);
    }

    @Operation(summary = "Получение пользователя по email",
            description = "Возвращает информацию о новом пользователе по его email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение пользователя",
                    content = @Content(schema = @Schema(implementation = UserGetDto.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка получения пользователя",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "403", description = "Ошибка аутентификации",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public UserGetDto getUser(
            @RequestParam
            @Parameter(description = "Email пользователя", required = true)
            String email
    ){
        return userService.getUser(email);
    }

    @Operation(summary = "Получение всех пользователей",
            description = "Возвращает информацию о всех пользователях")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение пользователей"),
            @ApiResponse(responseCode = "400", description = "Ошибка получения пользователей",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "403", description = "Ошибка аутентификации",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public Page<UserGetDto> getUsers(
            @Parameter(description = "Номер страницы")
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @Parameter(description = "Размер страницы")
            @RequestParam(value = "size", defaultValue = "5") Integer size
    ){
        return userService.getUsers(page, size);
    }

    @Operation(summary = "Добавление роли Admin для пользователя",
            description = "Добавляет указанному пользователю роль Admin."
                    + " Доступно только администраторам")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное добавление роли",
                    content = @Content(schema = @Schema(implementation = UserGetDto.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка добавления роли",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "403", description = "Ошибка аутентификации",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Указанный пользователь не найден",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))

    })
    @PatchMapping("/{id}/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public UserGetDto addAdminRole(
            @PathVariable
            @Parameter(description = "Идентификатор пользователя", required = true)
            Long id
    ){
        return userService.updateAdminRole(id);
    }

    @Operation(summary = "Удаление роли Admin у пользователя",
            description = "Удаляет у указанного пользователя роль Admin."
                    + " Доступно только администраторам")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Успешное удаление роли",
                    content = @Content(schema = @Schema(implementation = UserGetDto.class))),
            @ApiResponse(responseCode = "403", description = "Ошибка аутентификации",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Указанный пользователь не найден",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))

    })
    @DeleteMapping("/{id}/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public UserGetDto deleteAdminRole(
            @PathVariable
            @Parameter(description = "Идентификатор пользователя", required = true)
            Long id
    ){
        return userService.deleteAdminRole(id);
    }

    @Operation(summary = "Удаление текущего пользователя",
            description = "Удаляет пользователя который вызывает данный метод")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Успешное удаление пользователя"),
            @ApiResponse(responseCode = "403", description = "Ошибка аутентификации",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))

    })
    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(Authentication authentication){
        userService.deleteUser(authentication);
    }

    @Operation(summary = "Удаление указанного пользователя",
            description = "Удаляет пользователя указанного по id."
                    + " Доступно только администраторам")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Успешное удаление пользователя"),
            @ApiResponse(responseCode = "403", description = "Ошибка аутентификации",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Указанный пользователь не найден",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))

    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(
            @PathVariable
            @Parameter(description = "Идентификатор пользователя", required = true)
            Long id
    ){
        userService.deleteUser(id);
    }
}
