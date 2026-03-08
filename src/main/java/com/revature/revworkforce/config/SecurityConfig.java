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
 * Configuration class for Spring Security.
 *
 * <p>
 * This class defines authentication, authorization, and security policies
 * used throughout the application. It enables secure access control,
 * password encryption, session handling, and login management.
 * </p>
 *
 * <h2>Security Features</h2>
 * <ul>
 * <li>Form-based authentication</li>
 * <li>BCrypt password encoding</li>
 * <li>Role-Based Access Control (RBAC)</li>
 * <li>Session management</li>
 * <li>CSRF protection</li>
 * <li>Remember-me authentication</li>
 * <li>Custom authentication success handler</li>
 * </ul>
 *
 * <h2>Access Control Rules</h2>
 * <ul>
 * <li><b>Public Access:</b> "/", "/login", "/css/**", "/js/**",
 * "/images/**"</li>
 * <li><b>ADMIN:</b> "/admin/**"</li>
 * <li><b>MANAGER:</b> "/manager/**" (accessible by ADMIN as well)</li>
 * <li><b>EMPLOYEE:</b> "/employee/**" (accessible by MANAGER and ADMIN)</li>
 * <li><b>Authenticated Users:</b> "/api/**" and all other secured
 * endpoints</li>
 * </ul>
 *
 * @author RevWorkForce Team
 * @author Mastan Sayyad
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

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Authorize HTTP requests
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/error", "/error/**").permitAll()

                        // Public endpoints (no authentication required)
                        .requestMatchers(
                                "/",
                                "/login",
                                "/forgot-password",
                                "/forgot-password/**" // <-- allow all forgot password paths
                        ).permitAll()
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()

                        // Admin endpoints (ADMIN role required)
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // Manager endpoints (MANAGER or ADMIN role required)
                        .requestMatchers("/manager/**").hasAnyRole("MANAGER", "ADMIN")

                        // Employee endpoints (any authenticated user)
                        .requestMatchers("/employee/**").hasAnyRole("EMPLOYEE", "MANAGER", "ADMIN")

                        // Dashboard (authenticated users)
                        .requestMatchers("/dashboard").authenticated()

                        // API endpoints (authenticated users)
                        .requestMatchers("/api/**").authenticated()

                        // All other requests require authentication
                        .anyRequest().authenticated())

                // Form login configuration
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .successHandler(authenticationSuccessHandler)
                        .failureUrl("/login?error=true")
                        .usernameParameter("username") // Can be email or employee ID
                        .passwordParameter("password")
                        .permitAll())

                // Logout configuration
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID", "remember-me")
                        .permitAll())
                .sessionManagement(session -> session
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(false)
                        .expiredUrl("/login?expired=true"))

                // Remember me configuration
                .rememberMe(remember -> remember
                        .key("revworkforce-remember-me-key")
                        .tokenValiditySeconds(86400) // 24 hours
                        .rememberMeParameter("remember-me")
                        .userDetailsService(userDetailsService))

                // Exception handling (removing explicit accessDeniedPage mapping to prevent
                // Thymeleaf rendering errors)
                .exceptionHandling(ex -> ex.accessDeniedPage("/error"))

                // CSRF configuration
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/**", "/forgot-password/**") // <-- ignore CSRF for
                                                                                   // forgot-password
                );

        System.out.println("\n========================================");
        System.out.println("Spring Security Configuration Loaded");
        System.out.println("========================================");
        System.out.println("Authentication: Form-based (BCrypt)");
        System.out.println("Session Timeout: 30 minutes");
        System.out.println("Max Sessions: 1 per user");
        System.out.println("CSRF Protection: Enabled (except /api/** and /forgot-password/**)");
        System.out.println("Remember Me: 24 hours");
        System.out.println("========================================\n");

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}