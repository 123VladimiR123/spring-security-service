package com.springsecurityservice.springsecurityservice.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.management.InstanceAlreadyExistsException;
import javax.security.auth.login.CredentialException;
import java.io.IOException;

@ControllerAdvice
public class TestExceptionHandler implements AccessDeniedHandler {
    @SneakyThrows
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) {
        response.sendRedirect("/login?failure");
    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    protected void wrongusername(HttpServletResponse response) {
        try {
            response.sendRedirect("/login?error");
        } catch (IOException ignores) {
        }
    }

    @SneakyThrows
    @ExceptionHandler(CredentialException.class)
    protected void notSamePasswordRegistration( HttpServletResponse response) {
        response.sendRedirect("/registration?pass");
    }

    @SneakyThrows
    @ExceptionHandler(InstanceAlreadyExistsException.class)
    protected void userExistsRegistration (HttpServletResponse response) {
        response.sendRedirect("/registration?exists");
    }

    @SneakyThrows
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected void smallPasswordRegistration(HttpServletResponse response) {
        response.sendRedirect("/registration?incorrect");
    }
}
