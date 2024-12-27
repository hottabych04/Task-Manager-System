package com.hottabych04.app.configuration.jwt;

import com.hottabych04.app.service.jwt.serializer.AccessTokenJwsSerializer;
import com.hottabych04.app.service.jwt.serializer.RefreshTokenJweSerializer;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import java.text.ParseException;

@Configuration
public class JwtConfig {

    @Bean
    public JwtAuthenticationConfigurer jwtAuthenticationConfigurer(
            @Value("${jwt.access-token-key}") String accessTokenKey,
            @Value("${jwt.refresh-token-key}") String refreshTokenKey
    ) throws ParseException, JOSEException {
        return new JwtAuthenticationConfigurer()
                .accessTokenJwsSerializer(
                        new AccessTokenJwsSerializer(
                                new MACSigner(OctetSequenceKey.parse(accessTokenKey))
                        )
                )
                .refreshTokenJweSerializer(
                        new RefreshTokenJweSerializer(
                                new DirectEncrypter(OctetSequenceKey.parse(refreshTokenKey))
                        )
                );
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthenticationConfigurer jwtAuthenticationConfigurer
    ) throws Exception {
        jwtAuthenticationConfigurer.configure(http);

        return http
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(authorizeHttpRequests ->
                        authorizeHttpRequests.anyRequest().authenticated())
                .build();
    }

}
