package com.revature.revworkforce.controller;

import com.revature.revworkforce.dto.PerformanceReviewDTO;
import com.revature.revworkforce.enums.ReviewStatus;
import com.revature.revworkforce.model.Employee;
import com.revature.revworkforce.model.PerformanceReview;
import com.revature.revworkforce.security.SecurityUtils;
import com.revature.revworkforce.service.PerformanceReviewService;
import com.revature.revworkforce.service.EmployeeService;
import com.revature.revworkforce.service.GoalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Performance Review Controller
 * Handles performance review web endpoints
 * 
 * @author RevWorkForce Team
 */
@Controller
public class PerformanceReviewController {

    @Autowired
    private PerformanceReviewService reviewService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private GoalService goalService;

    // ============================================
    // EMPLOYEE ENDPOINTS
    // ============================================

    /**
     * View submitted performance reviews and average rating
     */
    @GetMapping("/employee/reviews")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public String viewMyReviews(Model model) {
        String employeeId = SecurityUtils.getCurrentUsername();

        List<PerformanceReviewDTO> reviews = reviewService.getEmployeeReviews(employeeId);

        model.addAttribute("reviews", reviews);
        model.addAttribute("averageRating", reviewService.getAverageRating(employeeId));
        model.addAttribute("pageTitle", "My Performance Reviews");

        // Sidebar/Navbar details
        try {
            Employee employee = employeeService.getEmployeeById(employeeId);
            model.addAttribute("fullName", employee.getFullName());
        } catch (Exception e) {
            model.addAttribute("fullName", "User");
        }
        model.addAttribute("userRole", "EMPLOYEE");

        return "pages/employee/performance-goals";
    }

    /**
     * Self-assessment form
     */
    @GetMapping("/employee/reviews/self-assessment/{reviewId}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public String showSelfAssessmentForm(@PathVariable Long reviewId, Model model,
            RedirectAttributes redirectAttributes) {
        try {
            PerformanceReviewDTO dto = reviewService.getReviewDTOById(reviewId);

            // Check if employee owns this review
            String currentUser = SecurityUtils.getCurrentUsername();
            if (!dto.getEmployeeId().equals(currentUser)) {
                redirectAttributes.addFlashAttribute("error", "You can only assess your own reviews");
                return "redirect:/employee/reviews";
            }

            // Check status
            if (dto.getStatus() != ReviewStatus.PENDING_SELF_ASSESSMENT) {
                redirectAttributes.addFlashAttribute("error",
                        "Self-assessment can only be submitted for reviews in PENDING_SELF_ASSESSMENT status");
                return "redirect:/employee/reviews";
            }

            model.addAttribute("reviewDTO", dto);
            model.addAttribute("pageTitle", "Employee Self-Assessment");

            // Sidebar/Navbar details
            try {
                Employee employee = employeeService.getEmployeeById(currentUser);
                model.addAttribute("fullName", employee.getFullName());
            } catch (Exception e) {
                model.addAttribute("fullName", "User");
            }
            model.addAttribute("userRole", "EMPLOYEE");

            return "pages/employee/self-assessment";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/employee/reviews";
        }
    }

    /**
     * Submit self-assessment
     */
    @PostMapping("/employee/reviews/self-assessment/{reviewId}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public String submitSelfAssessment(
            @PathVariable Long reviewId,
            @Valid @ModelAttribute("reviewDTO") PerformanceReviewDTO dto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("error", "Please correct the errors below and try again.");
            model.addAttribute("pageTitle", "Employee Self-Assessment");
            String employeeId = SecurityUtils.getCurrentUsername();
            try {
                Employee employee = employeeService.getEmployeeById(employeeId);
                model.addAttribute("fullName", employee.getFullName());
            } catch (Exception e) {
                model.addAttribute("fullName", "User");
            }
            model.addAttribute("userRole", "EMPLOYEE");
            return "pages/employee/self-assessment";
        }

        String employeeId = SecurityUtils.getCurrentUsername();

