package com.hottabych04.app.controller.jwt;

import com.hottabych04.app.service.security.jwt.JwtService;
import com.hottabych04.app.service.security.jwt.entity.RefreshedAccessToken;
import com.hottabych04.app.service.security.jwt.entity.Tokens;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/jwt")
public class JwtController {

    private final JwtService jwtService;

    @PostMapping("/tokens")
    public Tokens createTokens(Authentication authentication){
        return jwtService.createTokens(authentication);
    }

    @PostMapping("/refresh")
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
