package com.revature.revworkforce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * Controller for the Admin Dashboard and features.
 */
@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @GetMapping("/dashboard")
    public String dashboard() {
        return "admin-dashboard"; // Maps to /templates/admin-dashboard.html
    }
}
