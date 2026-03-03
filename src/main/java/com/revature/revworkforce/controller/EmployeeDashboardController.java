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

    @GetMapping("/dashboard")
    public String employeeDashboard(Model model) {

        String employeeId = SecurityUtils.getCurrentUsername();

        Employee currentUser =
                employeeRepository.findById(employeeId).orElse(null);

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

        model.addAttribute("pageTitle", "Employee Dashboard");

        return "employee/dashboard";
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
    }
}