package com.revature.revworkforce.controller;

import com.revature.revworkforce.model.Employee;
import com.revature.revworkforce.repository.EmployeeRepository;
import com.revature.revworkforce.security.SecurityUtils;
import com.revature.revworkforce.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Dashboard Controller.
 * 
 * Handles dashboard routing based on user role.
 * 
 * @author RevWorkForce Team
 */
@Controller
public class DashboardController {
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private AuthService authService;
    
    /**
     * Main dashboard - redirects based on role.
     * 
     * @param authentication the authentication object
     * @return redirect to role-specific dashboard
     */
    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication) {
        String employeeId = SecurityUtils.getCurrentUsername();
        
        // Check if first login - redirect to change password
        if (employeeId != null && authService.isFirstLogin(employeeId)) {
            return "redirect:/change-password";
        }
        
        // Redirect based on role
        if (SecurityUtils.hasRole("ADMIN")) {
            return "redirect:/admin/dashboard";
        } else if (SecurityUtils.hasRole("MANAGER")) {
            return "redirect:/manager/dashboard";
        } else {
            return "redirect:/employee/dashboard";
        }
    }
}