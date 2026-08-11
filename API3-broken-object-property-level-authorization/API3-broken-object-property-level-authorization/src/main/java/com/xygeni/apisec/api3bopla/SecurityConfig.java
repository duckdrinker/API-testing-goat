package com.xygeni.apisec.api3bopla;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * /api/users/profile is left permitAll so the original vulnerable
 * demonstration (UserProfileController, no per-method auth annotation)
 * keeps behaving as unauthenticated. Every other path defaults to
 * requiring authentication - that's what makes the negative-demo
 * endpoints below actually authenticated.
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/users/profile").permitAll()
                .anyRequest().authenticated());
        return http.build();
    }
}
