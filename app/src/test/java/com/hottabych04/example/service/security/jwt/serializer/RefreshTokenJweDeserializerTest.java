package com.hottabych04.example.service.security.jwt.serializer;

import com.hottabych04.app.service.security.jwt.entity.Token;
import com.hottabych04.app.service.security.jwt.serializer.RefreshTokenJweDeserializer;
import com.hottabych04.example.service.security.jwt.IntegrationTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class RefreshTokenJweDeserializerTest extends IntegrationTestBase {

    @Autowired
    private RefreshTokenJweDeserializer refreshTokenJweDeserializer;

    @Test
    @DisplayName("Success deserialize refresh token from JWT")
    public void successDeserializeRefreshToken(){
        Token refreshToken = new Token(
                UUID.fromString("4c189407-a43c-4725-bac0-89035114a508"),
                "login@example.com",
                List.of(
                        "JWT_REFRESH",
                        "JWT_LOGOUT",
                        "GRAND_ROLE_ADMIN"
                ),
                Instant.parse("2025-01-01T00:00:01.00Z"),
                Instant.parse("2025-01-01T00:00:01.00Z").plus(Duration.ofMinutes(1L))
        );

        String serializedRefreshToken = "eyJlbmMiOiJBMTkyR0NNIiwiYWxnIjoiZGlyIn0..5v3eAVVyF9myzkEu.ON5YcuvIR8efvAaMo9ST" +
                "vp-_T5hjde2FdVwYVp2IUU_lrMk7jYH-fzkxN6CSLksUEcCktkhj2HOXWgiBRGVQbB4Lj1tnZ1G0wbPqKRtEguc9nJV-JC6AIFC0Rl" +
                "x5rMBqP7JqNYwsVwhrJ_S3GzmsFVces9jyseF6_pytpyIeR6nYfnrNbZNMXJVX48TVvKI7r5CuNFQ60ycPOAEBtulEGi69BMOskpmJ" +
                ".rWjAFXnYgah3m-RWGXsnAg";

        Token deserializedRefreshToken = refreshTokenJweDeserializer.apply(serializedRefreshToken);

        assertThat(deserializedRefreshToken).isNotNull().isEqualTo(refreshToken);
    }
}
