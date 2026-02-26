package com.revature.revworkforce.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;

/**
 * Controller for routing authenticated users to their respective dashboards.
 */
@Controller
public class DashboardController {

    /**
     * Default success URL after login. Redirects user based on their role.
     * 
     * @param authentication the current authenticated user
     * @return the redirect URL to the appropriate dashboard
     */
    @GetMapping("/dashboard")
    public String defaultAfterLogin(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login"; // Should not happen due to Spring Security config
        }

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        // Check assigned roles (Note: Authorities typically have 'ROLE_' prefix)
        boolean isAdmin = authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boolean isManager = authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_MANAGER"));
        boolean isEmployee = authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_EMPLOYEE"));

        if (isAdmin) {
            return "redirect:/admin/dashboard";
        } else if (isManager) {
            return "redirect:/manager/dashboard";
        } else if (isEmployee) {
            return "redirect:/employee/dashboard";
        }

        // Fallback if no specific role matches
        return "redirect:/login?error=true";
    }
}
