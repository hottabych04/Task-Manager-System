package com.hottabych04.app.configuration.security.configurer;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityAuthenticationConfigurer extends AbstractHttpConfigurer<SecurityAuthenticationConfigurer, HttpSecurity> {

    private final UserDetailsService authenticationUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        var daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(this.authenticationUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(this.passwordEncoder);

        builder.authenticationProvider(daoAuthenticationProvider);
    }
}
