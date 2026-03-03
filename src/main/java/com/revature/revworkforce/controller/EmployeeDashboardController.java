package com.revature.revworkforce.controller;

import com.revature.revworkforce.dto.LeaveBalanceDTO;
import com.revature.revworkforce.model.Employee;
import com.revature.revworkforce.repository.EmployeeRepository;
import com.revature.revworkforce.security.SecurityUtils;
import com.revature.revworkforce.service.HolidayService;
import com.revature.revworkforce.service.LeaveService;
import com.revature.revworkforce.service.NotificationService;

import java.time.LocalDate;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.revature.revworkforce.service.LeaveService;
import com.revature.revworkforce.service.GoalService;
import com.revature.revworkforce.service.PerformanceReviewService;
import com.revature.revworkforce.dto.LeaveBalanceDTO;
import java.math.BigDecimal;

/**
 * Employee Dashboard Controller.
 * 
 * Handles employee self-service dashboard and operations.
 * Accessible by all authenticated users.
 * 
 * @author RevWorkForce Team
 */
@Controller
@RequestMapping("/employee")
@PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'ADMIN')")
public class EmployeeDashboardController {
	private final LeaveService leaveService;
    private final EmployeeRepository employeeRepository;
    private final HolidayService holidayService;
    private final NotificationService notificationService;
   
    public EmployeeDashboardController(EmployeeRepository employeeRepository,
            HolidayService holidayService,
            NotificationService notificationService,
            LeaveService leaveService) {

this.employeeRepository = employeeRepository;
this.holidayService = holidayService;
this.notificationService = notificationService;
this.leaveService = leaveService;
}

        @Autowired
        private LeaveService leaveService;

        @Autowired
        private GoalService goalService;

        @Autowired
        private PerformanceReviewService reviewService;

        if (currentUser != null) {
            model.addAttribute("fullName", currentUser.getFullName());
            model.addAttribute("designation",
                    currentUser.getDesignation() != null ?
                            currentUser.getDesignation().getDesignationName() : "N/A");
            model.addAttribute("department",
                    currentUser.getDepartment() != null ?
                            currentUser.getDepartment().getDepartmentName() : "N/A");
            leaveService.initializeLeaveBalances(currentUser);
        }

        // ===============================
        // Leave Balance
        // ===============================

        int currentYear = java.time.LocalDate.now().getYear();

        int totalRemaining = leaveService
                .getLeaveBalances(employeeId, currentYear)
                .stream()
                .mapToInt(lb -> lb.getRemainingBalance())
                .sum();

        model.addAttribute("leaveBalance", totalRemaining);

        // ✅ Add Notification Count
        model.addAttribute("notificationCount",
                notificationService.getUnreadCount(employeeId));

        // ✅ Holidays
        model.addAttribute("upcomingHolidays",
                holidayService.getUpcomingHolidays());
        model.addAttribute("leaveHistory",
                leaveService.getEmployeeLeaveHistory(employeeId));

                        try {
                                int activeGoals = goalService.getActiveGoals(employeeId).size();
                                model.addAttribute("activeGoals", activeGoals);
                                model.addAttribute("recentGoals", goalService.getEmployeeGoals(employeeId));
                        } catch (Exception e) {
                                model.addAttribute("activeGoals", 0);
                                model.addAttribute("recentGoals", List.of());
                        }

                        try {
                                long pendingActions = reviewService.getEmployeeReviews(employeeId).stream()
                                                .filter(r -> "SELF_ASSESSMENT_PENDING".equals(r.getStatus().name()))
                                                .count();
                                model.addAttribute("pendingActions", pendingActions);
                        } catch (Exception e) {
                                model.addAttribute("pendingActions", 0);
                        }

                        try {
                                model.addAttribute("recentLeaves", leaveService.getEmployeeLeaveHistory(employeeId));
                        } catch (Exception e) {
                                model.addAttribute("recentLeaves", List.of());
                        }
                }

                model.addAttribute("pageTitle", "Employee Dashboard");

                return "pages/employee/dashboard";
        }

        return "employee/dashboard";    
        
    }
}