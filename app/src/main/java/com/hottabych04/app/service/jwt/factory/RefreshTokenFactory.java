package com.hottabych04.app.service.jwt.factory;

import com.hottabych04.app.service.jwt.entity.Token;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.time.Duration;
import java.time.Instant;
import java.util.LinkedList;
import java.util.UUID;
import java.util.function.Function;

@Setter
public class RefreshTokenFactory implements Function<Authentication, Token> {

    private Duration tokentTtl = Duration.ofHours(6L);

    @Override
    public Token apply(Authentication authentication) {

        var authorities = new LinkedList<String>();
        authorities.add("JWT_REFRESH");
        authorities.add("JWT_LOGOUT");

        authorities.addAll(
                authentication.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .map(it -> "GRAND_" + it)
                        .toList()
        );

        var now = Instant.now();

        return new Token(
                UUID.randomUUID(),
                authentication.getName(),
                authorities,
                now,
                now.plus(tokentTtl)
        );
    }
}