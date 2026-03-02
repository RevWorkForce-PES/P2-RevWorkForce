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
    private HolidayService holidayService;  // ✅ use service

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
        }

        // ✅ Use service method
        model.addAttribute("upcomingHolidays",
                holidayService.getUpcomingHolidays());

        model.addAttribute("pageTitle", "Employee Dashboard");

        return "employee/dashboard";
        }

}