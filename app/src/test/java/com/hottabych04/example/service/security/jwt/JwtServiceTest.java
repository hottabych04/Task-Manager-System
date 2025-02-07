package com.hottabych04.example.service.security.jwt;

import com.hottabych04.app.controller.jwt.payload.LoginRequest;
import com.hottabych04.app.database.entity.DeactivatedToken;
import com.hottabych04.app.database.repository.DeactivatedTokenRepository;
import com.hottabych04.app.service.security.jwt.JwtService;
import com.hottabych04.app.service.security.jwt.entity.RefreshedAccessToken;
import com.hottabych04.app.service.security.jwt.entity.Token;
import com.hottabych04.app.service.security.jwt.entity.TokenUser;
import com.hottabych04.app.service.security.jwt.entity.Tokens;
import com.hottabych04.app.service.security.jwt.factory.AccessTokenFactory;
import com.hottabych04.app.service.security.jwt.factory.RefreshTokenFactory;
import com.hottabych04.app.service.security.jwt.serializer.AccessTokenJwsSerializer;
import com.hottabych04.app.service.security.jwt.serializer.RefreshTokenJweSerializer;
import com.hottabych04.example.IntegrationTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class JwtServiceTest extends IntegrationTestBase {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private AccessTokenFactory accessTokenFactory;
    @Mock
    private AccessTokenJwsSerializer accessTokenJwsSerializer;
    @Mock
    private RefreshTokenFactory refreshTokenFactory;
    @Mock
    private RefreshTokenJweSerializer refreshTokenJweSerializer;
    @Mock
    private DeactivatedTokenRepository deactivatedTokenRepository;

    @InjectMocks
    private JwtService jwtService;

    @Test
    @DisplayName("Success create jwt tokens")
    public void createJwtTokens(){
        LoginRequest loginRequest = new LoginRequest(
                "dummy@example.com",
                "dummyPassword"
        );

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                loginRequest.email(),
                loginRequest.password()
        );

        Token token = new Token(
                UUID.randomUUID(),
                loginRequest.email(),
                List.of("DUMMY"),
                Instant.now(),
                Instant.now()
        );

        String serializedToken = "dummyToken";

        Mockito.when(authenticationManager.authenticate(authentication)).thenReturn(authentication);
        Mockito.when(refreshTokenFactory.apply(authentication)).thenReturn(token);
        Mockito.when(accessTokenFactory.apply(token)).thenReturn(token);
        Mockito.when(refreshTokenJweSerializer.apply(token)).thenReturn(serializedToken);
        Mockito.when(accessTokenJwsSerializer.apply(token)).thenReturn(serializedToken);

        Tokens dummyTokens = new Tokens(
                serializedToken,
                token.expiredAt().toString(),
                serializedToken,
                token.expiredAt().toString()
        );

        Tokens tokens = jwtService.createTokens(loginRequest);
        assertThat(tokens).isEqualTo(dummyTokens);
    }

    @Test
    @DisplayName("Success refresh access token")
    public void refreshToken(){
        Token dummyToken = new Token(
                UUID.randomUUID(),
                "dummy@example.com",
                List.of("DUMMY"),
                Instant.now(),
                Instant.now()
        );

        var authentication = new PreAuthenticatedAuthenticationToken(
                new TokenUser(
                        "dummy@example.com",
                        "dummyPassword",
                        true,
                        true,
                        true,
                        true,
                        List.of(new SimpleGrantedAuthority("DUMMY")),
                        dummyToken
                ),
                null
        );

        String serializedToken = "dummyToken";

        Mockito.when(accessTokenFactory.apply(dummyToken)).thenReturn(dummyToken);
        Mockito.when(accessTokenJwsSerializer.apply(dummyToken)).thenReturn(serializedToken);

        var dummyRefreshedAccessToken = new RefreshedAccessToken(serializedToken, dummyToken.expiredAt().toString());

        var refreshedAccessToken = jwtService.refreshToken(authentication);
        assertThat(refreshedAccessToken).isEqualTo(dummyRefreshedAccessToken);
    }

    @Test
    @DisplayName("Success deactivated JWT token")
    public void logoutToken(){
        Token dummyToken = new Token(
                UUID.randomUUID(),
                "dummy@example.com",
                List.of("DUMMY"),
                Instant.now(),
                Instant.now()
        );

        var authentication = new PreAuthenticatedAuthenticationToken(
                new TokenUser(
                        "dummy@example.com",
                        "dummyPassword",
                        true,
                        true,
                        true,
                        true,
                        List.of(new SimpleGrantedAuthority("DUMMY")),
                        dummyToken
                ),
                null
        );

        DeactivatedToken deactivatedToken = new DeactivatedToken(
                dummyToken.id(),
                dummyToken.expiredAt().atZone(ZoneId.systemDefault()).toLocalDateTime()
        );

        Mockito.when(deactivatedTokenRepository.save(deactivatedToken)).thenReturn(deactivatedToken);

        jwtService.logoutToken(authentication);
    }
}
