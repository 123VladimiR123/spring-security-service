package com.springsecurityservice.springsecurityservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableDiscoveryClient
public class SpringSecurityServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityServiceApplication.class, args);
    }

}