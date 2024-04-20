package com.springsecurityservice.springsecurityservice.controllers;

import com.springsecurityservice.springsecurityservice.entities.CustomUser;
import com.springsecurityservice.springsecurityservice.entities.CustomUserDTO;
import com.springsecurityservice.springsecurityservice.securityservices.CustomUserDetailsService;
import com.springsecurityservice.springsecurityservice.securityservices.JwtTokenUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.management.InstanceAlreadyExistsException;
import javax.security.auth.login.CredentialException;

@Controller
@RequestMapping("/registration")
@RequiredArgsConstructor
public class RegistrationController {

    private final CustomUserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    @Value("${LOCALHOST_GATEWAY_ADDRESS}")
    private String prefix;

    @GetMapping
    public ModelAndView regGet() {
        return new ModelAndView("registration");
    }

    @PostMapping
    public ModelAndView regPost(@Validated @ModelAttribute CustomUserDTO userDTO,
                                @NonNull HttpServletResponse response) throws CredentialException, InstanceAlreadyExistsException {
        CustomUser user = userDetailsService.save(userDTO);
        response.addCookie(jwtTokenUtil.generateJWTCookie(user.getUsername(), user.getId().toString()));
        response.setStatus(302);
        response.setHeader("Location", prefix + "/");
        return null;
    }
}
