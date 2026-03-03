package com.revature.revworkforce.controller;

import com.revature.revworkforce.model.Employee;
import com.revature.revworkforce.model.Holiday;
import com.revature.revworkforce.repository.EmployeeRepository;
import com.revature.revworkforce.repository.HolidayRepository;
import com.revature.revworkforce.security.SecurityUtils;
import com.revature.revworkforce.service.HolidayService;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

        @Autowired
        private EmployeeRepository employeeRepository;

        @Autowired
        private HolidayService holidayService;

        @Autowired
        private LeaveService leaveService;

        @Autowired
        private GoalService goalService;

        @Autowired
        private PerformanceReviewService reviewService;

        @GetMapping("/dashboard")
        public String employeeDashboard(Model model) {

                String employeeId = SecurityUtils.getCurrentUsername();

                Employee currentUser = employeeRepository.findById(employeeId).orElse(null);

                if (currentUser != null) {
                        model.addAttribute("fullName", currentUser.getFullName());
                        model.addAttribute("designation",
                                        currentUser.getDesignation() != null
                                                        ? currentUser.getDesignation().getDesignationName()
                                                        : "N/A");
                        model.addAttribute("department",
                                        currentUser.getDepartment() != null
                                                        ? currentUser.getDepartment().getDepartmentName()
                                                        : "N/A");
                }

                // ✅ Use service method
                model.addAttribute("upcomingHolidays",
                                holidayService.getUpcomingHolidays());

                if (employeeId != null) {
                        try {
                                int currentYear = LocalDate.now().getYear();
                                List<LeaveBalanceDTO> balances = leaveService.getLeaveBalances(employeeId, currentYear);
                                double availableLeaves = balances.stream()
                                                .mapToDouble(LeaveBalanceDTO::getRemainingBalance).sum();
                                model.addAttribute("availableLeaves", availableLeaves);
                        } catch (Exception e) {
                                model.addAttribute("availableLeaves", 0);
                        }

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

        /** Redirect /employee/leave-management → actual leave controller */
        @GetMapping("/leave-management")
        public String redirectLeaveManagement() {
                return "redirect:/leave/employee/leave-management";
        }

}