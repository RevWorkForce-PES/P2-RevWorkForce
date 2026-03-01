package com.revature.revworkforce.controller;

import com.revature.revworkforce.dto.LeaveApplicationDTO;
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

    public LeaveController(LeaveService leaveService) {
        this.leaveService = leaveService;
    }

    // ================= EMPLOYEE =================

    @GetMapping("/employee/apply")
    @PreAuthorize("hasAnyRole('EMPLOYEE')")
    public String showApplyPage(Model model, Authentication auth) {

        String employeeId = auth.getName();

        model.addAttribute("leaveApplicationDTO", new LeaveApplicationDTO());
        model.addAttribute("balances",
                leaveService.getLeaveBalances(employeeId,
                        java.time.LocalDate.now().getYear()));

        return "employee/leave/apply";
    }

    @PostMapping("/employee/apply")
    @PreAuthorize("hasAnyRole('EMPLOYEE')")
    public String applyLeave(@ModelAttribute LeaveApplicationDTO dto,
                             Authentication auth) {

        leaveService.applyLeave(dto, auth.getName());
        return "redirect:/leave/employee/history";
    }

    @GetMapping("/employee/history")
    @PreAuthorize("hasAnyRole('EMPLOYEE')")
    public String showHistory(Model model, Authentication auth) {

        model.addAttribute("history",
                leaveService.getEmployeeLeaveHistory(auth.getName()));

        return "employee/leave/history";
    }

    @PostMapping("/employee/cancel/{id}")
    @PreAuthorize("hasAnyRole('EMPLOYEE')")
    public String cancel(@PathVariable Long id, Authentication auth) {

        leaveService.cancelLeave(id, auth.getName());
        return "redirect:/leave/employee/history";
    }

    @GetMapping("/employee/balance")
    @PreAuthorize("hasAnyRole('EMPLOYEE')")
    public String showBalance(Model model,
                              Authentication auth,
                              @RequestParam(required = false) Integer year) {

        if (year == null) {
            year = java.time.LocalDate.now().getYear();
        }

        model.addAttribute("balances",
                leaveService.getLeaveBalances(auth.getName(), year));

        model.addAttribute("selectedYear", year);

        return "employee/leave/balance";
    }
    // ================= MANAGER =================

    @GetMapping("/manager")
    @PreAuthorize("hasAuthority('MANAGER')")
    public String managerLeavePage(Model model,
                                   Authentication authentication) {

        String managerId = authentication.getName();

        model.addAttribute("pending",
                leaveService.getPendingLeavesForManager(managerId));

        model.addAttribute("team",
                leaveService.getTeamLeaves(managerId));

        return "manager/leave";
    }

    @PostMapping("/manager/approve/{id}")
    @PreAuthorize("hasAuthority('MANAGER')")
    public String approve(@PathVariable Long id,
                          @RequestParam(required = false) String comments,
                          Authentication authentication) {

        leaveService.approveLeave(id,
                authentication.getName(),
                comments);

        return "redirect:/leave/manager";
    }

    @PostMapping("/manager/reject/{id}")
    @PreAuthorize("hasAuthority('MANAGER')")
    public String reject(@PathVariable Long id,
                         @RequestParam String rejectionReason,
                         Authentication authentication) {

        leaveService.rejectLeave(id,
                authentication.getName(),
                rejectionReason);

        return "redirect:/leave/manager";
    }
}