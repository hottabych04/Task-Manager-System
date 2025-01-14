package com.hottabych04.app.service.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class AuthorizationUtil {

    private final static String ROLE_PREFIX = "ROLE_";

    public boolean isAdmin(Authentication authentication){
        return hasRole(authentication, "ADMIN");
    }

    public boolean hasRole(Authentication authentication, String roleWithoutPrefix){
        var role = roleWithPrefix(roleWithoutPrefix);

        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(it -> it.equals(role));
    }

    private String roleWithPrefix(String roleWithoutPrefix){
        return ROLE_PREFIX + roleWithoutPrefix;
    }

}
