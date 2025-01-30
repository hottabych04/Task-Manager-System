package com.hottabych04.example.service.security.jwt.serializer;

import com.hottabych04.app.Application;
import com.hottabych04.app.service.security.jwt.entity.Token;
import com.hottabych04.app.service.security.jwt.serializer.AccessTokenJwsDeserializer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = Application.class)
public class AccessTokenJwsDeserializerTest {

    @Autowired
    private AccessTokenJwsDeserializer accessTokenJwsDeserializer;

    @Test
    @DisplayName("Success deserialize access token from JWT")
    public void successDeserializeAccessToken(){
        Token accessToken = new Token(
                UUID.fromString("4c189407-a43c-4725-bac0-89035114a508"),
                "login@example.com",
                List.of(
                        "ROLE_ADMIN"
                ),
                Instant.parse("2025-01-01T00:00:01.00Z"),
                Instant.parse("2025-01-01T00:00:01.00Z").plus(Duration.ofMinutes(1L))
        );

        String serializedAccessToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsb2dpbkBleGFtcGxlLmNvbSIsImV4cCI6MTczNTY4OTY2MS" +
                "wiaWF0IjoxNzM1Njg5NjAxLCJqdGkiOiI0YzE4OTQwNy1hNDNjLTQ3MjUtYmFjMC04OTAzNTExNGE1MDgiLCJhdXRob3JpdGllcyI6" +
                "WyJST0xFX0FETUlOIl19.FOkJWqdfmVRIhEy3cJAkONOEAw6cLYKM6U9UEMcIh4U";

        Token deserializedAccessToken = accessTokenJwsDeserializer.apply(serializedAccessToken);

        assertThat(deserializedAccessToken).isNotNull().isEqualTo(accessToken);
    }
}
