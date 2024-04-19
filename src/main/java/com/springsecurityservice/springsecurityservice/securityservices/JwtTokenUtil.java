package com.springsecurityservice.springsecurityservice.securityservices;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.Serializable;

import java.security.NoSuchAlgorithmException;

import io.jsonwebtoken.Jwts;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

@Service
public class JwtTokenUtil implements Serializable {
    private final CustomUserDetailsService customUserDetailsService;

    @Value("${jwt.cookie.name}")
    private String tokenCookieName;
    private final SecretKey secretKey;

    {
        try {
            KeyGenerator generator = KeyGenerator.getInstance("HmacSHA256");
            generator.init(256);
            secretKey = generator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public Cookie generateJWTCookie(String email, String id) {

        Cookie cookie =  new Cookie(tokenCookieName, Jwts.builder()
                .subject("user")
                .claim("email", email)
                .claim("id", id)
                .signWith(secretKey)
                .compact());

        cookie.setHttpOnly(true);
        return cookie;
    }

    // returns email
    public String validateJWT(String token) throws UsernameNotFoundException {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .decryptWith(secretKey)
                .build().parseSignedClaims(token)
                .getPayload();

        return customUserDetailsService.verifyByIdAndEmail(
                claims.get("id", String.class),
                claims.get("email", String.class))
                .orElseThrow(() -> {return new UsernameNotFoundException("Wrong token");})
                .getUsername();
    }

    public JwtTokenUtil(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }
}
