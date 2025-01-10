package com.hottabych04.app.service.security.jwt.detail;

import com.hottabych04.app.database.repository.DeactivatedTokenRepository;
import com.hottabych04.app.database.repository.UserRepository;
import com.hottabych04.app.service.security.jwt.entity.Token;
import com.hottabych04.app.service.security.jwt.entity.TokenUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.time.Instant;

@RequiredArgsConstructor
public class TokenAuthenticationUserDetailsService
        implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

    private final DeactivatedTokenRepository deactivatedTokenRepository;

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken authenticationToken)
            throws UsernameNotFoundException {
        if (authenticationToken.getPrincipal() instanceof Token token){
            return new TokenUser(
                    token.login(),
                    "nopassword",
                    true,
                    !deactivatedTokenRepository.existsById(token.id()) && token.expiredAt().isAfter(Instant.now()),
                    token.expiredAt().isAfter(Instant.now()),
                    userRepository.existsByEmail(token.login()),
                    token.authorities().stream()
                            .map(SimpleGrantedAuthority::new)
                            .toList(),
                    token
            );
        }

        throw new UsernameNotFoundException("Principal must be of type Token");
    }
}
