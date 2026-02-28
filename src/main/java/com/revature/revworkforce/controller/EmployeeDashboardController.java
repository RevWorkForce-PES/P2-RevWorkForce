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

/**
 * Employee Dashboard Controller.
 * 
 * Handles employee self-service dashboard and operations.
 * Accessible by all authenticated users.
 * 
 * @author RevWorkForce Team
 */
@Controller
@RequestMapping("/employee")
@PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'ADMIN')")
public class EmployeeDashboardController {

    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * Display employee dashboard.
     * 
     * @param model the model
     * @return employee dashboard view
     */
    @GetMapping("/dashboard")
    public String employeeDashboard(Model model) {
        String employeeId = SecurityUtils.getCurrentUsername();

        // Get current user
        Employee currentUser = employeeRepository.findById(employeeId).orElse(null);

        if (currentUser != null) {
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("fullName", currentUser.getFullName());
            model.addAttribute("designation",
                    currentUser.getDesignation() != null ? currentUser.getDesignation().getDesignationName() : "N/A");
            model.addAttribute("department",
                    currentUser.getDepartment() != null ? currentUser.getDepartment().getDepartmentName() : "N/A");
        }

        model.addAttribute("pageTitle", "Employee Dashboard");

        return "dashboard/employee-dashboard";
    }
}