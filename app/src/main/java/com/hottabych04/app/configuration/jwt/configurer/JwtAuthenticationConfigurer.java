package com.hottabych04.app.configuration.jwt.configurer;

import com.hottabych04.app.service.security.jwt.filter.JwtLogoutFilter;
import com.hottabych04.app.service.security.jwt.filter.RefreshJwtTokenFilter;
import com.hottabych04.app.service.security.jwt.filter.RequestJwtTokensFilter;
import com.hottabych04.app.database.repository.DeactivatedTokenRepository;
import com.hottabych04.app.service.security.jwt.TokenAuthenticationUserDetailsService;
import com.hottabych04.app.service.security.jwt.converter.JwtAuthenticationConverter;
import com.hottabych04.app.service.security.jwt.entity.Token;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Objects;
import java.util.function.Function;

public class JwtAuthenticationConfigurer extends AbstractHttpConfigurer<JwtAuthenticationConfigurer, HttpSecurity> {

    private Function<Token, String> accessTokenJwsSerializer = Objects::toString;

    private Function<Token, String> refreshTokenJweSerializer = Objects::toString;

    private Function<String, Token> accessTokenDeserializer;

    private Function<String, Token> refreshTokenDeserializer;

    private DeactivatedTokenRepository deactivatedTokenRepository;

    @Override
    public void init(HttpSecurity builder) throws Exception {
        var csrfConfigurer = builder.getConfigurer(CsrfConfigurer.class);
        if (csrfConfigurer != null) {
            csrfConfigurer.ignoringRequestMatchers(new AntPathRequestMatcher("/jwt/tokens", HttpMethod.POST.name()));
        }
    }

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        var requestJwtTokensFilter = new RequestJwtTokensFilter();
        requestJwtTokensFilter.setAccessTokenJwsSerializer(this.accessTokenJwsSerializer);
        requestJwtTokensFilter.setRefreshTokenJweSerializer(this.refreshTokenJweSerializer);

        var authenticationProvider = new PreAuthenticatedAuthenticationProvider();
        authenticationProvider.setPreAuthenticatedUserDetailsService(new TokenAuthenticationUserDetailsService(deactivatedTokenRepository));

        var jwtAuthenticationFilter = new AuthenticationFilter(
                new ProviderManager(authenticationProvider),
                new JwtAuthenticationConverter(this.accessTokenDeserializer, this.refreshTokenDeserializer)
        );

        jwtAuthenticationFilter.setSuccessHandler((request, response, authentication) -> CsrfFilter.skipRequest(request));
        jwtAuthenticationFilter.setFailureHandler((request, response, authentication) -> response.sendError(HttpServletResponse.SC_FORBIDDEN));

        var refreshJwtTokenFilter = new RefreshJwtTokenFilter();
        refreshJwtTokenFilter.setAccessTokenSerializer(this.accessTokenJwsSerializer);

        var jwtLogoutFilter = new JwtLogoutFilter(this.deactivatedTokenRepository);

        builder
                .addFilterAfter(requestJwtTokensFilter, ExceptionTranslationFilter.class)
                .addFilterAfter(refreshJwtTokenFilter, ExceptionTranslationFilter.class)
                .addFilterAfter(jwtLogoutFilter, ExceptionTranslationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter, CsrfFilter.class);
    }

    public JwtAuthenticationConfigurer accessTokenJwsSerializer(Function<Token, String> accessTokenJwsSerializer) {
        this.accessTokenJwsSerializer = accessTokenJwsSerializer;
        return this;
    }

    public JwtAuthenticationConfigurer refreshTokenJweSerializer(Function<Token, String> refreshTokenJweSerializer) {
        this.refreshTokenJweSerializer = refreshTokenJweSerializer;
        return this;
    }

    public JwtAuthenticationConfigurer accessTokenDeserializer(Function<String, Token> accessTokenDeserializer) {
        this.accessTokenDeserializer = accessTokenDeserializer;
        return this;
    }

    public JwtAuthenticationConfigurer refreshTokenDeserializer(Function<String, Token> refreshTokenDeserializer) {
        this.refreshTokenDeserializer = refreshTokenDeserializer;
        return this;
    }

    public JwtAuthenticationConfigurer deactivatedTokenRepository(DeactivatedTokenRepository deactivatedTokenRepository) {
        this.deactivatedTokenRepository = deactivatedTokenRepository;
        return this;
    }
}
