package com.hottabych04.app.configuration.jwt.configurer;

import com.hottabych04.app.database.repository.UserRepository;
import com.hottabych04.app.database.repository.DeactivatedTokenRepository;
import com.hottabych04.app.service.security.jwt.detail.TokenAuthenticationUserDetailsService;
import com.hottabych04.app.service.security.jwt.converter.JwtAuthenticationConverter;
import com.hottabych04.app.service.security.jwt.serializer.AccessTokenJwsDeserializer;
import com.hottabych04.app.service.security.jwt.serializer.RefreshTokenJweDeserializer;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationConfigurer extends AbstractHttpConfigurer<JwtAuthenticationConfigurer, HttpSecurity> {

    private final AccessTokenJwsDeserializer accessTokenJwsDeserializer;
    private final RefreshTokenJweDeserializer refreshTokenJweDeserializer;

    private final UserRepository userRepository;
    private final DeactivatedTokenRepository deactivatedTokenRepository;

    @Override
    public void init(HttpSecurity builder) throws Exception {
        var csrfConfigurer = builder.getConfigurer(CsrfConfigurer.class);
        if (csrfConfigurer != null) {
            csrfConfigurer.ignoringRequestMatchers(new AntPathRequestMatcher("/**/auth/login", HttpMethod.POST.name()));
        }
    }

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        var authenticationProvider = new PreAuthenticatedAuthenticationProvider();
        authenticationProvider.setPreAuthenticatedUserDetailsService(
                new TokenAuthenticationUserDetailsService(this.deactivatedTokenRepository, this.userRepository)
        );

        var jwtAuthenticationFilter = new AuthenticationFilter(
                new ProviderManager(authenticationProvider),
                new JwtAuthenticationConverter(this.accessTokenJwsDeserializer, this.refreshTokenJweDeserializer)
        );

        jwtAuthenticationFilter.setSuccessHandler((request, response, authentication) -> CsrfFilter.skipRequest(request));
        jwtAuthenticationFilter.setFailureHandler((request, response, authentication) -> response.sendError(HttpServletResponse.SC_FORBIDDEN));

        builder.addFilterBefore(jwtAuthenticationFilter, CsrfFilter.class);
    }
}
