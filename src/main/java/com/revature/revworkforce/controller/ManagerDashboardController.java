package com.revature.revworkforce.controller;

import com.revature.revworkforce.model.Employee;
import com.revature.revworkforce.repository.EmployeeRepository;
import com.revature.revworkforce.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Manager Dashboard Controller.
 * 
 * Handles manager-specific dashboard and operations.
 * Accessible by users with MANAGER or ADMIN role.
 * 
 * @author RevWorkForce Team
 */
@Controller
@RequestMapping("/manager")
@PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
public class ManagerDashboardController {
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    /**
     * Display manager dashboard.
     * 
     * @param model the model
     * @return manager dashboard view
     */
    @GetMapping("/dashboard")
    public String managerDashboard(Model model) {
        String employeeId = SecurityUtils.getCurrentUsername();
        
        // Get current user
        Employee currentUser = employeeRepository.findById(employeeId).orElse(null);
        
        if (currentUser != null) {
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("fullName", currentUser.getFullName());
            
            // Get team members (employees reporting to this manager)
            List<Employee> teamMembers = employeeRepository.findByManager_EmployeeId(employeeId);
            model.addAttribute("teamSize", teamMembers.size());
        }
        
        model.addAttribute("pageTitle", "Manager Dashboard");
        
        return "manager/dashboard";
    }
}