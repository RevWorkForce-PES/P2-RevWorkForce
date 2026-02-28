package com.revature.revworkforce.controller;

import com.revature.revworkforce.enums.EmployeeStatus;
import com.revature.revworkforce.model.Employee;
import com.revature.revworkforce.repository.EmployeeRepository;
import com.revature.revworkforce.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Admin Dashboard Controller.
 * 
 * Handles admin-specific dashboard and operations.
 * Only accessible by users with ADMIN role.
 * 
 * @author RevWorkForce Team
 */
@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminDashboardController {

    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * Display admin dashboard.
     * 
     * @param model the model
     * @return admin dashboard view
     */
    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        String employeeId = SecurityUtils.getCurrentUsername();

        // Get current user
        Employee currentUser = employeeRepository.findById(employeeId).orElse(null);

        if (currentUser != null) {
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("fullName", currentUser.getFullName());
        }

        // Get statistics
        long totalEmployees = employeeRepository.count();
        long activeEmployees = employeeRepository.countByStatus(EmployeeStatus.ACTIVE);

        model.addAttribute("totalEmployees", totalEmployees);
        model.addAttribute("activeEmployees", activeEmployees);
        model.addAttribute("pageTitle", "Admin Dashboard");

        return "dashboard/admin-dashboard";
    }
}