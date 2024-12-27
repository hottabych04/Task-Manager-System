package com.hottabych04.app.configuration.jwt;

import com.hottabych04.app.service.jwt.entity.Token;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Objects;
import java.util.function.Function;

public class JwtAuthenticationConfigurer extends AbstractHttpConfigurer<JwtAuthenticationConfigurer, HttpSecurity> {

    private Function<Token, String> accessTokenJwsSerializer = Objects::toString;

    private Function<Token, String> refreshTokenJweSerializer = Objects::toString;

    @Override
    public void init(HttpSecurity builder) throws Exception {
        var csrfConfigurer = builder.getConfigurer(CsrfConfigurer.class);
        if (csrfConfigurer != null) {
            csrfConfigurer.ignoringRequestMatchers(new AntPathRequestMatcher("/jwt/tokens", HttpMethod.POST.name()));
        }
    }

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        var filter = new RequestJwtTokensFilter();
        filter.setAccessTokenJwsSerializer(this.accessTokenJwsSerializer);
        filter.setRefreshTokenJweSerializer(this.refreshTokenJweSerializer);

        builder.addFilterAfter(filter, ExceptionTranslationFilter.class);
    }

    public JwtAuthenticationConfigurer accessTokenJwsSerializer(Function<Token, String> accessTokenJwsSerializer) {
        this.accessTokenJwsSerializer = accessTokenJwsSerializer;
        return this;
    }

    public JwtAuthenticationConfigurer refreshTokenJweSerializer(Function<Token, String> refreshTokenJweSerializer) {
        this.refreshTokenJweSerializer = refreshTokenJweSerializer;
        return this;
    }
}
