package com.revature.revworkforce.controller;

import com.revature.revworkforce.dto.ChangePasswordRequest;
import com.revature.revworkforce.dto.LoginRequest;
import com.revature.revworkforce.exception.AuthenticationException;
import com.revature.revworkforce.exception.ValidationException;
import com.revature.revworkforce.security.SecurityUtils;
import com.revature.revworkforce.service.AuthService;
import com.revature.revworkforce.service.EmployeeService;
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

    @Autowired
    private EmployeeService employeeService;

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
        return "pages/auth/index";
    }

    /**
     * Get current user info for sidebar.
     */
    @GetMapping("/api/auth/me")
    @ResponseBody
    public java.util.Map<String, String> getCurrentUserInfo() {
        String fullName = "Guest";
        String role = "employee";

        if (SecurityUtils.isAuthenticated()) {
            String employeeId = SecurityUtils.getCurrentUsername();
            try {
                com.revature.revworkforce.model.Employee employee = employeeService.getEmployeeById(employeeId);
                if (employee != null) {
                    fullName = employee.getFirstName() + " " + employee.getLastName();
                }
            } catch (Exception e) {
                fullName = employeeId;
            }

            if (SecurityUtils.hasRole("ADMIN")) {
                role = "admin";
            } else if (SecurityUtils.hasRole("MANAGER")) {
                role = "manager";
            }
        }

        java.util.Map<String, String> userInfo = new java.util.HashMap<>();
        userInfo.put("fullName", fullName);
        userInfo.put("role", role);
        return userInfo;
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

        return "pages/auth/reset-password";
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
            return "pages/auth/reset-password";
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

    /**
     * Display forgot password page.
     * 
     * @return forgot password view
     */
    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "pages/auth/forgot-password";
    }

    /**
     * Handle forgot password submission (mocked).
     * 
     * @param email              user email
     * @param redirectAttributes redirect attributes
     * @return redirect to forgot password page
     */
    @PostMapping("/forgot-password/send")
    public String sendForgotPasswordLink(@RequestParam("email") String email, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("success", true);
        return "redirect:/forgot-password";
    }
}