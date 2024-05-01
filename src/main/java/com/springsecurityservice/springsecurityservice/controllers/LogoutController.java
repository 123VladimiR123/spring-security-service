package com.springsecurityservice.springsecurityservice.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
@RequestMapping("/logout")
@RestController
public class LogoutController {

    @Value("${jwt.cookie.name}")
    private String cookieName;
    @Value("${LOCALHOST_GATEWAY_ADDRESS}")
    private String prefix;
    @GetMapping
    public ResponseEntity logout(HttpServletResponse response){
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.status(302).header("Location", prefix + "/login").build();
    }
}
