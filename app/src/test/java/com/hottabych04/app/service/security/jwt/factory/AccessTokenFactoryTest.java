package com.hottabych04.app.service.security.jwt.factory;

import com.hottabych04.app.service.security.jwt.entity.Token;
import com.hottabych04.app.IntegrationTestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class AccessTokenFactoryTest {

    private Duration tokenTtl = Duration.ofMinutes(10L);

    private AccessTokenFactory accessTokenFactory = new AccessTokenFactory(tokenTtl);

    @Test
    @DisplayName("Success access token create from refresh token")
    public void successCreateAccessToken(){
        Token refreshToken = new Token(
                UUID.randomUUID(),
                "login@example.com",
                List.of(
                        "JWT_REFRESH",
                        "JWT_LOGOUT",
                        "GRAND_ROLE_ADMIN"
                ),
                Instant.now(),
                Instant.now().plus(Duration.ofDays(1L))
        );

        Token accessToken = accessTokenFactory.apply(refreshToken);

        Assertions.assertAll(() -> {
            assertThat(accessToken).isNotNull();
            assertThat(accessToken.id()).isNotNull().isEqualTo(refreshToken.id());
            assertThat(accessToken.authorities()).isNotNull().isNotEmpty().anyMatch(it -> it.equals("ROLE_ADMIN"));
            assertThat(accessToken.login()).isNotNull().isNotEmpty().isEqualTo(refreshToken.login());
            assertThat(accessToken.createdAt()).isNotNull();
            assertThat(accessToken.expiredAt()).isNotNull();
        });
    }
}
