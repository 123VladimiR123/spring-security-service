package com.springsecurityservice.springsecurityservice.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RequestMapping("/logout")
@RestController
public class LogoutController {

    @Value("${jwt.cookie.name}")
    private String cookieName;
    @Value("${LOCALHOST_GATEWAY_ADDRESS}")
    private String prefix;
    @GetMapping
    public void logout(HttpServletResponse response) throws IOException {
        Cookie cookie = new Cookie(cookieName, "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        response.sendRedirect(prefix + "/login");
    }
}
