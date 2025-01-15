package com.hottabych04.app.controller.jwt;

import com.hottabych04.app.controller.jwt.payload.LoginRequest;
import com.hottabych04.app.controller.task.payload.TaskGetDto;
import com.hottabych04.app.service.security.jwt.JwtService;
import com.hottabych04.app.service.security.jwt.entity.RefreshedAccessToken;
import com.hottabych04.app.service.security.jwt.entity.Tokens;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "JWT")
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication controller", description = "API для аутентификации и работы с JWT")
public class JwtController {

    private final JwtService jwtService;

    @Operation(summary = "Аутентификация",
            description = "Прохождение аутентификации и получение access и refresh токенов."
                    + " Доступно только не аутентифицированным пользователям")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешная аутентификация",
                    content = @Content(schema = @Schema(implementation = Tokens.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка создания токенов",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "403", description = "Ошибка аутентификации",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping("/login")
    @PreAuthorize("isAnonymous()")
    public Tokens createTokens(@RequestBody @Validated LoginRequest request){
        return jwtService.createTokens(request);
    }

    @Operation(summary = "Обновление access токена",
            description = "Получение нового access токена."
                    + " Доступно только пользователям аутентифицированными при помощи refresh токена")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное обновление токена",
                    content = @Content(schema = @Schema(implementation = RefreshedAccessToken.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка обновления токена",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "403", description = "Ошибка аутентификации",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping("/token/refresh")
    @PreAuthorize("hasAuthority('JWT_REFRESH')")
    public RefreshedAccessToken refreshToken(Authentication authentication){
        return jwtService.refreshToken(authentication);
    }

    @Operation(summary = "Выход из системы",
            description = "Блокирует все токены полученные в рамках текущей аутентификации."
                    + " Доступно только пользователям аутентифицированными при помощи refresh токена")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешная дактивация токена"),
            @ApiResponse(responseCode = "400", description = "Ошибка декактивации токена",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "403", description = "Ошибка аутентификации",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping("/logout")
    @PreAuthorize("hasAuthority('JWT_LOGOUT')")
    public void logoutToken(Authentication authentication){
        jwtService.logoutToken(authentication);
    }
}
