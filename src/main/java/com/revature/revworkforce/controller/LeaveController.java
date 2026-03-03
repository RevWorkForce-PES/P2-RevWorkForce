package com.revature.revworkforce.controller;

import com.revature.revworkforce.dto.LeaveApplicationDTO;
import com.revature.revworkforce.repository.LeaveTypeRepository;
import com.revature.revworkforce.service.LeaveService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/leave")
public class LeaveController {

    private final LeaveService leaveService;
    private final LeaveTypeRepository leaveTypeRepository;

    public LeaveController(LeaveService leaveService, LeaveTypeRepository leaveTypeRepository) {
        this.leaveService = leaveService;
        this.leaveTypeRepository = leaveTypeRepository;
    }

    // ================= EMPLOYEE =================

    @GetMapping({ "/employee/apply", "/employee/history", "/employee/balance", "/employee/leave-management" })
    @PreAuthorize("hasAnyRole('EMPLOYEE','MANAGER','ADMIN')")
    public String showApplyPage(Model model, Authentication auth, @RequestParam(required = false) Integer year) {
        String employeeId = auth.getName();
        if (year == null) {
            year = java.time.LocalDate.now().getYear();
        }

        model.addAttribute("leaveApplicationDTO", new LeaveApplicationDTO());
        model.addAttribute("balances", leaveService.getLeaveBalances(employeeId, java.time.LocalDate.now().getYear()));
        model.addAttribute("history", leaveService.getEmployeeLeaveHistory(employeeId));
        model.addAttribute("leaveTypes", leaveTypeRepository.findAll());
        model.addAttribute("selectedYear", year);

        return "pages/employee/leave-management";
    }

    @PostMapping("/employee/apply")
    @PreAuthorize("hasAnyRole('EMPLOYEE','MANAGER','ADMIN')")
    public String applyLeave(@ModelAttribute LeaveApplicationDTO dto,
            Authentication auth) {

        leaveService.applyLeave(dto, auth.getName());
        return "redirect:/leave/employee/leave-management";
    }

    // History endpoint merged into leave-management

    @PostMapping("/employee/cancel/{id}")
    @PreAuthorize("hasAnyRole('EMPLOYEE','MANAGER','ADMIN')")
    public String cancel(@PathVariable Long id, Authentication auth) {

        leaveService.cancelLeave(id, auth.getName());
        return "redirect:/leave/employee/leave-management";
    }

    // Balance endpoint merged into leave-management

    // ================= MANAGER =================

    @GetMapping({ "/manager/pending", "/manager/team", "/manager/leave-approvals" })
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public String showPendingLeaves(Model model, Authentication auth) {

        String managerId = auth.getName();

        model.addAttribute("pending", leaveService.getPendingLeavesForManager(managerId));
        model.addAttribute("team", leaveService.getTeamLeaves(managerId));

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

        return "redirect:/leave/manager/leave-approvals";
    }

    @PostMapping("/manager/reject/{id}")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public String reject(@PathVariable Long id,
            @RequestParam String rejectionReason,
            Authentication authentication) {

        leaveService.rejectLeave(id,
                authentication.getName(),
                rejectionReason);

        return "redirect:/leave/manager/leave-approvals";
    }

}