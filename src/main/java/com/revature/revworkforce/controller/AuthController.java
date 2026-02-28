package com.revature.revworkforce.controller;

import com.revature.revworkforce.dto.ChangePasswordRequest;
import com.revature.revworkforce.dto.LoginRequest;
import com.revature.revworkforce.exception.AuthenticationException;
import com.revature.revworkforce.exception.ValidationException;
import com.revature.revworkforce.security.SecurityUtils;
import com.revature.revworkforce.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

/**
 * Authentication Controller.
 * 
 * Handles authentication-related requests:
 * - Login page
 * - Logout
 * - Change password
 * - First-time login
 * 
 * @author RevWorkForce Team
 */
@Controller
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * Display login page.
     * 
     * @param error   login error flag
     * @param logout  logout success flag
     * @param expired session expired flag
     * @param model   the model
     * @return login view
     */
    @GetMapping("/login")
    public String loginPage(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            @RequestParam(value = "expired", required = false) String expired,
            Model model) {

        // If already logged in, redirect to dashboard
        if (SecurityUtils.isAuthenticated()) {
            return "redirect:/dashboard";
        }

        if (error != null) {
            model.addAttribute("error", "Invalid username or password. Please try again.");
        }

        if (logout != null) {
            model.addAttribute("message", "You have been logged out successfully.");
        }

        if (expired != null) {
            model.addAttribute("error", "Your session has expired. Please login again.");
        }
        model.addAttribute("loginRequest", new LoginRequest());
        return "auth/login";
    }

    /**
     * Handle logout.
     * 
     * @param request  HTTP request
     * @param response HTTP response
     * @return redirect to login page
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

        return "redirect:/login?logout=true";
    }

    /**
     * Display change password page.
     * 
     * @param model the model
     * @return change password view
     */
    @GetMapping("/change-password")
    public String changePasswordPage(Model model) {
        model.addAttribute("changePasswordRequest", new ChangePasswordRequest());

        // Check if first login
        String employeeId = SecurityUtils.getCurrentUsername();
        if (employeeId != null && authService.isFirstLogin(employeeId)) {
            model.addAttribute("firstLogin", true);
            model.addAttribute("message", "For security reasons, please change your password on first login.");
        }

        return "auth/change-password";
    }

    /**
     * Handle change password request.
     * 
     * @param request            the change password request
     * @param bindingResult      validation results
     * @param redirectAttributes redirect attributes
     * @return redirect to appropriate page
     */
    @PostMapping("/change-password")
    public String changePassword(
            @Valid @ModelAttribute("changePasswordRequest") ChangePasswordRequest request,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        // Check for validation errors
        if (bindingResult.hasErrors()) {
            return "auth/change-password";
        }

        try {
            String employeeId = SecurityUtils.getCurrentUsername();

            if (employeeId == null) {
                throw new AuthenticationException("User not authenticated");
            }

            // Change password
            authService.changePassword(employeeId, request);

            redirectAttributes.addFlashAttribute("success", "Password changed successfully!");

            // If first login, redirect to dashboard
            return "redirect:/dashboard";

        } catch (ValidationException | AuthenticationException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/change-password";
        }
    }

    /**
     * Handle access denied errors.
     * 
     * @param model the model
     * @return access denied view
     */
    @GetMapping("/error/403")
    public String accessDenied(Model model) {
        model.addAttribute("errorMessage", "You do not have permission to access this page.");
        return "error/403";
    }

    /**
     * Handle not found errors.
     * 
     * @param model the model
     * @return not found view
     */
    @GetMapping("/error/404")
    public String notFound(Model model) {
        model.addAttribute("errorMessage", "The page you are looking for was not found.");
        return "error/404";
    }

    /**
     * Handle internal server errors.
     * 
     * @param model the model
     * @return error view
     */
    @GetMapping("/error/500")
    public String internalError(Model model) {
        model.addAttribute("errorMessage", "An internal server error occurred. Please try again later.");
        return "error/500";
    }
}