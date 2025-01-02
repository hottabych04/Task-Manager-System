package com.hottabych04.app.service.security.jwt.serializer;

import com.hottabych04.app.service.security.jwt.entity.Token;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEDecrypter;
import com.nimbusds.jwt.EncryptedJWT;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.text.ParseException;
import java.util.UUID;
import java.util.function.Function;

@Log4j2
@AllArgsConstructor
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
            log.error("Value not deserialize to refresh token");
        }

        return null;
    }
}
