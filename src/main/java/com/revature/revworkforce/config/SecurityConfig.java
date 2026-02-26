package com.revature.revworkforce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Spring Security Configuration.
 * 
 * Configures authentication, authorization, and security settings.
 * 
 * @author RevWorkForce Team
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

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
                                // Authorize requests
                                .authorizeHttpRequests(auth -> auth
                                                // Public endpoints (no authentication required)
                                                .requestMatchers("/", "/login", "/css/**", "/js/**", "/images/**")
                                                .permitAll()

                                                // Admin endpoints (ADMIN role required)
                                                .requestMatchers("/admin/**").hasRole("ADMIN")

                                                // Manager endpoints (MANAGER or ADMIN role required)
                                                .requestMatchers("/manager/**").hasAnyRole("MANAGER", "ADMIN")

                                                // Employee endpoints (authenticated users)
                                                .requestMatchers("/employee/**")
                                                .hasAnyRole("EMPLOYEE", "MANAGER", "ADMIN")

                                                // API endpoints
                                                .requestMatchers("/api/**").authenticated()

                                                // All other requests require authentication
                                                .anyRequest().authenticated())

                                // Form login configuration
                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .loginProcessingUrl("/login")
                                                .defaultSuccessUrl("/dashboard", true)
                                                .failureUrl("/login?error=true")
                                                .usernameParameter("username")
                                                .passwordParameter("password")
                                                .permitAll())

                                // Logout configuration
                                .logout(logout -> logout
                                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                                .logoutSuccessUrl("/login?logout=true")
                                                .invalidateHttpSession(true)
                                                .deleteCookies("JSESSIONID")
                                                .permitAll())

                                // Session management
                                .sessionManagement(session -> session
                                                .maximumSessions(1) // Only one session per user
                                                .expiredUrl("/login?expired=true"))

                                // Remember me configuration
                                .rememberMe(remember -> remember
                                                .key("revworkforce-remember-me-key")
                                                .tokenValiditySeconds(86400) // 24 hours
                                )

                                // Exception handling
                                .exceptionHandling(ex -> ex
                                                .accessDeniedPage("/error/403"));

                return http.build();
        }
}