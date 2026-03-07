package com.revature.revworkforce.controller;

import com.revature.revworkforce.model.Employee;
import com.revature.revworkforce.repository.EmployeeRepository;
import com.revature.revworkforce.security.SecurityUtils;
import com.revature.revworkforce.service.LeaveService;
import com.revature.revworkforce.service.NotificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Manager Dashboard Controller.
 * 
 * Handles manager-specific dashboard and operations.
 * Accessible by users with MANAGER or ADMIN role.
 * 
 * @author RevWorkForce Team
 */
@Controller
@RequestMapping("/manager")
@PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
public class ManagerDashboardController {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private LeaveService leaveService;
    @Autowired
    private NotificationService notificationService;

    /**
     * Display manager dashboard.
     * 
     * @param model the model
     * @return manager dashboard view
     */
    @GetMapping("/dashboard")
    public String managerDashboard(Model model) {
        String employeeId = SecurityUtils.getCurrentUsername();

        Employee currentUser = employeeRepository.findById(employeeId).orElse(null);

        if (currentUser != null) {
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("fullName", currentUser.getFullName());

            // Team Size
            List<Employee> teamMembers = employeeRepository.findByManager_EmployeeId(employeeId);
            model.addAttribute("teamSize", teamMembers.size());

            model.addAttribute("pendingCount",
                    leaveService.getPendingLeavesForManager(employeeId).size());
            try {
                model.addAttribute("unreadCount", notificationService.getUnreadCount(employeeId));
            } catch (Exception e) {
                model.addAttribute("unreadCount", 0);
            }
        }

        model.addAttribute("pageTitle", "Manager Dashboard");
        model.addAttribute("currentDashboard", "manager");

        return "pages/manager/dashboard";
    }

    /**
     * Redirect sidebar shortcut /manager/leave-approvals → actual leave controller
     */
    @GetMapping("/leave-approvals")
    public String redirectLeaveApprovals() {
        return "redirect:/leave/manager/leave-approvals";
    }

    /**
     * Display team management page.
     *
     * @param model the model
     * @return team management view
     */
    @GetMapping("/team-management")
    public String teamManagement(Model model) {
        String managerId = SecurityUtils.getCurrentUsername();
        List<com.revature.revworkforce.model.Employee> teamMembers = employeeRepository
                .findByManager_EmployeeId(managerId);
        model.addAttribute("teamMembers", teamMembers);
        model.addAttribute("pageTitle", "Team Management");
        return "pages/manager/team-management";
    }

    /** Redirect /manager/performance → performance review list */
    @GetMapping("/performance")
    public String redirectPerformance() {
        return "redirect:/manager/reviews";
    }

    /** Redirect /manager/goals → team goals view */
    @GetMapping("/goals")
    public String redirectGoals() {
        return "redirect:/goals/manager/team";
    }
}
