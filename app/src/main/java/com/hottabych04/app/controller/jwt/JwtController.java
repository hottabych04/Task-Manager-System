package com.hottabych04.app.controller.jwt;

import com.hottabych04.app.controller.jwt.payload.LoginRequest;
import com.hottabych04.app.service.security.jwt.JwtService;
import com.hottabych04.app.service.security.jwt.entity.RefreshedAccessToken;
import com.hottabych04.app.service.security.jwt.entity.Tokens;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class JwtController {

    private final JwtService jwtService;

    @PostMapping("/login")
    @PreAuthorize("isAnonymous()")
    public Tokens createTokens(@RequestBody @Validated LoginRequest request){
        return jwtService.createTokens(request);
    }

    @PostMapping("/token/refresh")
    @PreAuthorize("hasAuthority('JWT_REFRESH')")
    public RefreshedAccessToken refreshToken(Authentication authentication){
        return jwtService.refreshToken(authentication);
    }

    @PostMapping("/logout")
    @PreAuthorize("hasAuthority('JWT_LOGOUT')")
    public void logoutToken(Authentication authentication){
        jwtService.logoutToken(authentication);
    }
}
