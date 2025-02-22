package com.crm.erp.config;

import com.crm.erp.security.JwtAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Security configuration for API authentication and authorization.
 */
@Configuration
public class SecurityConfig {

    private static final String[] PUBLIC_ENDPOINTS = {
            "/api/auth/login",
            "/api/users/register",
            "/api/public/**",
            "/api/projects/**",  // ✅ Allow access to project-related actions
            "/api/clients/**",
            "/api/invoices/**"
    };

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final Environment env;
    private final ObjectMapper objectMapper;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, Environment env, ObjectMapper objectMapper) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.env = env;
        this.objectMapper = objectMapper;
    }

    /**
     * Configures security settings for API endpoints.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(corsConfigSource())) // ✅ CORS Configuration
                .csrf(csrf -> csrf.disable()) // ✅ Disable CSRF for REST APIs
                .exceptionHandling(exception -> exception.authenticationEntryPoint(this::handleAuthException)) // ✅ Custom Exception Handling
                .authorizeHttpRequests(auth -> setupAuthorizationRules(auth)) // ✅ Define Authorization Rules
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // ✅ Stateless Sessions
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // ✅ Add JWT Filter
                .build();
    }

    /**
     * Defines authorization rules for API endpoints.
     */
    private void setupAuthorizationRules(org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
        auth
                .requestMatchers(PUBLIC_ENDPOINTS).permitAll() // ✅ Allow all public APIs
                .requestMatchers("/api/admin/**").hasAuthority("ROLE_ADMIN") // ✅ Restrict admin routes
                .requestMatchers("/api/projects/create").permitAll() // ✅ Allow project creation
                .requestMatchers("/api/projects/{id}").permitAll() // ✅ Allow fetching project details
                .requestMatchers("/api/projects/update/{id}").permitAll() // ✅ Allow project updates
                .requestMatchers("/api/projects/delete/{id}").permitAll() // ✅ Allow project deletion
                .requestMatchers("/api/projects/restore/{id}").permitAll() // ✅ Allow restoring projects
                .anyRequest().authenticated(); // ✅ Secure all other endpoints
    }

    /**
     * Handles authentication exceptions and returns a JSON response.
     */
    private void handleAuthException(HttpServletRequest request, HttpServletResponse response, Exception authException) throws IOException {
        if (!response.isCommitted()) {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);

            Map<String, Object> errorDetails = Map.of(
                    "error", "Access Denied",
                    "message", "You do not have permission to access this resource.",
                    "timestamp", System.currentTimeMillis(),
                    "path", request.getRequestURI()
            );

            response.getWriter().write(objectMapper.writeValueAsString(errorDetails));
            response.getWriter().flush();
        }
    }

    /**
     * Provides CORS configuration for cross-origin requests.
     */
    @Bean
    public CorsFilter corsFilter() {
        return new CorsFilter(corsConfigSource());
    }

    private UrlBasedCorsConfigurationSource corsConfigSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of(env.getProperty("cors.allowed-origins", "http://localhost:3000").split(",")));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // ✅ Ensure all HTTP methods are included
        config.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept", "Origin"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    /**
     * Provides password encoder for secure authentication.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures authentication manager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
