package com.threatbeacon.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

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
                )
                .httpBasic(withDefaults());
        return http.build();
    }
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withUsername("soc-demo")
                .password("{noop}demo123!") // {noop} indica que la contraseña no está encriptada (solo para dev)
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user);
    }
}