package com.m01project.taskmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // close CSRF protection.
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // allow all requests.
                )
                .formLogin(form -> form.disable()) // shut down login.
                .httpBasic(basic -> basic.disable()); // close basic auth.

        return http.build();
    }
}
