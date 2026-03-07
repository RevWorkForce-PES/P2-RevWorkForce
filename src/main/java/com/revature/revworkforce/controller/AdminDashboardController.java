package com.revature.revworkforce.controller;

import com.revature.revworkforce.enums.EmployeeStatus;
import com.revature.revworkforce.model.Employee;
import com.revature.revworkforce.repository.AuditLogRepository;
import com.revature.revworkforce.repository.DepartmentRepository;
import com.revature.revworkforce.repository.EmployeeRepository;
import com.revature.revworkforce.repository.LeaveApplicationRepository;
import com.revature.revworkforce.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Admin Dashboard Controller.
 * 
 * Handles admin-specific dashboard and operations.
 * Only accessible by users with ADMIN role.
 * 
 * @author RevWorkForce Team
 */
@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminDashboardController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Autowired
    private LeaveApplicationRepository leaveApplicationRepository;

    /**
     * Display admin dashboard.
     * 
     * @param model the model
     * @return admin dashboard view
     */
    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        String employeeId = SecurityUtils.getCurrentUsername();

        // Get current user
        Employee currentUser = employeeRepository.findById(employeeId).orElse(null);

        if (currentUser != null) {
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("fullName", currentUser.getFullName());
        }

        // Get statistics
        long totalEmployees = employeeRepository.count();
        long activeEmployees = employeeRepository.countByStatus(EmployeeStatus.ACTIVE);
        long totalDepartments = departmentRepository.count();

        model.addAttribute("totalEmployees", totalEmployees);
        model.addAttribute("activeEmployees", activeEmployees);
        model.addAttribute("totalDepartments", totalDepartments);
        model.addAttribute("pageTitle", "Admin Dashboard");

        return "pages/admin/dashboard";
    }

    @Autowired
    private com.revature.revworkforce.service.DepartmentService departmentService;

    @Autowired
    private com.revature.revworkforce.service.DesignationService designationService;

    @Autowired
    private com.revature.revworkforce.service.HolidayService holidayService;

    @GetMapping("/system-config")
    public String systemConfig(Model model) {
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("designations", designationService.getAllDesignations());
        model.addAttribute("holidays", holidayService.getAllHolidays());
        model.addAttribute("pageTitle", "System Configuration");
        return "pages/admin/system-config";
    }

    @GetMapping("/audit-reports")
    public String auditReports(Model model) {
        try {
            model.addAttribute("auditLogs",
                    auditLogRepository.findAll(
                            PageRequest.of(0, 100, Sort.by(Sort.Direction.DESC, "createdAt"))).getContent());
        } catch (Exception e) {
            model.addAttribute("auditLogs", java.util.List.of());
        }
        try {
            model.addAttribute("leaveReports", leaveApplicationRepository.findAll());
        } catch (Exception e) {
            model.addAttribute("leaveReports", java.util.List.of());
        }
        model.addAttribute("pageTitle", "Audit Reports");
        return "pages/admin/audit-reports";
    }

}