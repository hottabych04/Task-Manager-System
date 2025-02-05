package com.hottabych04.example.service.security;

import com.hottabych04.app.service.security.AuthorizationUtil;
import com.hottabych04.example.IntegrationTestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.List;

public class AuthorizationUtilTest extends IntegrationTestBase {

    @Autowired
    private AuthorizationUtil authorizationUtil;

    @Test
    @DisplayName("Successful verification for admin")
    public void isAdmin(){
        Authentication admin = new PreAuthenticatedAuthenticationToken(
                "admin@gmail.com",
                null,
                List.of(
                        new SimpleGrantedAuthority("ROLE_ADMIN")
                )
        );

        boolean isAdmin = authorizationUtil.isAdmin(admin);

        Assertions.assertTrue(isAdmin);
    }

    @Test
    @DisplayName("Fail verification for user")
    public void isNotAdmin(){
        Authentication user = new PreAuthenticatedAuthenticationToken(
                "admin@gmail.com",
                null,
                List.of(
                        new SimpleGrantedAuthority("ROLE_USER")
                )
        );

        boolean isAdmin = authorizationUtil.isAdmin(user);

        Assertions.assertFalse(isAdmin);
    }
}
