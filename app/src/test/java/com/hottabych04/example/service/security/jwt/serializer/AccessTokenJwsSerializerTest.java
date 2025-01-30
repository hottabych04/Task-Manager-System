package com.hottabych04.example.service.security.jwt.serializer;

import com.hottabych04.app.Application;
import com.hottabych04.app.service.security.jwt.entity.Token;
import com.hottabych04.app.service.security.jwt.serializer.AccessTokenJwsSerializer;
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
public class AccessTokenJwsSerializerTest {

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
