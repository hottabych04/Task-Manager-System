package com.hottabych04.example.service.security.jwt.factory;

import com.hottabych04.app.service.security.jwt.entity.Token;
import com.hottabych04.app.service.security.jwt.factory.RefreshTokenFactory;
import com.hottabych04.example.IntegrationTestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class RefreshTokenFactoryTest extends IntegrationTestBase {

    @Autowired
    private RefreshTokenFactory refreshTokenFactory;

    @Test
    @DisplayName("Success refresh token create from authentication")
    public void successCreateRefreshToken(){
        Authentication authentication = new PreAuthenticatedAuthenticationToken(
                "admin@gmail.com",
                null,
                List.of(
                        new SimpleGrantedAuthority("ROLE_ADMIN"),
                        new SimpleGrantedAuthority("ROLE_USER")
                )
        );

        Token token = refreshTokenFactory.apply(authentication);

        Assertions.assertAll(() -> {
                    assertThat(token).isNotNull();
                    assertThat(token.id()).isNotNull();
                    assertThat(token.authorities()).isNotNull().isNotEmpty();
                    assertThat(token.login()).isNotNull().isNotEmpty();
                    assertThat(token.createdAt()).isNotNull();
                    assertThat(token.expiredAt()).isNotNull();
        });
    }

}
