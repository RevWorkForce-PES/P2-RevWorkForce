package com.revature.revworkforce.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Security Utility Class.
 * 
 * Provides helper methods for security-related operations.
 * 
 * @author RevWorkForce Team
 */
public class SecurityUtils {
    
    /**
     * Get the currently authenticated user's username (employee ID).
     * 
     * @return employee ID or null if not authenticated
     */
    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        
        Object principal = authentication.getPrincipal();
        
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            return (String) principal;
        }
        
        return null;
    }
    
    /**
     * Get the current authentication object.
     * 
     * @return Authentication object or null
     */
    public static Authentication getCurrentAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
    
    /**
     * Check if user is authenticated.
     * 
     * @return true if user is authenticated
     */
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && 
               authentication.isAuthenticated() && 
               !"anonymousUser".equals(authentication.getPrincipal());
    }
    
    /**
     * Check if current user has a specific role.
     * 
     * @param role the role name (with or without ROLE_ prefix)
     * @return true if user has the role
     */
    public static boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        
        // Add ROLE_ prefix if not present
        String roleToCheck = role.startsWith("ROLE_") ? role : "ROLE_" + role;
        
        return authentication.getAuthorities().stream()
            .anyMatch(auth -> auth.getAuthority().equals(roleToCheck));
    }
    
    /**
     * Check if current user has any of the specified roles.
     * 
     * @param roles array of role names
     * @return true if user has any of the roles
     */
    public static boolean hasAnyRole(String... roles) {
        for (String role : roles) {
            if (hasRole(role)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Get all authorities (roles) of current user.
     * 
     * @return collection of granted authorities
     */
    public static Collection<? extends GrantedAuthority> getCurrentUserAuthorities() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        
        return authentication.getAuthorities();
    }
}