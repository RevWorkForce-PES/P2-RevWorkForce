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
    public LeaveController(LeaveService leaveService,
            LeaveTypeRepository leaveTypeRepository) {
this.leaveService = leaveService;
this.leaveTypeRepository = leaveTypeRepository;
}

    // ================= EMPLOYEE =================

    @GetMapping("/employee/apply")
    @PreAuthorize("hasAnyRole('EMPLOYEE','MANAGER','ADMIN')")
    public String showApplyPage(Model model, Authentication auth) {

        String employeeId = auth.getName();

        model.addAttribute("leaveApplicationDTO", new LeaveApplicationDTO());

        model.addAttribute("balances",
                leaveService.getLeaveBalances(employeeId,
                        java.time.LocalDate.now().getYear()));

        // 🔥 ADD THIS LINE
        model.addAttribute("leaveTypes", leaveTypeRepository.findAll());

        return "employee/leave/apply";
    }

    @PostMapping("/employee/apply")
    @PreAuthorize("hasAnyRole('EMPLOYEE','MANAGER','ADMIN')")
    public String applyLeave(@ModelAttribute LeaveApplicationDTO dto,
                             Authentication auth) {

        leaveService.applyLeave(dto, auth.getName());
        return "redirect:/leave/employee/history";
    }

    @GetMapping("/employee/history")
    @PreAuthorize("hasAnyRole('EMPLOYEE','MANAGER','ADMIN')")
    public String showHistory(Model model, Authentication auth) {

        model.addAttribute("history",
                leaveService.getEmployeeLeaveHistory(auth.getName()));

        return "employee/leave/history";
    }

    @PostMapping("/employee/cancel/{id}")
    @PreAuthorize("hasAnyRole('EMPLOYEE','MANAGER','ADMIN')")
    public String cancel(@PathVariable Long id, Authentication auth) {

        leaveService.cancelLeave(id, auth.getName());
        return "redirect:/leave/employee/history";
    }

    @GetMapping("/employee/balance")
    @PreAuthorize("hasAnyRole('EMPLOYEE','MANAGER','ADMIN')")
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

    @GetMapping("/manager/pending")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public String showPendingLeaves(Model model, Authentication auth) {

        String managerId = auth.getName();

        model.addAttribute("pending",
                leaveService.getPendingLeavesForManager(managerId));

        return "manager/leave/pending";
    }

   
    @GetMapping("/manager/team")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public String showTeamLeaves(Model model, Authentication auth) {

        String managerId = auth.getName();

        model.addAttribute("team",
                leaveService.getTeamLeaves(managerId));

        return "manager/leave/team";
    }
    

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

        return "redirect:/leave/manager/pending";
    }

    @PostMapping("/manager/reject/{id}")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public String reject(@PathVariable Long id,
                         @RequestParam String rejectionReason,
                         Authentication authentication) {

        leaveService.rejectLeave(id,
                authentication.getName(),
                rejectionReason);

        return "redirect:/leave/manager/pending";
    }
   
}