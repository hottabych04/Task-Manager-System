package com.hottabych04.app.service.security.jwt.serializer;

import com.hottabych04.app.service.security.jwt.entity.Token;
import com.nimbusds.jose.*;
import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.JWTClaimsSet;

import java.util.Date;
import java.util.function.Function;

public class RefreshTokenJweSerializer implements Function<Token, String> {

    private JWEEncrypter jweEncrypter;
    private JWEAlgorithm jweAlgorithm = JWEAlgorithm.DIR;
    private EncryptionMethod encryptionMethod = EncryptionMethod.A192GCM;

    public RefreshTokenJweSerializer(JWEEncrypter jweEncrypter) {
        this.jweEncrypter = jweEncrypter;
    }

    @Override
    public String apply(Token token) {
        JWEHeader header = new JWEHeader(this.jweAlgorithm, this.encryptionMethod);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .jwtID(token.id().toString())
                .subject(token.login())
                .issueTime(Date.from(token.createdAt()))
                .expirationTime(Date.from(token.expiredAt()))
                .claim("authorities", token.authorities())
                .build();

        EncryptedJWT encryptedJWT = new EncryptedJWT(header, claimsSet);

        try {
            encryptedJWT.encrypt(this.jweEncrypter);

            return encryptedJWT.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }
}
