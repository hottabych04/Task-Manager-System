package com.hottabych04.app.service.security.jwt.serializer;

import com.hottabych04.app.service.security.jwt.entity.Token;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.UUID;
import java.util.function.Function;

@Log4j2
@RequiredArgsConstructor
public class AccessTokenJwsDeserializer implements Function<String, Token> {

    private final JWSVerifier jwsVerifier;

    @Override
    public Token apply(String s) {
        try {
            var signedJWT = SignedJWT.parse(s);
            if (signedJWT.verify(this.jwsVerifier)){
                var jwtClaimsSet = signedJWT.getJWTClaimsSet();

                return new Token(
                        UUID.fromString(jwtClaimsSet.getJWTID()),
                        jwtClaimsSet.getSubject(),
                        jwtClaimsSet.getStringListClaim("authorities"),
                        jwtClaimsSet.getIssueTime().toInstant(),
                        jwtClaimsSet.getExpirationTime().toInstant()
                );
            }
        } catch (ParseException | JOSEException e) {
            log.error("Value not deserialize to access token");
        }

        return null;
    }
}
