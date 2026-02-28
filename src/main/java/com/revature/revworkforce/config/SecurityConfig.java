package com.revature.revworkforce.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Spring Security Configuration.
 * 
 * Configures authentication, authorization, and security settings for the application.
 * 
 * Security Features:
 * - Form-based authentication
 * - BCrypt password encoding
 * - Role-based access control (RBAC)
 * - Session management
 * - CSRF protection
 * - Remember-me functionality
 * - Custom authentication success handling
 * 
 * Access Control:
 * - Public: /, /login, /css/**, /js/**, /images/**
 * - ADMIN: /admin/**
 * - MANAGER: /manager/** (also accessible by ADMIN)
 * - EMPLOYEE: /employee/** (also accessible by MANAGER and ADMIN)
 * - Authenticated: /api/**, all other pages
 * 
 * @author RevWorkForce Team
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {
    
    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;
    
    /**
     * Configure HTTP security filter chain.
     * 
     * @param http HttpSecurity object
     * @return SecurityFilterChain
     * @throws Exception if configuration fails
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Authorize HTTP requests
            .authorizeHttpRequests(auth -> auth
                // Public endpoints (no authentication required)
                .requestMatchers("/", "/login", "/error/**").permitAll()
                .requestMatchers("/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()
                
                // Admin endpoints (ADMIN role required)
                .requestMatchers("/admin/**").hasRole("ADMIN")
                
                // Manager endpoints (MANAGER or ADMIN role required)
                .requestMatchers("/manager/**").hasAnyRole("MANAGER", "ADMIN")
                
                // Employee endpoints (any authenticated user)
                .requestMatchers("/employee/**").hasAnyRole("EMPLOYEE", "MANAGER", "ADMIN")
                
                // Dashboard (authenticated users, role-based redirect)
                .requestMatchers("/dashboard").authenticated()
                
                // API endpoints (authenticated users)
                .requestMatchers("/api/**").authenticated()
                
                // All other requests require authentication
                .anyRequest().authenticated()
            )
            
            // Form login configuration
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .successHandler(authenticationSuccessHandler)
                .failureUrl("/login?error=true")
                .usernameParameter("username")  // Can be email or employee ID
                .passwordParameter("password")
                .permitAll()
            )
            
            // Logout configuration
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID", "remember-me")
                .permitAll()
            )
            
            // Session management
            .sessionManagement(session -> session
                .maximumSessions(1) // Only one session per user
                .maxSessionsPreventsLogin(false) // New login invalidates old session
                .expiredUrl("/login?expired=true")
            )
            
            // Remember me configuration
            .rememberMe(remember -> remember
                .key("revworkforce-remember-me-key")
                .tokenValiditySeconds(86400) // 24 hours
                .rememberMeParameter("remember-me")
                .userDetailsService(userDetailsService)
            )
            
            // Exception handling
            .exceptionHandling(ex -> ex
                .accessDeniedPage("/error/403")
            )
            
            // CSRF protection (enabled by default for POST requests)
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/api/public/**") // Disable CSRF for public API if needed
            );
        
        System.out.println("\n========================================");
        System.out.println("Spring Security Configuration Loaded");
        System.out.println("========================================");
        System.out.println("Authentication: Form-based (BCrypt)");
        System.out.println("Session Timeout: 30 minutes");
        System.out.println("Max Sessions: 1 per user");
        System.out.println("CSRF Protection: Enabled");
        System.out.println("Remember Me: 24 hours");
        System.out.println("========================================\n");
        
        return http.build();
    }
    
    /**
     * Configure authentication provider with BCrypt password encoder.
     * 
     * @return DaoAuthenticationProvider
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }
    
    /**
     * Expose AuthenticationManager as a bean.
     * 
     * @param authConfig AuthenticationConfiguration
     * @return AuthenticationManager
     * @throws Exception if configuration fails
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}