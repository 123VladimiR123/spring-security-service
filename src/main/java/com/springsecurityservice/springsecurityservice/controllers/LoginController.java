package com.springsecurityservice.springsecurityservice.controllers;

import com.springsecurityservice.springsecurityservice.securityservices.JwtTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    private final AuthenticationManager customAuthenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    @Value("${LOCALHOST_GATEWAY_ADDRESS}")
    private String prefix;

    @GetMapping
    public ModelAndView loginPage() {
        return new ModelAndView("login");
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity postLoginPage(@RequestParam("Username") String username,
                                        @RequestParam("Password") String password,
                                        HttpServletResponse response){
        Authentication token =
                new UsernamePasswordAuthenticationToken(username, password, List.of(new SimpleGrantedAuthority("ROLE_USER")));
        token.setAuthenticated(false);

        token = customAuthenticationManager.authenticate(token);
        if (token instanceof AnonymousAuthenticationToken) {
            throw new AuthenticationCredentialsNotFoundException("incorrect password");
        } else {
            response.addCookie(jwtTokenUtil.generateJWTCookie(token.getPrincipal().toString(), token.getCredentials().toString()));
            response.setStatus(302);
            response.setHeader("Location", prefix + "/");
        }

        return ResponseEntity.ok(response);
    }

}
