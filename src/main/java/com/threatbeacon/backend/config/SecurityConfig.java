package com.threatbeacon.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. Disable CSRF: Required for POST/PUT/DELETE requests to work in a REST API
                .csrf(csrf -> csrf.disable())
                // 2. Authorization settings
                .authorizeHttpRequests(auth -> auth
                        // Allows public access to all routes under /api/beacon (including /mute)
                        .requestMatchers("/api/beacon/**","/api/risk").permitAll()
                        // Any other request must be authenticated (default behavior)
                        .anyRequest().authenticated()
                );
        return http.build();
    }
}