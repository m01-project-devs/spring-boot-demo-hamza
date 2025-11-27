package com.m01project.taskmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/auth/**")  // exclude users endpoint.
                )
                // rules for endpoint
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/auth/**").permitAll()    // public
                        .requestMatchers("/api/users/**").authenticated() // requires auth
                        .anyRequest().authenticated()
                )

                // For testing using http, for real projects need to use JWT or formlogin.
                .httpBasic(basic -> {});
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        var user = User.withUsername("testuser")
                .password("{noop}password") // without encoding.
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user);
    }
}
