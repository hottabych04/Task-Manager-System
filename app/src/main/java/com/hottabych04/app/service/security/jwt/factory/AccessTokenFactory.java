package com.hottabych04.app.service.security.jwt.factory;

import com.hottabych04.app.service.security.jwt.entity.Token;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Function;

@RequiredArgsConstructor
public class AccessTokenFactory implements Function<Token, Token> {

    private final Duration tokenTtl;

    @Override
    public Token apply(Token token) {

        var authorities = token.authorities()
                .stream()
                .filter(it -> it.startsWith("GRAND_"))
                .map(it -> it.replace("GRAND_", ""))
                .toList();

        var now = Instant.now();

        return new Token(
                token.id(),
                token.login(),
                authorities,
                now,
                now.plus(tokenTtl)
        );
    }
}
