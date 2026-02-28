package com.revature.revworkforce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller for handling authentication-related views.
 */
@Controller
public class AuthController {

    /**
     * Handles GET requests to /login.
     * 
     * @param error   true if there was an authentication error
     * @param logout  true if the user just logged out
     * @param expired true if the user's session expired
     * @param model   to add attributes for the view
     * @return the login view name
     */
    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            @RequestParam(value = "expired", required = false) String expired,
            Model model) {

        if (error != null) {
            model.addAttribute("error", "Invalid username or password.");
        }

        if (logout != null) {
            model.addAttribute("msg", "You have been logged out successfully.");
        }

        if (expired != null) {
            model.addAttribute("msg", "Your session has expired. Please log in again.");
        }

        return "auth/login"; // Resolves to /templates/auth/login.html
    }
}
