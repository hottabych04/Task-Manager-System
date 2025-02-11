package com.hottabych04.app.service.security.jwt.serializer;

import com.hottabych04.app.service.security.jwt.entity.Token;
import com.hottabych04.app.IntegrationTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class AccessTokenJwsSerializerTest extends IntegrationTestBase {

    @Autowired
    private AccessTokenJwsSerializer accessTokenJwsSerializer;

    @Test
    @DisplayName("Success serialize access token to JWT")
    public void successSerializeAccessToken(){
        Token accessToken = new Token(
                UUID.randomUUID(),
                "login@example.com",
                List.of(
                        "ROLE_ADMIN"
                ),
                Instant.now(),
                Instant.now().plus(Duration.ofMinutes(1L))
        );

        String serializedAccessToken = accessTokenJwsSerializer.apply(accessToken);
        assertThat(serializedAccessToken).isNotNull().isNotEmpty();

        String[] split = serializedAccessToken.split("\\.");
        assertThat(split.length).isEqualTo(3);
    }
}
