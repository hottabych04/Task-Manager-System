package com.hottabych04.app.service.security.jwt.serializer;

import com.hottabych04.app.service.security.jwt.entity.Token;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.Setter;

import java.util.Date;
import java.util.function.Function;

public class AccessTokenJwsSerializer implements Function<Token, String> {

    private JWSSigner jwsSigner;

    @Setter
    private JWSAlgorithm jwsAlgorithm = JWSAlgorithm.HS256;

    public AccessTokenJwsSerializer(JWSSigner jwsSigner) {
        this.jwsSigner = jwsSigner;
    }

    public AccessTokenJwsSerializer(JWSAlgorithm jwsAlgorithm, JWSSigner jwsSigner) {
        this.jwsAlgorithm = jwsAlgorithm;
        this.jwsSigner = jwsSigner;
    }

    @Override
    public String apply(Token token) {

        JWSHeader header = new JWSHeader(this.jwsAlgorithm);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .jwtID(token.id().toString())
                .subject(token.login())
                .issueTime(Date.from(token.createdAt()))
                .expirationTime(Date.from(token.expiredAt()))
                .claim("authorities", token.authorities())
                .build();

        SignedJWT signedJWT = new SignedJWT(header, claimsSet);

        try {
            signedJWT.sign(this.jwsSigner);

            return signedJWT.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }

    }
}
