package com.hottabych04.app.service.security.jwt.filter;

import com.hottabych04.app.database.entity.DeactivatedToken;
import com.hottabych04.app.database.repository.DeactivatedTokenRepository;
import com.hottabych04.app.service.security.jwt.entity.TokenUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.ZoneId;

@RequiredArgsConstructor
public class JwtLogoutFilter extends OncePerRequestFilter {

    private RequestMatcher requestMatcher = new AntPathRequestMatcher("/jwt/logout");

    private SecurityContextRepository securityContextRepository = new RequestAttributeSecurityContextRepository();

    private final DeactivatedTokenRepository deactivatedTokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (this.requestMatcher.matches(request)) {
            if (this.securityContextRepository.containsContext(request)) {
                var context = this.securityContextRepository.loadDeferredContext(request).get();

                if (context != null &&
                        context.getAuthentication() instanceof PreAuthenticatedAuthenticationToken &&
                        context.getAuthentication().getPrincipal() instanceof TokenUser user &&
                        context.getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("JWT_LOGOUT"))
                ) {

                    var token = user.getToken();

                    DeactivatedToken deactivatedToken = new DeactivatedToken(
                            token.id(),
                            token.expiredAt().atZone(ZoneId.systemDefault()).toLocalDateTime()
                    );

                    deactivatedTokenRepository.save(deactivatedToken);

                    return;
                }
            }

            throw new AccessDeniedException("User must be authenticated with JWT");
        }

        filterChain.doFilter(request, response);
    }
}
