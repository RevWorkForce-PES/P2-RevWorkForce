package com.revature.revworkforce.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

/**
 * Custom Authentication Success Handler.
 * 
 * Redirects users to appropriate dashboard based on their role after successful login.
 * 
 * Redirect Rules:
 * - ADMIN role → /admin/dashboard
 * - MANAGER role → /manager/dashboard
 * - EMPLOYEE role → /employee/dashboard
 * - Default → /dashboard
 * 
 * @author RevWorkForce Team
 */
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    
    /**
     * Handle successful authentication.
     * 
     * @param request the HTTP request
     * @param response the HTTP response
     * @param authentication the Authentication object
     * @throws IOException if redirect fails
     * @throws ServletException if servlet error occurs
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, 
                                       HttpServletResponse response,
                                       Authentication authentication) throws IOException, ServletException {
        
        // Get user authorities (roles)
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        
        // Determine redirect URL based on role
        String redirectUrl = determineTargetUrl(authorities);
        
        // Log successful login
        String username = authentication.getName();
        System.out.println("User '" + username + "' logged in successfully. Redirecting to: " + redirectUrl);
        
        // Redirect to appropriate dashboard
        response.sendRedirect(redirectUrl);
    }
    
    /**
     * Determine target URL based on user roles.
     * 
     * Priority: ADMIN > MANAGER > EMPLOYEE
     * 
     * @param authorities user authorities
     * @return redirect URL
     */
    private String determineTargetUrl(Collection<? extends GrantedAuthority> authorities) {
        // Check for ADMIN role (highest priority)
        if (hasRole(authorities, "ROLE_ADMIN")) {
            return "/admin/dashboard";
        }
        
        // Check for MANAGER role
        if (hasRole(authorities, "ROLE_MANAGER")) {
            return "/manager/dashboard";
        }
        
        // Check for EMPLOYEE role
        if (hasRole(authorities, "ROLE_EMPLOYEE")) {
            return "/employee/dashboard";
        }
        
        // Default redirect
        return "/dashboard";
    }
    
    /**
     * Check if user has a specific role.
     * 
     * @param authorities user authorities
     * @param role the role to check
     * @return true if user has the role
     */
    private boolean hasRole(Collection<? extends GrantedAuthority> authorities, String role) {
        return authorities.stream()
            .anyMatch(authority -> authority.getAuthority().equals(role));
    }
}