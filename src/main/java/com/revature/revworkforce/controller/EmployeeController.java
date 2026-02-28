package com.revature.revworkforce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * Controller for the Employee Dashboard and features.
 */
@Controller
@RequestMapping("/employee")
@PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'ADMIN')")
public class EmployeeController {

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard/employee-dashboard"; // Maps to /templates/dashboard/employee-dashboard.html
    }
}
