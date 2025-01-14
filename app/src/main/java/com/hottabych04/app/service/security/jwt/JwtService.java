package com.hottabych04.app.service.security.jwt;

import com.hottabych04.app.controller.jwt.payload.LoginRequest;
import com.hottabych04.app.database.entity.DeactivatedToken;
import com.hottabych04.app.database.repository.DeactivatedTokenRepository;
import com.hottabych04.app.exception.token.RefreshTokenException;
import com.hottabych04.app.service.security.jwt.entity.RefreshedAccessToken;
import com.hottabych04.app.service.security.jwt.entity.Token;
import com.hottabych04.app.service.security.jwt.entity.TokenUser;
import com.hottabych04.app.service.security.jwt.entity.Tokens;
import com.hottabych04.app.service.security.jwt.factory.AccessTokenFactory;
import com.hottabych04.app.service.security.jwt.factory.RefreshTokenFactory;
import com.hottabych04.app.service.security.jwt.serializer.AccessTokenJwsSerializer;
import com.hottabych04.app.service.security.jwt.serializer.RefreshTokenJweSerializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.ZoneId;

@Service
@Log4j2
@RequiredArgsConstructor
public class JwtService {

    private final AuthenticationManager authenticationManager;

    private final AccessTokenJwsSerializer accessTokenJwsSerializer;
    private final RefreshTokenJweSerializer refreshTokenJweSerializer;

    private final AccessTokenFactory accessTokenFactory;
    private final RefreshTokenFactory refreshTokenFactory;

    private final DeactivatedTokenRepository deactivatedTokenRepository;

    public Tokens createTokens(LoginRequest request){

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        Token refreshToken = refreshTokenFactory.apply(authentication);
        Token accessToken = accessTokenFactory.apply(refreshToken);

        var tokens = new Tokens(
                this.accessTokenJwsSerializer.apply(accessToken),
                accessToken.expiredAt().toString(),
                this.refreshTokenJweSerializer.apply(refreshToken),
                refreshToken.expiredAt().toString()
        );

        return tokens;
    }

    public RefreshedAccessToken refreshToken(Authentication authentication){
        if (authentication instanceof PreAuthenticatedAuthenticationToken &&
                authentication.getPrincipal() instanceof TokenUser user
        ) {
            var accessToken = this.accessTokenFactory.apply(user.getToken());

            var refreshedAccessToken = new RefreshedAccessToken(
                    accessTokenJwsSerializer.apply(accessToken),
                    accessToken.expiredAt().toString()
            );

            return refreshedAccessToken;
        }

        throw new RefreshTokenException();
    }

    public void logoutToken(Authentication authentication) {
        if (authentication instanceof PreAuthenticatedAuthenticationToken &&
                authentication.getPrincipal() instanceof TokenUser user
        ) {

            var token = user.getToken();

            DeactivatedToken deactivatedToken = new DeactivatedToken(
                    token.id(),
                    token.expiredAt().atZone(ZoneId.systemDefault()).toLocalDateTime()
            );

            deactivatedTokenRepository.save(deactivatedToken);

            return;
        }

        throw new RefreshTokenException();
    }
}
