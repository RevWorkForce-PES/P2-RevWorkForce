package com.revature.revworkforce.controller;

import com.revature.revworkforce.dto.PerformanceReviewDTO;
import com.revature.revworkforce.model.PerformanceReview;
import com.revature.revworkforce.security.SecurityUtils;
import com.revature.revworkforce.service.PerformanceReviewService;
import com.revature.revworkforce.service.EmployeeService;
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
    
    // ============================================
    // EMPLOYEE ENDPOINTS
    // ============================================
    
    /**
     * P2: View submitted performance reviews with status
     */
    @GetMapping("/employee/reviews")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'ADMIN')")
    public String viewMyReviews(Model model) {
        String employeeId = SecurityUtils.getCurrentUsername();
        
        List<PerformanceReview> reviews = reviewService.getEmployeeReviews(employeeId);
        List<PerformanceReviewDTO> reviewDTOs = reviews.stream()
            .map(reviewService::convertToDTO)
            .collect(Collectors.toList());
        
        model.addAttribute("reviews", reviewDTOs);
        model.addAttribute("averageRating", reviewService.getAverageRating(employeeId));
        model.addAttribute("pageTitle", "My Performance Reviews");
        
        return "employee/reviews/list";
    }
    
    /**
     * P2: Create performance review document
     */
    @GetMapping("/employee/reviews/create")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'ADMIN')")
    public String showCreateForm(Model model) {
        String employeeId = SecurityUtils.getCurrentUsername();
        
        PerformanceReviewDTO dto = new PerformanceReviewDTO();
        dto.setReviewYear(LocalDate.now().getYear());
        dto.setEmployeeId(employeeId);
        
        model.addAttribute("reviewDTO", dto);
        model.addAttribute("pageTitle", "Create Performance Review");
        
        return "employee/reviews/create";
    }
    
    /**
     * P2: Process create review
     */
    @PostMapping("/employee/reviews/create")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'ADMIN')")
    public String createReview(
            @RequestParam Integer reviewYear,
            RedirectAttributes redirectAttributes) {
        
        String employeeId = SecurityUtils.getCurrentUsername();
        
        try {
            // Check if already exists
            if (reviewService.reviewExists(employeeId, reviewYear)) {
                redirectAttributes.addFlashAttribute("error", 
                    "Review already exists for year " + reviewYear);
                return "redirect:/employee/reviews";
            }
            
            PerformanceReview review = reviewService.createReview(employeeId, reviewYear, employeeId);
            redirectAttributes.addFlashAttribute("success", 
                "Performance review created successfully! Please fill in the details.");
            return "redirect:/employee/reviews/edit/" + review.getReviewId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/employee/reviews/create";
        }
    }
    
    /**
     * P2: Edit review (fill in key deliverables, accomplishments, etc.)
     */
    @GetMapping("/employee/reviews/edit/{reviewId}")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'ADMIN')")
    public String showEditForm(@PathVariable Long reviewId, Model model,
                               RedirectAttributes redirectAttributes) {
        try {
            PerformanceReview review = reviewService.getReviewById(reviewId);
            PerformanceReviewDTO dto = reviewService.convertToDTO(review);
            
            // Check if employee owns this review
            String currentUser = SecurityUtils.getCurrentUsername();
            if (!dto.getEmployeeId().equals(currentUser)) {
                redirectAttributes.addFlashAttribute("error", "You can only edit your own reviews");
                return "redirect:/employee/reviews";
            }
            
            // Check if editable
            if (!dto.isEditable()) {
                redirectAttributes.addFlashAttribute("error", 
                    "Can only edit reviews in DRAFT status");
                return "redirect:/employee/reviews";
            }
            
            model.addAttribute("reviewDTO", dto);
            model.addAttribute("pageTitle", "Edit Performance Review");
            
            return "employee/reviews/form";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/employee/reviews";
        }
    }
    
    /**
     * P2: Update review with key deliverables, accomplishments, areas of improvement, rating
     */
    @PostMapping("/employee/reviews/edit/{reviewId}")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'ADMIN')")
    public String updateReview(
            @PathVariable Long reviewId,
            @Valid @ModelAttribute("reviewDTO") PerformanceReviewDTO dto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            model.addAttribute("pageTitle", "Edit Performance Review");
            return "employee/reviews/form";
        }
        
        String employeeId = SecurityUtils.getCurrentUsername();
        
        try {
            reviewService.updateDraft(
                reviewId,
                employeeId,
                dto.getKeyDeliverables(),
                dto.getMajorAccomplishments(),
                dto.getAreasOfImprovement(),
                dto.getSelfAssessmentRating(),
                dto.getSelfAssessmentComments()
            );
            
            redirectAttributes.addFlashAttribute("success", 
                "Performance review updated successfully!");
            return "redirect:/employee/reviews";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("pageTitle", "Edit Performance Review");
            return "employee/reviews/form";
        }
    }
    
    /**
     * P2: Submit review to manager
     */
    @PostMapping("/employee/reviews/submit/{reviewId}")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'ADMIN')")
    public String submitReview(
            @PathVariable Long reviewId,
            @RequestParam String keyDeliverables,
            @RequestParam String majorAccomplishments,
            @RequestParam String areasOfImprovement,
            @RequestParam BigDecimal selfAssessmentRating,
            @RequestParam(required = false) String selfAssessmentComments,
            RedirectAttributes redirectAttributes) {
        
        String employeeId = SecurityUtils.getCurrentUsername();
        
        try {
            reviewService.submitSelfAssessment(
                reviewId,
                employeeId,
                keyDeliverables,
                majorAccomplishments,
                areasOfImprovement,
                selfAssessmentRating,
                selfAssessmentComments
            );
            
            redirectAttributes.addFlashAttribute("success", 
                "Performance review submitted to manager successfully!");
            return "redirect:/employee/reviews";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/employee/reviews/edit/" + reviewId;
        }
    }
    
    /**
     * P2: View review details (including manager feedback if reviewed)
     */
    @GetMapping("/employee/reviews/view/{reviewId}")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'ADMIN')")
    public String viewReviewDetails(@PathVariable Long reviewId, Model model,
                                    RedirectAttributes redirectAttributes) {
        try {
            PerformanceReview review = reviewService.getReviewById(reviewId);
            PerformanceReviewDTO dto = reviewService.convertToDTO(review);
            
            model.addAttribute("review", dto);
            model.addAttribute("pageTitle", "Review Details");
            
            return "employee/reviews/view";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/employee/reviews";
        }
    }
    
    // ============================================
    // MANAGER ENDPOINTS
    // ============================================
    
    /**
     * P2: View performance reviews submitted by direct reportees
     */
    @GetMapping("/manager/reviews/pending")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public String viewPendingReviews(Model model) {
        String managerId = SecurityUtils.getCurrentUsername();
        
        List<PerformanceReview> pendingReviews = reviewService.getPendingReviewsForManager(managerId);
        List<PerformanceReviewDTO> reviewDTOs = pendingReviews.stream()
            .map(reviewService::convertToDTO)
            .collect(Collectors.toList());
        
        model.addAttribute("reviews", reviewDTOs);
        model.addAttribute("pageTitle", "Pending Reviews");
        
        return "manager/reviews/pending";
    }
    
    /**
     * P2: View all team reviews
     */
    @GetMapping("/manager/reviews/team")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public String viewTeamReviews(Model model) {
        String managerId = SecurityUtils.getCurrentUsername();
        
        List<PerformanceReview> teamReviews = reviewService.getTeamReviews(managerId);
        List<PerformanceReviewDTO> reviewDTOs = teamReviews.stream()
            .map(reviewService::convertToDTO)
            .collect(Collectors.toList());
        
        model.addAttribute("reviews", reviewDTOs);
        model.addAttribute("pageTitle", "Team Performance Reviews");
        
        return "manager/reviews/team";
    }
    
    /**
     * P2: Provide detailed feedback and rate employee performance (1-5)
     */
    @GetMapping("/manager/reviews/review/{reviewId}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public String showManagerReviewForm(@PathVariable Long reviewId, Model model,
                                        RedirectAttributes redirectAttributes) {
        try {
            PerformanceReview review = reviewService.getReviewById(reviewId);
            PerformanceReviewDTO dto = reviewService.convertToDTO(review);
            
            // Check if can review
            if (!dto.canManagerReview()) {
                redirectAttributes.addFlashAttribute("error", 
                    "Can only review SUBMITTED reviews");
                return "redirect:/manager/reviews/pending";
            }
            
            model.addAttribute("reviewDTO", dto);
            model.addAttribute("pageTitle", "Manager Review");
            
            return "manager/reviews/review-form";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/manager/reviews/pending";
        }
    }
    
    /**
     * P2: Submit performance review feedback
     */
    @PostMapping("/manager/reviews/review/{reviewId}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public String submitManagerReview(
            @PathVariable Long reviewId,
            @Valid @ModelAttribute("reviewDTO") PerformanceReviewDTO dto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            model.addAttribute("pageTitle", "Manager Review");
            return "manager/reviews/review-form";
        }
        
        String managerId = SecurityUtils.getCurrentUsername();
        
        try {
            reviewService.submitManagerReview(
                reviewId,
                managerId,
                dto.getManagerFeedback(),
                dto.getManagerRating(),
                dto.getManagerComments()
            );
            
            redirectAttributes.addFlashAttribute("success", 
                "Review feedback submitted successfully!");
            return "redirect:/manager/reviews/pending";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("pageTitle", "Manager Review");
            return "manager/reviews/review-form";
        }
    }
    
    /**
     * View team member review details
     */
    @GetMapping("/manager/reviews/view/{reviewId}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public String viewTeamMemberReview(@PathVariable Long reviewId, Model model,
                                       RedirectAttributes redirectAttributes) {
        try {
            PerformanceReview review = reviewService.getReviewById(reviewId);
            PerformanceReviewDTO dto = reviewService.convertToDTO(review);
            
            model.addAttribute("review", dto);
            model.addAttribute("pageTitle", "Review Details");
            
            return "manager/reviews/view";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/manager/reviews/team";
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
