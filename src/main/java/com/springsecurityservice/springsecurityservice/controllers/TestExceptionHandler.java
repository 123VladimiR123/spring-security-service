package com.springsecurityservice.springsecurityservice.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.management.InstanceAlreadyExistsException;
import javax.security.auth.login.CredentialException;

@ControllerAdvice
public class TestExceptionHandler implements AccessDeniedHandler {
    @Value("${LOCALHOST_GATEWAY_ADDRESS}")
    private String prefix;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) {
        response.setStatus(302);
        response.setHeader("Location", prefix + "/login?failure");
    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    protected void wrongUsername(HttpServletResponse response) {
        response.setStatus(302);
        response.setHeader("Location", prefix + "/login?error");
    }

    @ExceptionHandler(CredentialException.class)
    protected void notSamePasswordRegistration( HttpServletResponse response) {
        response.setStatus(302);
        response.setHeader("Location", prefix + "/registration?pass");
    }

    @ExceptionHandler(InstanceAlreadyExistsException.class)
    protected void userExistsRegistration (HttpServletResponse response) {
        response.setStatus(302);
        response.setHeader("Location", prefix + "/registration?exists");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected void smallPasswordRegistration(HttpServletResponse response) {
        response.setStatus(302);
        response.setHeader("Location", prefix + "/registration?incorrect");
    }
}
