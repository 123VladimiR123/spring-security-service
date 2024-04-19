package com.springsecurityservice.springsecurityservice.securityservices;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomRequestMatcher implements RequestMatcher {

    @Override
    public boolean matches(HttpServletRequest request) {
        return !List.of("/login", "/register").contains(request.getRequestURI());
    }
}
