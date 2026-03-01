package com.revature.revworkforce.controller;

import com.revature.revworkforce.dto.LeaveApplicationDTO;
import com.revature.revworkforce.dto.LeaveBalanceDTO;
import com.revature.revworkforce.model.LeaveApplication;
import com.revature.revworkforce.service.LeaveService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class LeaveController {

    private final LeaveService leaveService;

    public LeaveController(LeaveService leaveService) {
        this.leaveService = leaveService;
    }

    // =========================================================
    // EMPLOYEE ENDPOINTS
    // =========================================================

    /**
     * Show apply leave form (if using frontend later)
     */
    @GetMapping("/employee/leave/apply")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public String showApplyLeavePage() {
        return "Apply Leave Page";
    }

    /**
     * Apply Leave
     */
    @PostMapping("/employee/leave/apply")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public LeaveApplication applyLeave(@RequestBody LeaveApplicationDTO dto,
                                       Authentication authentication) {

        String employeeId = authentication.getName();
        return leaveService.applyLeave(dto, employeeId);
    }

    /**
     * Cancel Leave
     */
    @PostMapping("/employee/leave/cancel/{applicationId}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public String cancelLeave(@PathVariable Long applicationId,
                              Authentication authentication) {

        String employeeId = authentication.getName();
        leaveService.cancelLeave(applicationId, employeeId);
        return "Leave cancelled successfully";
    }

    /**
     * View Leave History
     */
    @GetMapping("/employee/leave/history")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public List<LeaveApplication> getLeaveHistory(Authentication authentication) {

        String employeeId = authentication.getName();
        return leaveService.getEmployeeLeaveHistory(employeeId);
    }

    /**
     * View Leave Balances
     */
    @GetMapping("/employee/leave/balance")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public List<LeaveBalanceDTO> getLeaveBalances(@RequestParam Integer year,
                                                  Authentication authentication) {

        String employeeId = authentication.getName();
        return leaveService.getLeaveBalances(employeeId, year);
    }


    // =========================================================
    // MANAGER ENDPOINTS
    // =========================================================

    /**
     * View Pending Leaves
     */
    @GetMapping("/manager/leave/pending")
    @PreAuthorize("hasRole('MANAGER')")
    public List<LeaveApplication> getPendingLeaves(Authentication authentication) {

        String managerId = authentication.getName();
        return leaveService.getPendingLeavesForManager(managerId);
    }

    /**
     * View Team Leave History
     */
    @GetMapping("/manager/leave/team")
    @PreAuthorize("hasRole('MANAGER')")
    public List<LeaveApplication> getTeamLeaves(Authentication authentication) {

        String managerId = authentication.getName();
        return leaveService.getTeamLeaves(managerId);
    }

    /**
     * Review Leave Details
     */
    @GetMapping("/manager/leave/review/{applicationId}")
    @PreAuthorize("hasRole('MANAGER')")
    public LeaveApplication reviewLeave(@PathVariable Long applicationId) {

        return leaveService.getLeaveById(applicationId);
    }

    /**
     * Approve Leave
     */
    @PostMapping("/manager/leave/approve/{applicationId}")
    @PreAuthorize("hasRole('MANAGER')")
    public LeaveApplication approveLeave(@PathVariable Long applicationId,
                                         @RequestParam(required = false) String comments,
                                         Authentication authentication) {

        String managerId = authentication.getName();
        return leaveService.approveLeave(applicationId, managerId, comments);
    }

    /**
     * Reject Leave
     */
    @PostMapping("/manager/leave/reject/{applicationId}")
    @PreAuthorize("hasRole('MANAGER')")
    public LeaveApplication rejectLeave(@PathVariable Long applicationId,
                                        @RequestParam String rejectionReason,
                                        Authentication authentication) {

        String managerId = authentication.getName();
        return leaveService.rejectLeave(applicationId, managerId, rejectionReason);
    }
}