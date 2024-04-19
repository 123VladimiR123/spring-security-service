package com.springsecurityservice.springsecurityservice.securityservices;

import com.springsecurityservice.springsecurityservice.entities.CustomUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class CustomAuthenticationManager implements AuthenticationManager {

    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    private final List<GrantedAuthority> list = List.of((GrantedAuthority)new SimpleGrantedAuthority("ROLE_USER"));

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication.isAuthenticated()) return authentication;

        CustomUser customUser = (CustomUser)userDetailsService
            .loadUserByUsername(authentication.getPrincipal().toString());

        if (passwordEncoder.matches(authentication.getCredentials().toString(), customUser.getPassword())) {
            authentication = new UsernamePasswordAuthenticationToken(customUser.getUsername(),customUser.getId(), list);
        }
        else throw new AuthenticationCredentialsNotFoundException("wrong username or password");

        return authentication;
    }
}
