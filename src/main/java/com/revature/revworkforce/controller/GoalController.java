package com.revature.revworkforce.controller;

import com.revature.revworkforce.dto.GoalDTO;
import com.revature.revworkforce.dto.GoalStatistics;
import com.revature.revworkforce.enums.Priority;
import com.revature.revworkforce.enums.GoalStatus;
import com.revature.revworkforce.model.Goal;
import com.revature.revworkforce.security.SecurityUtils;
import com.revature.revworkforce.service.GoalService;
import com.revature.revworkforce.service.PerformanceReviewService;
import com.revature.revworkforce.model.PerformanceReview;
import com.revature.revworkforce.dto.PerformanceReviewDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;
import jakarta.transaction.Transactional;

/**
 * Goal Controller - P2 VERSION
 * Handles goal management web endpoints
 * 
 * @author RevWorkForce Team
 */
@Controller
public class GoalController {

    @Autowired
    private GoalService goalService;

    @Autowired
    private PerformanceReviewService reviewService;

    // ============================================
    // EMPLOYEE ENDPOINTS
    // ============================================

    /**
     * P2: View all my goals with status
     */
    @GetMapping({ "/employee/performance", "/employee/goals" })
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'ADMIN')")
    public String viewPerformanceDashboard(Model model) {
        String employeeId = SecurityUtils.getCurrentUsername();

        List<GoalDTO> goals = goalService.getEmployeeGoals(employeeId);

        GoalStatistics stats = goalService.getEmployeeStatistics(employeeId);

        model.addAttribute("goals", goals);
        model.addAttribute("statistics", stats);
        model.addAttribute("goalDTO", new GoalDTO());
        model.addAttribute("priorities", Priority.values());

        List<PerformanceReviewDTO> reviewDTOs = reviewService.getEmployeeReviews(employeeId);

        model.addAttribute("reviews", reviewDTOs);
        model.addAttribute("averageRating", reviewService.getAverageRating(employeeId));
        model.addAttribute("pageTitle", "Performance & Goals");

        return "pages/employee/performance-goals";
    }

    /**
     * P2: View active goals
     */
    @GetMapping("/employee/goals/active")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'ADMIN')")
    public String viewActiveGoals(Model model) {
        String employeeId = SecurityUtils.getCurrentUsername();

        List<GoalDTO> goals = goalService.getActiveGoals(employeeId);

        model.addAttribute("goals", goals);
        model.addAttribute("pageTitle", "Active Goals");

        return "pages/employee/goals-active";
    }

    /**
     * P2: Set goals with description, deadline, priority
     */
    @GetMapping("/employee/goals/create")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'ADMIN')")
    public String showCreateForm(Model model) {
        GoalDTO goalDTO = new GoalDTO();
        goalDTO.setEmployeeId(SecurityUtils.getCurrentUsername());

        model.addAttribute("goalDTO", goalDTO);
        model.addAttribute("priorities", Priority.values());
        model.addAttribute("pageTitle", "Create New Goal");

        return "pages/employee/goal-create";
    }

    /**
     * P2: Process create goal
     */
    @PostMapping("/employee/goals/create")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'ADMIN')")
    public String createGoal(
            @Valid @ModelAttribute("goalDTO") GoalDTO dto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("priorities", Priority.values());
            model.addAttribute("pageTitle", "Create New Goal");
            return "pages/employee/goal-create";
        }

        String employeeId = SecurityUtils.getCurrentUsername();

        try {
            goalService.createGoal(
                    employeeId,
                    dto.getGoalTitle(),
                    dto.getGoalDescription(),
                    dto.getCategory(),
                    dto.getDeadline(),
                    dto.getPriority());

            redirectAttributes.addFlashAttribute("success", "Goal created successfully!");
            return "redirect:/employee/performance";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("priorities", Priority.values());
            model.addAttribute("pageTitle", "Create New Goal");
            return "pages/employee/goal-create";
        }
    }

    /**
     * Edit goal form
     */
    @GetMapping("/employee/goals/edit/{goalId}")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'ADMIN')")
    public String showEditForm(@PathVariable Long goalId, Model model,
            RedirectAttributes redirectAttributes) {
        try {
            GoalDTO dto = goalService.getGoalDTOById(goalId);

            // Check if employee owns this goal
            String currentUser = SecurityUtils.getCurrentUsername();
            if (!dto.getEmployeeId().equals(currentUser)) {
                redirectAttributes.addFlashAttribute("error", "You can only edit your own goals");
                return "redirect:/employee/performance";
            }

            // Check if editable
            if (!dto.isEditable()) {
                redirectAttributes.addFlashAttribute("error",
                        "Cannot edit completed or cancelled goals");
                return "redirect:/employee/performance";
            }

            model.addAttribute("goalDTO", dto);
            model.addAttribute("priorities", Priority.values());
            model.addAttribute("pageTitle", "Edit Goal");

            return "pages/employee/goal-edit";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/employee/performance";
        }
    }

    /**
     * Update goal
     */
    @PostMapping("/employee/goals/edit/{goalId}")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'ADMIN')")
    public String updateGoal(
            @PathVariable Long goalId,
            @Valid @ModelAttribute("goalDTO") GoalDTO dto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("priorities", Priority.values());
            model.addAttribute("pageTitle", "Edit Goal");
            return "pages/employee/goal-edit";
        }

        String employeeId = SecurityUtils.getCurrentUsername();

        try {
            goalService.updateGoal(
                    goalId,
                    employeeId,
                    dto.getGoalTitle(),
                    dto.getGoalDescription(),
                    dto.getCategory(),
                    dto.getDeadline(),
                    dto.getPriority());

            redirectAttributes.addFlashAttribute("success", "Goal updated successfully!");
            return "redirect:/employee/performance";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("priorities", Priority.values());
            model.addAttribute("pageTitle", "Edit Goal");
            return "pages/employee/goal-edit";
        }
    }

    /**
     * P2: Update goal progress
     */
    @GetMapping("/employee/goals/progress/{goalId}")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'ADMIN')")
    public String showProgressForm(@PathVariable Long goalId, Model model,
            RedirectAttributes redirectAttributes) {
        try {
            GoalDTO dto = goalService.getGoalDTOById(goalId);

            // Check ownership
            String currentUser = SecurityUtils.getCurrentUsername();
            if (!dto.getEmployeeId().equals(currentUser)) {
                redirectAttributes.addFlashAttribute("error",
                        "You can only update your own goals");
                return "redirect:/employee/performance";
            }

            model.addAttribute("goalDTO", dto);
            model.addAttribute("pageTitle", "Update Progress");

            return "pages/employee/goal-progress";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/employee/performance";
        }
    }

    /**
     * P2: Process progress update
     */
    @PostMapping("/employee/goals/progress/{goalId}")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'ADMIN')")
    public String updateProgress(
            @PathVariable Long goalId,
            @RequestParam Integer progress,
            RedirectAttributes redirectAttributes) {

        String employeeId = SecurityUtils.getCurrentUsername();

        try {
            goalService.updateProgress(goalId, employeeId, progress);
            redirectAttributes.addFlashAttribute("success",
                    "Progress updated successfully!");
            return "redirect:/employee/performance";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/employee/goals/progress/" + goalId;
        }
    }

    /**
     * View goal details
     */
    @GetMapping("/employee/goals/view/{goalId}")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'ADMIN')")
    public String viewGoalDetails(@PathVariable Long goalId, Model model,
            RedirectAttributes redirectAttributes) {
        try {
            GoalDTO dto = goalService.getGoalDTOById(goalId);

            model.addAttribute("goal", dto);
            model.addAttribute("pageTitle", "Goal Details");

            return "pages/employee/goal-view";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/employee/performance";
        }
    }

    /**
     * Delete goal
     */
    @PostMapping("/employee/goals/delete/{goalId}")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'ADMIN')")
    public String deleteGoal(@PathVariable Long goalId,
            RedirectAttributes redirectAttributes) {
        String employeeId = SecurityUtils.getCurrentUsername();

        try {
            goalService.deleteGoal(goalId, employeeId);
            redirectAttributes.addFlashAttribute("success", "Goal deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/employee/performance";
    }

    /**
     * View upcoming deadlines
     */
    @GetMapping("/employee/goals/upcoming")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'ADMIN')")
    public String viewUpcomingDeadlines(Model model) {
        String employeeId = SecurityUtils.getCurrentUsername();

        List<GoalDTO> goals = goalService.getUpcomingDeadlines(employeeId, 30);

        model.addAttribute("goals", goals);
        model.addAttribute("pageTitle", "Upcoming Deadlines (30 Days)");

        return "pages/employee/goals-upcoming";
    }

    /**
     * View overdue goals
     */
    @GetMapping("/employee/goals/overdue")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'ADMIN')")
    public String viewOverdueGoals(Model model) {
        String employeeId = SecurityUtils.getCurrentUsername();

        List<GoalDTO> goals = goalService.getOverdueGoals(employeeId);

        model.addAttribute("goals", goals);
        model.addAttribute("pageTitle", "Overdue Goals");

        return "pages/employee/goals-overdue";
    }

    // ============================================
    // MANAGER ENDPOINTS
    // ============================================

    /**
     * P2: View team member goal progress
     */
    @GetMapping({ "/manager/goals/performance", "/manager/goals/team", "/manager/reviews/team",
            "/manager/reviews/pending", "/manager/performance" })
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    @Transactional
    public String viewTeamPerformance(Model model) {
        String managerId = SecurityUtils.getCurrentUsername();

        List<GoalDTO> goalDTOs = goalService.getTeamGoals(managerId);

        model.addAttribute("goals", goalDTOs);

        List<PerformanceReviewDTO> reviewDTOs = reviewService.getTeamReviews(managerId);

        model.addAttribute("reviews", reviewDTOs);

        List<PerformanceReviewDTO> pendingReviews = reviewService.getPendingReviewsForManager(managerId);
        model.addAttribute("pendingReviews", pendingReviews);

        model.addAttribute("pageTitle", "Team Performance Review");

        return "pages/manager/performance-review";
    }

    /**
     * P2: Provide comments on goals
     */
    @GetMapping("/manager/goals/comment/{goalId}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public String showCommentForm(@PathVariable Long goalId, Model model,
            RedirectAttributes redirectAttributes) {
        try {
            GoalDTO dto = goalService.getGoalDTOById(goalId);

            model.addAttribute("goalDTO", dto);
            model.addAttribute("pageTitle", "Add Manager Comments");

            return "pages/manager/goal-comment";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/manager/performance";
        }
    }

    /**
     * P2: Submit manager comments
     */
    @PostMapping("/manager/goals/comment/{goalId}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public String submitComments(
            @PathVariable Long goalId,
            @RequestParam String managerComments,
            RedirectAttributes redirectAttributes) {

        String managerId = SecurityUtils.getCurrentUsername();

        try {
            goalService.addManagerComments(goalId, managerId, managerComments);
            redirectAttributes.addFlashAttribute("success",
                    "Comments added successfully!");
            return "redirect:/manager/performance";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/manager/goals/comment/" + goalId;
        }
    }

    /**
     * View team member goal details
     */
    @GetMapping("/manager/goals/view/{goalId}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public String viewTeamGoalDetails(@PathVariable Long goalId, Model model,
            RedirectAttributes redirectAttributes) {
        try {
            GoalDTO dto = goalService.getGoalDTOById(goalId);

            model.addAttribute("goal", dto);
            model.addAttribute("pageTitle", "Goal Details");

            return "pages/manager/goal-view";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/manager/goals/team";
        }
    }
}