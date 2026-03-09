package com.revature.revworkforce.controller;

import com.revature.revworkforce.dto.AnnouncementDTO;
import com.revature.revworkforce.dto.EmployeeDTO;
import com.revature.revworkforce.dto.LeaveBalanceDTO;
import com.revature.revworkforce.model.Employee;
import com.revature.revworkforce.repository.EmployeeRepository;
import com.revature.revworkforce.security.SecurityUtils;
import com.revature.revworkforce.service.AnnouncementService;
import com.revature.revworkforce.service.GoalService;
import com.revature.revworkforce.service.HolidayService;
import com.revature.revworkforce.service.LeaveService;
import com.revature.revworkforce.service.NotificationService;
import com.revature.revworkforce.service.PerformanceReviewService;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
        private final GoalService goalService;
        private final PerformanceReviewService reviewService;
        private final AnnouncementService announcementService;
        private final com.revature.revworkforce.service.EmployeeService employeeService;

        public EmployeeDashboardController(EmployeeRepository employeeRepository,
                        HolidayService holidayService,
                        NotificationService notificationService,
                        LeaveService leaveService,
                        GoalService goalService,
                        PerformanceReviewService reviewService,
                        AnnouncementService announcementService,
                        com.revature.revworkforce.service.EmployeeService employeeService) {
                this.employeeRepository = employeeRepository;
                this.holidayService = holidayService;
                this.notificationService = notificationService;
                this.leaveService = leaveService;
                this.goalService = goalService;
                this.reviewService = reviewService;
                this.announcementService = announcementService;
                this.employeeService = employeeService;
        }

        @GetMapping("/dashboard")
        public String employeeDashboard(Model model) {
                String employeeId = SecurityUtils.getCurrentUsername();
                
                List<AnnouncementDTO> recentAnnouncements = 
                        announcementService.getRecentAnnouncements(3);
                    model.addAttribute("recentAnnouncements", recentAnnouncements);
                    
                // Get current user info
                EmployeeDTO currentUser = employeeService.getEmployeeDTOById(employeeId);
                if (currentUser != null) {
                        model.addAttribute("currentUser", currentUser);
                        model.addAttribute("fullName", currentUser.getFirstName() + " " + currentUser.getLastName());
                        model.addAttribute("designation",
                                        currentUser.getDesignationName() != null
                                                        ? currentUser.getDesignationName()
                                                        : "N/A");
                        model.addAttribute("department",
                                        currentUser.getDepartmentName() != null
                                                        ? currentUser.getDepartmentName()
                                                        : "N/A");
                        // We need the entity for initializeLeaveBalances, or we should update that too.
                        // For now, let's just get the entity specifically for that if needed.
                        Employee currentUserEntity = employeeRepository.findById(employeeId).orElse(null);
                        leaveService.initializeLeaveBalances(currentUserEntity);
                }

                // ===============================
                // Leave Balance
                // ===============================
                int currentYear = LocalDate.now().getYear();

                List<LeaveBalanceDTO> balances = leaveService.getLeaveBalances(employeeId, currentYear);

                int totalRemaining = balances.stream()
                                .mapToInt(LeaveBalanceDTO::getRemainingBalance)
                                .sum();

                model.addAttribute("leaveBalance", totalRemaining);
                model.addAttribute("leaveBalances", balances);

                // Notification count for bell badge
                model.addAttribute("notificationCount",
                                notificationService.getUnreadCount(employeeId));

                // Upcoming holidays and leave history
                model.addAttribute("upcomingHolidays", holidayService.getUpcomingHolidays());
                model.addAttribute("leaveHistory", leaveService.getEmployeeLeaveHistory(employeeId));

                // Active goals
                try {
                        int activeGoals = goalService.getActiveGoals(employeeId).size();
                        model.addAttribute("activeGoals", activeGoals);
                        model.addAttribute("recentGoals", goalService.getEmployeeGoals(employeeId));
                } catch (Exception e) {
                        model.addAttribute("activeGoals", 0);
                        model.addAttribute("recentGoals", List.of());
                }

                // Pending performance review actions
                try {
                        long pendingActions = reviewService.getEmployeeReviews(employeeId).stream()
                                        .filter(r -> "SELF_ASSESSMENT_PENDING".equals(r.getStatus().name()))
                                        .count();
                        model.addAttribute("pendingActions", pendingActions);
                } catch (Exception e) {
                        model.addAttribute("pendingActions", 0);
                }

                // Recent leaves
                try {
                        model.addAttribute("recentLeaves", leaveService.getEmployeeLeaveHistory(employeeId));
                } catch (Exception e) {
                        model.addAttribute("recentLeaves", List.of());
                }

                // Announcements
                try {
                        model.addAttribute("announcements", announcementService.getActiveAnnouncements());
                } catch (Exception e) {
                        model.addAttribute("announcements", List.of());
                }

                // Unread notification count (for sidebar badge)
                try {
                        model.addAttribute("unreadCount", notificationService.getUnreadCount(employeeId));
                } catch (Exception e) {
                        model.addAttribute("unreadCount", 0);
                }

                model.addAttribute("pageTitle", "Employee Dashboard");
                model.addAttribute("currentDashboard", "employee");

                return "pages/employee/dashboard";
        }

      

}