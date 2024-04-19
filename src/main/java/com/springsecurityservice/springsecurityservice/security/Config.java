package com.springsecurityservice.springsecurityservice.security;

import com.springsecurityservice.springsecurityservice.securityservices.CustomAuthenticationManager;
import com.springsecurityservice.springsecurityservice.securityservices.CustomUserDetailsService;
import com.springsecurityservice.springsecurityservice.securityservices.TokenFilter;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.ForwardAuthenticationFailureHandler;
import org.springframework.security.web.session.SessionManagementFilter;


@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class Config {

    @Value("${jwt.cookie.name}")
    private String cookieName;


    private final TokenFilter tokenFilter;
    private final CustomAuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;


    //Dont use roles
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .authenticationManager(authenticationManager)
                .exceptionHandling(e -> e
                        .authenticationEntryPoint((request, response, authException) -> response
                                .sendRedirect("/login?req")))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/registration").anonymous()
                        .requestMatchers(HttpMethod.OPTIONS).permitAll()
                        .requestMatchers(HttpMethod.GET).permitAll()
                        .requestMatchers(HttpMethod.POST).permitAll()
                        .requestMatchers(HttpMethod.GET,"/favicon.ico").permitAll()
                        .anyRequest().authenticated())
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(log -> log
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .addLogoutHandler((request, response, authentication) -> {
                            Cookie cookie = new Cookie(cookieName, "");
                            cookie.setMaxAge(0);
                            cookie.setPath("/");
                            response.addCookie(cookie);
                        }))
                .rememberMe(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(e -> e.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(tokenFilter, SessionManagementFilter.class)
                .httpBasic(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .userDetailsService(userDetailsService)
                .build();
    }

    @Bean
    protected AuthenticationFailureHandler handler() {
        return new ForwardAuthenticationFailureHandler("/login?error");
    }
}
