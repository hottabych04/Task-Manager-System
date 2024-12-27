package com.hottabych04.app.service.jwt.serializer;

import com.hottabych04.app.service.jwt.entity.Token;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEDecrypter;
import com.nimbusds.jwt.EncryptedJWT;

import java.text.ParseException;
import java.util.UUID;
import java.util.function.Function;

public class RefreshTokenJweDeserializer implements Function<String, Token> {

    private JWEDecrypter jweDecrypter;

    @Override
    public Token apply(String s) {
        try {
            var encryptedJWT = EncryptedJWT.parse(s);
            encryptedJWT.decrypt(this.jweDecrypter);

            var jwtClaimsSet = encryptedJWT.getJWTClaimsSet();

            return new Token(
                    UUID.fromString(jwtClaimsSet.getJWTID()),
                    jwtClaimsSet.getSubject(),
                    jwtClaimsSet.getStringListClaim("authorities"),
                    jwtClaimsSet.getIssueTime().toInstant(),
                    jwtClaimsSet.getExpirationTime().toInstant()
            );
        } catch (ParseException | JOSEException e) {
            System.out.println("Error decrypt");
        }

        return null;
    }
}
