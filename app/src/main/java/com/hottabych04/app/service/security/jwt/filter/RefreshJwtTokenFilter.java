package com.hottabych04.app.service.security.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hottabych04.app.service.security.jwt.entity.RefreshedAccessToken;
import com.hottabych04.app.service.security.jwt.entity.Token;
import com.hottabych04.app.service.security.jwt.entity.TokenUser;
import com.hottabych04.app.service.security.jwt.factory.AccessTokenFactory;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Function;

@Setter
public class RefreshJwtTokenFilter extends OncePerRequestFilter {

    private RequestMatcher requestMatcher = new AntPathRequestMatcher("/jwt/refresh");

    private SecurityContextRepository securityContextRepository = new RequestAttributeSecurityContextRepository();

    private Function<Token, Token> accessTokenFactory = new AccessTokenFactory();

    private Function<Token, String> accessTokenSerializer = Objects::toString;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (this.requestMatcher.matches(request)){
            if (this.securityContextRepository.containsContext(request)){
                var context = this.securityContextRepository.loadDeferredContext(request).get();

                if (context != null &&
                        context.getAuthentication() instanceof PreAuthenticatedAuthenticationToken &&
                        context.getAuthentication().getPrincipal() instanceof TokenUser user &&
                        context.getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("JWT_REFRESH"))
                ) {
                    var accessToken = this.accessTokenFactory.apply(user.getToken());

                    response.setStatus(HttpServletResponse.SC_OK);
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

                    var refreshedAccessToken = new RefreshedAccessToken(
                            this.accessTokenSerializer.apply(accessToken),
                            accessToken.expiredAt().toString()
                    );

                    this.objectMapper.writeValue(response.getWriter(), refreshedAccessToken);

                    return;
                }

                throw new AccessDeniedException("User must be authenticated with JWT");
            }
        }

        filterChain.doFilter(request, response);
    }
}
