package com.hottabych04.app.service.security.jwt.converter;

import com.hottabych04.app.service.security.jwt.entity.Token;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.function.Function;

@RequiredArgsConstructor
public class JwtAuthenticationConverter implements AuthenticationConverter {

    private final Function<String, Token> accessTokenDeserializer;

    private final Function<String, Token> refreshTokenDeserializer;

    @Override
    public Authentication convert(HttpServletRequest request) {
        var authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization != null && authorization.startsWith("Bearer ")){
            var token = authorization.replace("Bearer ", "");

            var accessToken = this.accessTokenDeserializer.apply(token);
            if (accessToken != null) {
                return new PreAuthenticatedAuthenticationToken(accessToken, token);
            }

            var refreshToken = this.refreshTokenDeserializer.apply(token);
            if (refreshToken != null) {
                return new PreAuthenticatedAuthenticationToken(refreshToken, token);
            }
        }

        return null;
    }
}
