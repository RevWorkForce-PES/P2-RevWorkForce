package com.revature.revworkforce.controller;

import com.revature.revworkforce.dto.LeaveApplicationDTO;
import com.revature.revworkforce.model.LeaveBalance;
import com.revature.revworkforce.repository.EmployeeRepository;
import com.revature.revworkforce.repository.LeaveTypeRepository;
import com.revature.revworkforce.service.HolidayService;
import com.revature.revworkforce.service.LeaveService;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LeaveController {
    private final HolidayService holidayService;
    private final LeaveService leaveService;
    private final LeaveTypeRepository leaveTypeRepository;
    private final EmployeeRepository employeeRepository;

    public LeaveController(
            LeaveService leaveService,
            LeaveTypeRepository leaveTypeRepository,
            HolidayService holidayService,
            EmployeeRepository employeeRepository) {

        this.leaveService = leaveService;
        this.leaveTypeRepository = leaveTypeRepository;
        this.holidayService = holidayService;
        this.employeeRepository = employeeRepository;
    }

    // ================= EMPLOYEE =================

    // ================= EMPLOYEE =================

    @GetMapping({ "/employee/apply", "/employee/history", "/employee/balance", "/employee/leave-management" })
    public String showApplyPage(Model model, Authentication auth,
            @RequestParam(required = false) Integer year) {

        String employeeId = auth.getName();

        // Get employee
        var employee = employeeRepository.findById(employeeId).orElse(null);

        String managerName = "N/A";

        // Initialize leave balances
        if (employee != null) {
            leaveService.initializeLeaveBalances(employee);

            if (employee.getManager() != null) {
                managerName = employee.getManager().getFullName();
            }
        }

        model.addAttribute("managerName", managerName);

        // Set current year if null
        if (year == null) {
            year = java.time.LocalDate.now().getYear();
        }

        // Get balances
        var balances = leaveService.getLeaveBalances(employeeId, year);

        model.addAttribute("leaveApplicationDTO", new LeaveApplicationDTO());
        model.addAttribute("balances", balances);
        model.addAttribute("leaveTypes", leaveTypeRepository.findAll());
        model.addAttribute("history", leaveService.getEmployeeLeaveHistory(employeeId));
        model.addAttribute("holidays", holidayService.getHolidaysByYear(year));
        model.addAttribute("selectedYear", year);

        // Year dropdown
        java.util.List<Integer> years = java.util.stream.IntStream
                .rangeClosed(year - 2, year + 3)
                .boxed()
                .toList();

        model.addAttribute("years", years);

        return "pages/employee/leave-management";
    }

    @PostMapping("/employee/apply")
    @PreAuthorize("hasAnyRole('EMPLOYEE','MANAGER','ADMIN')")
    public String applyLeave(@ModelAttribute LeaveApplicationDTO dto,
            Authentication auth) {

        leaveService.applyLeave(dto, auth.getName());
        return "redirect:/employee/leave-management";
    }

    // History endpoint merged into leave-management

    @PostMapping("/employee/cancel/{id}")
    @PreAuthorize("hasAnyRole('EMPLOYEE','MANAGER','ADMIN')")
    public String cancel(@PathVariable Long id, Authentication auth) {

        leaveService.cancelLeave(id, auth.getName());
        return "redirect:/employee/leave-management";
    }

    // Balance endpoint merged into leave-management

    // ================= MANAGER =================
    @GetMapping({ "/manager/pending", "/manager/team", "/manager/leave-approvals" })
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public String showPendingLeaves(Model model, Authentication auth) {

        String managerId = auth.getName();

        model.addAttribute("pending",
                leaveService.getPendingLeavesForManager(managerId));

        model.addAttribute("team",
                leaveService.getTeamLeaves(managerId));

        model.addAttribute("holidays",
                holidayService.getAllActiveHolidays());

        List<LeaveBalance> balances = leaveService.findByEmployee_Manager_EmployeeIdAndYear(
                managerId,
                LocalDate.now().getYear());

        model.addAttribute("teamBalances", balances);

        model.addAttribute("teamMembers",
                employeeRepository.findByManager_EmployeeId(managerId));

        return "pages/manager/leave-approvals";
    }

    // Team endpoint merged into leave-approvals

    @GetMapping("/manager/review/{id}")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public String reviewLeave(@PathVariable Long id,
            Model model,
            Authentication auth) {

        model.addAttribute("leave",
                leaveService.getLeaveById(id));

        return "manager/leave/review";
    }

    @PostMapping("/manager/approve/{id}")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public String approve(@PathVariable Long id,
            @RequestParam(required = false) String comments,
            Authentication authentication) {

        leaveService.approveLeave(id,
                authentication.getName(),
                comments);

        return "redirect:/manager/leave-approvals";
    }

    @PostMapping("/manager/reject/{id}")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public String reject(@PathVariable Long id,
            @RequestParam String rejectionReason,
            Authentication authentication) {

        leaveService.rejectLeave(id,
                authentication.getName(),
                rejectionReason);

        return "redirect:/manager/leave-approvals";
    }

    // ================= ADMIN =================

}