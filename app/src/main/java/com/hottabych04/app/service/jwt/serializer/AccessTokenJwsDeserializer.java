package com.hottabych04.app.service.jwt.serializer;

import com.hottabych04.app.service.jwt.entity.Token;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;

import java.text.ParseException;
import java.util.UUID;
import java.util.function.Function;

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
            System.out.println("Error verify signed JWT");
        }

        return null;
    }
}
