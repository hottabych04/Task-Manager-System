package com.hottabych04.app.configuration.jwt;

import com.hottabych04.app.configuration.jwt.configurer.JwtAuthenticationConfigurer;
import com.hottabych04.app.database.repository.DeactivatedTokenRepository;
import com.hottabych04.app.service.security.jwt.serializer.AccessTokenJwsDeserializer;
import com.hottabych04.app.service.security.jwt.serializer.AccessTokenJwsSerializer;
import com.hottabych04.app.service.security.jwt.serializer.RefreshTokenJweDeserializer;
import com.hottabych04.app.service.security.jwt.serializer.RefreshTokenJweSerializer;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.ParseException;

@Configuration
@RequiredArgsConstructor
public class JwtConfig {

    @Bean
    public JwtAuthenticationConfigurer jwtAuthenticationConfigurer(
            @Value("${jwt.access-token-key}") String accessTokenKey,
            @Value("${jwt.refresh-token-key}") String refreshTokenKey,
            DeactivatedTokenRepository deactivatedTokenRepository
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
                )
                .accessTokenDeserializer(
                        new AccessTokenJwsDeserializer(
                                new MACVerifier(OctetSequenceKey.parse(accessTokenKey))
                        )
                )
                .refreshTokenDeserializer(
                        new RefreshTokenJweDeserializer(
                                new DirectDecrypter(OctetSequenceKey.parse(refreshTokenKey))
                        )
                )
                .deactivatedTokenRepository(
                        deactivatedTokenRepository
                );
    }
}

