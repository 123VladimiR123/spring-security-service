package com.springsecurityservice.springsecurityservice.securityservices;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;

@Service
public class TokenFilter extends AbstractAuthenticationProcessingFilter {
    private final JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.cookie.name}")
    private String tokenCookieName;

    private final CustomAuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        Cookie cookie = getTokenCookie(request);


        if (cookie == null) return null;
        else
            try {
                String email = jwtTokenUtil.validateJWT(cookie.getValue());
                PreAuthenticatedAuthenticationToken auth =
                        new PreAuthenticatedAuthenticationToken(email, null);
                auth.setAuthenticated(true);
                return auth;
            } catch (UsernameNotFoundException ignored) {
                throw new AuthenticationCredentialsNotFoundException("failed");
            }
    }

    @Override
    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
        return true;
    }

    private Cookie getTokenCookie(HttpServletRequest request) {
        return Arrays.stream(request.getCookies() != null ? request.getCookies() : new Cookie[]{})
                .filter(cookie -> cookie.getName().equals(tokenCookieName))
                .findFirst()
                .orElse(null);
    }

    public TokenFilter(JwtTokenUtil jwtTokenUtil, CustomAuthenticationManager manager) {
        super("/**", manager);
        this.authenticationManager = manager;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (getTokenCookie((HttpServletRequest)request) != null)
            SecurityContextHolder.getContext().setAuthentication(attemptAuthentication((HttpServletRequest)request, (HttpServletResponse) response));
        else chain.doFilter(request, response);
    }

    @Bean
    public FilterRegistrationBean<TokenFilter> tokenFilterFilterRegistrationBean(TokenFilter tokenFilter) {
        FilterRegistrationBean<TokenFilter> registrationBean = new FilterRegistrationBean<>(tokenFilter);
        registrationBean.setEnabled(false);
        return registrationBean;
    }
}
