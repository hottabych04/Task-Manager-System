package com.hottabych04.app.service.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class AuthorizationUtil {

    private final static String ROLE_PREFIX = "ROLE_";

    public boolean isAdmin(Authentication authentication){
        return hasRole(authentication, "ADMIN");
    }

    public boolean isUser(Authentication authentication){
        return hasRole(authentication, "USER");
    }

    public boolean hasRole(Authentication authentication, String roleWithoutPrefix){
        var role = new SimpleGrantedAuthority(roleWithPrefix(roleWithoutPrefix));

        return authentication.getAuthorities().contains(role);
    }

    private String roleWithPrefix(String roleWithoutPrefix){
        return ROLE_PREFIX + roleWithoutPrefix;
    }

}