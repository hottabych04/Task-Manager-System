package com.hottabych04.example.service.security.jwt.serializer;

import com.hottabych04.app.service.security.jwt.entity.Token;
import com.hottabych04.app.service.security.jwt.serializer.RefreshTokenJweSerializer;
import com.hottabych04.example.service.security.jwt.IntegrationTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class RefreshTokenJweSerializerTest extends IntegrationTestBase {

    @Autowired
    private RefreshTokenJweSerializer refreshTokenJweSerializer;

    @Test
    @DisplayName("Success serialize refresh token to JWT")
    public void successSerializeRefreshToken(){
        Token refreshToken = new Token(
                UUID.randomUUID(),
                "login@example.com",
                List.of(
                        "JWT_REFRESH",
                        "JWT_LOGOUT",
                        "GRAND_ROLE_ADMIN"
                ),
                Instant.now(),
                Instant.now().plus(Duration.ofHours(6L))
        );

        String serializedRefreshToken = refreshTokenJweSerializer.apply(refreshToken);
        assertThat(serializedRefreshToken).isNotNull().isNotEmpty();
    }
}