        try {
            reviewService.submitSelfAssessment(reviewId, dto, employeeId);
            redirectAttributes.addFlashAttribute("success", "Self-assessment submitted successfully!");
            return "redirect:/employee/reviews";
        } catch (Exception e) {
            model.addAttribute("reviewDTO", dto); // Explicitly ensure it's there
            model.addAttribute("error", e.getMessage());
            model.addAttribute("pageTitle", "Employee Self-Assessment");
            try {
                Employee employee = employeeService.getEmployeeById(employeeId);
                model.addAttribute("fullName", employee.getFullName());
            } catch (Exception ex) {
                model.addAttribute("fullName", "User");
            }
            model.addAttribute("userRole", "EMPLOYEE");
            return "pages/employee/self-assessment";
        }
    }

    // ============================================
    // MANAGER ENDPOINTS
    // ============================================

    /**
     * View team reviews
     */
    @GetMapping("/manager/reviews")
    @PreAuthorize("hasRole('MANAGER')")
    public String viewTeamReviews(Model model) {
        String managerId = SecurityUtils.getCurrentUsername();
        List<PerformanceReviewDTO> reviews = reviewService.getTeamReviews(managerId);

        model.addAttribute("reviews", reviews);
        model.addAttribute("goals", goalService.getTeamGoals(managerId));
        model.addAttribute("performanceStats", reviewService.getTeamPerformanceStats(managerId));
        model.addAttribute("goalStats", goalService.getTeamStatistics(managerId));
        model.addAttribute("pageTitle", "Team Performance Reviews");

        // Sidebar/Navbar details
        try {
            Employee manager = employeeService.getEmployeeById(managerId);
            model.addAttribute("fullName", manager.getFullName());
        } catch (Exception e) {
            model.addAttribute("fullName", "Manager");
        }
        model.addAttribute("userRole", "MANAGER");

        return "pages/manager/performance-review";
    }

    /**
     * View pending reviews
     */
    @GetMapping("/manager/reviews/pending")
    @PreAuthorize("hasRole('MANAGER')")
    public String viewPendingReviews(Model model) {
        String managerId = SecurityUtils.getCurrentUsername();
        List<PerformanceReviewDTO> reviews = reviewService.getPendingReviewsForManager(managerId);

        model.addAttribute("reviews", reviews);
        model.addAttribute("pageTitle", "Pending Team Reviews");

        // Sidebar/Navbar details
        try {
            Employee manager = employeeService.getEmployeeById(managerId);
            model.addAttribute("fullName", manager.getFullName());
        } catch (Exception e) {
            model.addAttribute("fullName", "Manager");
        }
        model.addAttribute("userRole", "MANAGER");

        return "pages/manager/pending-reviews";
    }

    /**
     * Create review form
     */
    @GetMapping("/manager/reviews/create")
    @PreAuthorize("hasRole('MANAGER')")
    public String showCreateReviewForm(Model model) {
        String managerId = SecurityUtils.getCurrentUsername();
        PerformanceReviewDTO dto = new PerformanceReviewDTO();
        dto.setReviewYear(LocalDate.now().getYear());

        model.addAttribute("teamMembers", employeeService.getTeamMembers(managerId));
        model.addAttribute("reviewDTO", dto);
        model.addAttribute("pageTitle", "Create Performance Review");

        // Sidebar/Navbar details
        try {
            Employee manager = employeeService.getEmployeeById(managerId);
            model.addAttribute("fullName", manager.getFullName());
        } catch (Exception e) {
            model.addAttribute("fullName", "Manager");
        }
        model.addAttribute("userRole", "MANAGER");

        return "pages/manager/review-create";
    }

    /**
     * Create review
     */
    @PostMapping("/manager/reviews/create")
    @PreAuthorize("hasRole('MANAGER')")
    public String createReview(
            @Valid @ModelAttribute("reviewDTO") PerformanceReviewDTO dto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        String managerId = SecurityUtils.getCurrentUsername();

        if (result.hasFieldErrors("employeeId") || result.hasFieldErrors("reviewYear")) {
            model.addAttribute("pageTitle", "Create Performance Review");
            model.addAttribute("teamMembers", employeeService.getTeamMembers(managerId)); // Re-add team members on
                                                                                          // error
            try {
                Employee manager = employeeService.getEmployeeById(managerId);
                model.addAttribute("fullName", manager.getFullName());
            } catch (Exception e) {
                model.addAttribute("fullName", "Manager");
            }
            model.addAttribute("userRole", "MANAGER");
            return "pages/manager/review-create";
        }

        try {
            reviewService.createReview(dto, managerId);
            redirectAttributes.addFlashAttribute("success", "Performance review created successfully!");
            return "redirect:/manager/reviews";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("pageTitle", "Create Performance Review");
            model.addAttribute("teamMembers", employeeService.getTeamMembers(managerId)); // Re-add team members on
                                                                                          // error
            try {
                Employee manager = employeeService.getEmployeeById(managerId);
                model.addAttribute("fullName", manager.getFullName());
            } catch (Exception ex) {
                model.addAttribute("fullName", "Manager");
            }
            model.addAttribute("userRole", "MANAGER");
            return "pages/manager/review-create";
        }
    }

    /**
     * Manager review form
     */
    @GetMapping("/manager/reviews/review/{reviewId}")
    @PreAuthorize("hasRole('MANAGER')")
    public String showManagerReviewForm(@PathVariable Long reviewId, Model model,
            RedirectAttributes redirectAttributes) {
        try {
            PerformanceReviewDTO dto = reviewService.getReviewDTOById(reviewId);

            if (dto.getStatus() != ReviewStatus.PENDING_MANAGER_REVIEW) {
                redirectAttributes.addFlashAttribute("error",
                        "Review can only be completed in PENDING_MANAGER_REVIEW status");
                return "redirect:/manager/reviews";
            }

            model.addAttribute("reviewDTO", dto);
            model.addAttribute("pageTitle", "Manager Evaluation");

            // Sidebar/Navbar details
            String managerId = SecurityUtils.getCurrentUsername();
            try {
                Employee manager = employeeService.getEmployeeById(managerId);
                model.addAttribute("fullName", manager.getFullName());
            } catch (Exception e) {
                model.addAttribute("fullName", "Manager");
            }
            model.addAttribute("userRole", "MANAGER");

            return "pages/manager/review-form";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/manager/reviews";
        }
    }

    /**
     * Submit manager review
     */
    @PostMapping("/manager/reviews/review/{reviewId}")
    @PreAuthorize("hasRole('MANAGER')")
    public String submitManagerReview(
            @PathVariable Long reviewId,
            @Valid @ModelAttribute("reviewDTO") PerformanceReviewDTO dto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        String managerId = SecurityUtils.getCurrentUsername();

        if (result.hasErrors()) {
            model.addAttribute("pageTitle", "Manager Evaluation");
            try {
                Employee manager = employeeService.getEmployeeById(managerId);
                model.addAttribute("fullName", manager.getFullName());
            } catch (Exception e) {
                model.addAttribute("fullName", "Manager");
            }
            model.addAttribute("userRole", "MANAGER");
            return "pages/manager/review-form";
        }

        try {
            reviewService.submitManagerReview(reviewId, dto, managerId);
            redirectAttributes.addFlashAttribute("success", "Manager evaluation submitted successfully!");
            return "redirect:/manager/reviews";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("pageTitle", "Manager Evaluation");
            try {
                Employee manager = employeeService.getEmployeeById(managerId);
                model.addAttribute("fullName", manager.getFullName());
            } catch (Exception ex) {
                model.addAttribute("fullName", "Manager");
            }
            model.addAttribute("userRole", "MANAGER");
            return "pages/manager/review-form";
        }
    }

    /**
     * View detailed review
     */
    @GetMapping("/manager/reviews/view/{reviewId}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN', 'EMPLOYEE')")
    public String viewDetailedReview(@PathVariable Long reviewId, Model model,
            RedirectAttributes redirectAttributes) {
        try {
            PerformanceReviewDTO dto = reviewService.getReviewDTOById(reviewId);
            String currentUser = SecurityUtils.getCurrentUsername();

            // Allow access if user is the employee OR a manager/admin
            boolean isOwner = dto.getEmployeeId().equals(currentUser);
            boolean isManagerOrAdmin = SecurityUtils.hasRole("MANAGER") || SecurityUtils.hasRole("ADMIN");

            if (!isOwner && !isManagerOrAdmin) {
                redirectAttributes.addFlashAttribute("error", "You can only view your own performance records");
                return "redirect:/employee/performance";
            }

            model.addAttribute("review", dto);
            model.addAttribute("pageTitle", "Review Details");

            // Sidebar/Navbar details
            String userId = SecurityUtils.getCurrentUsername();
            String userRole = "USER"; // Default role
            try {
                Employee user = employeeService.getEmployeeById(userId);
                model.addAttribute("fullName", user.getFullName());
                if (SecurityUtils.hasRole("MANAGER")) {
                    userRole = "MANAGER";
                } else if (SecurityUtils.hasRole("ADMIN")) {
                    userRole = "ADMIN";
                } else if (SecurityUtils.hasRole("EMPLOYEE")) {
                    userRole = "EMPLOYEE";
                }
            } catch (Exception e) {
                model.addAttribute("fullName", "User");
            }
            model.addAttribute("userRole", userRole);

            return "pages/manager/review-view";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/manager/reviews";
        }
    }

    // ============================================
    // ADMIN ENDPOINTS (if needed)
    // ============================================

    /**
     * Delete review (admin only)
     */
    @PostMapping("/admin/reviews/delete/{reviewId}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteReview(@PathVariable Long reviewId,
            RedirectAttributes redirectAttributes) {
        try {
            reviewService.deleteReview(reviewId);
            redirectAttributes.addFlashAttribute("success", "Review deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/admin/reviews";
    }
}
