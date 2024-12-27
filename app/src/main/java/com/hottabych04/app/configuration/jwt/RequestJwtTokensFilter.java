package com.hottabych04.app.configuration.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hottabych04.app.service.jwt.entity.Token;
import com.hottabych04.app.service.jwt.entity.Tokens;
import com.hottabych04.app.service.jwt.factory.AccessTokenFactory;
import com.hottabych04.app.service.jwt.factory.RefreshTokenFactory;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.Objects;
import java.util.function.Function;

@Setter
public class RequestJwtTokensFilter extends OncePerRequestFilter {

    private RequestMatcher requestMatcher = new AntPathRequestMatcher("/jwt/tokens", HttpMethod.POST.name());

    private Function<Token, Token> accessTokenFactory = new AccessTokenFactory();
    private Function<Authentication, Token> refreshTokenFactory = new RefreshTokenFactory();
    private Function<Token, String> accessTokenJwsSerializer = Objects::toString;
    private Function<Token, String> refreshTokenJweSerializer = Objects::toString;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (this.requestMatcher.matches(request)){
            SecurityContext context = SecurityContextHolder.getContext();
            if (context != null && (context.getAuthentication() instanceof PreAuthenticatedAuthenticationToken)){
                Token refreshToken = refreshTokenFactory.apply(context.getAuthentication());
                Token accessToken = accessTokenFactory.apply(refreshToken);

                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);

                Tokens tokens = new Tokens(
                        this.accessTokenJwsSerializer.apply(accessToken),
                        accessToken.expiredAt().toString(),
                        this.refreshTokenJweSerializer.apply(accessToken),
                        refreshToken.expiredAt().toString()
                );

                this.objectMapper.writeValue(
                        response.getWriter(),
                        tokens
                );
            }

            throw new AccessDeniedException("User must be authenticated");
        }

        filterChain.doFilter(request, response);
    }
}
