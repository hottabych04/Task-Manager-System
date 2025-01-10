package com.hottabych04.app.configuration.jwt;

import com.hottabych04.app.service.security.jwt.factory.AccessTokenFactory;
import com.hottabych04.app.service.security.jwt.factory.RefreshTokenFactory;
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
import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class JwtConfig {

    @Bean
    public AccessTokenJwsSerializer accessTokenJwsSerializer(
            @Value("${jwt.access-token-key}") String accessTokenKey
    ) throws ParseException, JOSEException {
        return new AccessTokenJwsSerializer(
                new MACSigner(OctetSequenceKey.parse(accessTokenKey))
        );
    }

    @Bean
    public AccessTokenJwsDeserializer accessTokenJwsDeserializer(
            @Value("${jwt.access-token-key}") String accessTokenKey
    ) throws ParseException, JOSEException {
        return new AccessTokenJwsDeserializer(
                new MACVerifier(OctetSequenceKey.parse(accessTokenKey))
        );
    }

    @Bean
    public RefreshTokenJweSerializer refreshTokenJweSerializer(
            @Value("${jwt.refresh-token-key}") String refreshTokenKey
    ) throws ParseException, JOSEException {
        return new RefreshTokenJweSerializer(
                new DirectEncrypter(OctetSequenceKey.parse(refreshTokenKey))
        );
    }

    @Bean
    public RefreshTokenJweDeserializer refreshTokenJweDeserializer(
            @Value("${jwt.refresh-token-key}") String refreshTokenKey
    ) throws ParseException, JOSEException {
        return new RefreshTokenJweDeserializer(
                new DirectDecrypter(OctetSequenceKey.parse(refreshTokenKey))
        );
    }

    @Bean
    public AccessTokenFactory accessTokenFactory(
            @Value("${jwt.access-token-duration}") Long accessTokenDuration
    ){
        return new AccessTokenFactory(
                Duration.ofMinutes(accessTokenDuration)
        );
    }

    @Bean
    public RefreshTokenFactory refreshTokenFactory(
            @Value("${jwt.refresh-token-duration}") Long refreshTokenDuration
    )
    {
        return new RefreshTokenFactory(
                Duration.ofMinutes(refreshTokenDuration)
        );
    }
}

