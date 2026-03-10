package com.revature.revworkforce.controller;

import com.revature.revworkforce.dto.ChangePasswordRequest;
import com.revature.revworkforce.dto.LoginRequest;
import com.revature.revworkforce.enums.AuditAction;
import com.revature.revworkforce.exception.AuthenticationException;
import com.revature.revworkforce.exception.ResourceNotFoundException;
import com.revature.revworkforce.exception.ValidationException;
import com.revature.revworkforce.model.Employee;
import com.revature.revworkforce.repository.EmployeeRepository;
import com.revature.revworkforce.security.SecurityUtils;
import com.revature.revworkforce.service.AuditService;
import com.revature.revworkforce.service.AuthService;
import com.revature.revworkforce.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AuditService auditService;

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
            @RequestParam(value = "nosplash", required = false) String nosplash,
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

        // If no parameter forces bypassing the splash screen, show the splash screen
        if (error == null && logout == null && expired == null && nosplash == null) {
            return "pages/auth/splash";
        }

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

    // Show forgot password page
    @GetMapping("/forgot-password")
    public String showForgotPasswordPage(Model model) {
        model.addAttribute("pageTitle", "Forgot Password");
        return "pages/auth/forgot-password";
    }

    // Verify email
    @PostMapping("/forgot-password/verify-email")
    public String verifyEmail(
            @RequestParam("email") String email,
            Model model) {

        try {
            Employee employee = employeeRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("Employee", "email", email));

            if (employee.getSecurityQuestion1() == null || employee.getSecurityQuestion2() == null) {
                model.addAttribute("error", "Security questions not set. Please contact administrator.");
                return "pages/auth/forgot-password";
            }

            model.addAttribute("email", email);
            model.addAttribute("question1", employee.getSecurityQuestion1());
            model.addAttribute("question2", employee.getSecurityQuestion2());
            model.addAttribute("pageTitle", "Security Questions");

            return "pages/auth/security-questions";

        } catch (ResourceNotFoundException e) {
            model.addAttribute("error", "Email not found in system.");
            return "pages/auth/forgot-password";
        }
    }

    // Verify security answers
    @PostMapping("/forgot-password/verify-answers")
    public String verifySecurityAnswers(
            @RequestParam("email") String email,
            @RequestParam("answer1") String answer1,
            @RequestParam("answer2") String answer2,
            Model model) {

        try {
            Employee employee = employeeRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("Employee", "email", email));

            // Verify answers (case-insensitive)
            boolean answer1Match = passwordEncoder.matches(
                    answer1.toLowerCase().trim(),
                    employee.getSecurityAnswer1());

            boolean answer2Match = passwordEncoder.matches(
                    answer2.toLowerCase().trim(),
                    employee.getSecurityAnswer2());

            if (!answer1Match || !answer2Match) {
                model.addAttribute("error", "Security answers do not match. Please try again.");
                model.addAttribute("email", email);
                model.addAttribute("question1", employee.getSecurityQuestion1());
                model.addAttribute("question2", employee.getSecurityQuestion2());
                return "pages/auth/security-questions";
            }

            // Answers correct
            model.addAttribute("email", email);
            model.addAttribute("pageTitle", "Reset Password");
            return "pages/auth/login-reset-password";

        } catch (ResourceNotFoundException e) {
            model.addAttribute("error", "Invalid request.");
            return "pages/auth/forgot-password";
        }
    }

    // Reset password
    @PostMapping("/forgot-password/reset")
    public String resetPassword(
            @RequestParam("email") String email,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match.");
            model.addAttribute("email", email);
            return "pages/auth/reset-password";
        }

        if (newPassword.length() < 8) {
            model.addAttribute("error", "Password must be at least 8 characters long.");
            model.addAttribute("email", email);
            return "pages/auth/reset-password";
        }

        try {
            Employee employee = employeeRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("Employee", "email", email));

            employee.setPasswordHash(passwordEncoder.encode(newPassword));
            employee.setFirstLogin('N');
            employeeRepository.save(employee);

            auditService.logAction(
                    employee.getEmployeeId(),
                    AuditAction.PASSWORD_RESET,
                    "EMPLOYEES",
                    employee.getEmployeeId(),
                    null,
                    "Password reset via security questions");

            redirectAttributes.addFlashAttribute("success",
                    "Password reset successfully! Please login with your new password.");
            return "redirect:/login";

        } catch (ResourceNotFoundException e) {
            model.addAttribute("error", "Invalid request.");
            return "pages/auth/forgot-password";
        }
    }
}