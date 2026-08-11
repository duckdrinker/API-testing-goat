package com.xygeni.apisec.api2auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Centralized, non-per-method auth configuration (ASEC-045 known-limitation fixture).
 *
 * /api/accounts/balance and /health are explicitly permitAll so the original
 * unauthenticated_endpoint demonstrations (ASEC-043, ASEC-044) keep behaving
 * exactly as before. Every other request defaults to
 * anyRequest().authenticated() - this is the "centralized" auth declaration
 * style (comparable to WebSecurityConfig.authorizeHttpRequests() in the real
 * crAPI benchmark) that Xygeni's unauthenticated_endpoint detector does not
 * parse: it only looks for per-method/class auth annotations near the
 * handler, so an endpoint protected only by this filter chain (see
 * TransactionController) may still be flagged as a false-positive candidate.
 *
 * /api/login is also explicitly permitAll - a login endpoint is naturally
 * unauthenticated, and this keeps LoginController's `password` parameter
 * (ASEC-033) genuinely unauthenticated per the model, so
 * sensitive_param_unauthenticated fires and surfaces the classifier's tag
 * directly in a flaw's evidence, instead of being swallowed by the
 * centralized "authenticated" classification.
 */
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/accounts/balance", "/health", "/api/login").permitAll()
                .anyRequest().authenticated());
        return http.build();
    }
}
