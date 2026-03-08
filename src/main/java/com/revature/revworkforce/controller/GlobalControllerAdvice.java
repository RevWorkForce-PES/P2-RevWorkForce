package com.revature.revworkforce.controller;

import com.revature.revworkforce.model.Employee;
import com.revature.revworkforce.security.SecurityUtils;
import com.revature.revworkforce.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Global Controller Advice.
 * 
 * Provides common attributes to all controllers' models.
 * This ensures user information is available to the sidebar on every page.
 * 
 * @author RevWorkForce Team
 */
@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private EmployeeService employeeService;

    /**
     * Adds global user information to the model.
     * 
     * @param model the model
     */
    @ModelAttribute
    public void addGlobalUserAttributes(Model model) {
        if (SecurityUtils.isAuthenticated()) {
            String employeeId = SecurityUtils.getCurrentUsername();

            // Default values
            String fullName = employeeId;
            String role = "User";

            try {
                Employee employee = employeeService.getEmployeeById(employeeId);
                if (employee != null) {
                    fullName = employee.getFirstName() + " " + employee.getLastName();
                }
            } catch (Exception e) {
                // Ignore and use defaults
            }

            if (SecurityUtils.hasRole("ADMIN")) {
                role = "Admin";
            } else if (SecurityUtils.hasRole("MANAGER")) {
                role = "Manager";
            } else if (SecurityUtils.hasRole("EMPLOYEE")) {
                role = "Employee";
            }

            model.addAttribute("globalFullName", fullName);
            model.addAttribute("globalUserRole", role);
        }
    }
}
