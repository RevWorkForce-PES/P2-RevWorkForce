package com.revature.revworkforce.service;

import com.revature.revworkforce.dto.LeaveApplicationDTO;
import com.revature.revworkforce.dto.LeaveBalanceDTO;
import com.revature.revworkforce.model.Employee;
import com.revature.revworkforce.model.LeaveApplication;
import com.revature.revworkforce.model.LeaveBalance;
import com.revature.revworkforce.model.LeaveType;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * LeaveService - Core business logic interface for Leave Management.
 *
 * Handles:
 * - Employee leave lifecycle
 * - Manager approval workflow
 * - Leave balance management
 * - Validation and business rules
 */
public interface LeaveService {

    // =========================================================
    // EMPLOYEE OPERATIONS
    // =========================================================

    /**
     * Apply for leave.
     *
     * Validations:
     * - Date validation
     * - Working days calculation
     * - Overlapping leave detection
     * - Continuous leave limit
     * - Leave balance sufficiency
     */
    LeaveApplication applyLeave(LeaveApplicationDTO dto, String employeeId);

    /**
     * Cancel a leave application.
     *
     * Rules:
     * - Only employee who applied can cancel
     * - Cannot cancel APPROVED leave after start date
     * - Balance restored if already deducted
     */
    void cancelLeave(Long applicationId, String employeeId);

    /**
     * Fetch leave application by ID.
     */
    LeaveApplication getLeaveById(Long applicationId);

    /**
     * Get leave history for employee.
     */
    List<LeaveApplication> getEmployeeLeaveHistory(String employeeId);


    // =========================================================
    // MANAGER OPERATIONS
    // =========================================================

    /**
     * Approve leave request.
     *
     * Rules:
     * - Only reporting manager can approve
     * - Status must be PENDING
     * - Balance deducted
     */
    LeaveApplication approveLeave(Long applicationId, String approverId, String comments);

    /**
     * Reject leave request.
     *
     * Rules:
     * - Only reporting manager can reject
     * - Status must be PENDING
     * - No balance deduction
     */
    LeaveApplication rejectLeave(Long applicationId, String approverId, String rejectionReason);

    /**
     * Get pending leave requests for manager.
     */
    List<LeaveApplication> getPendingLeavesForManager(String managerId);

    /**
     * Get all leave applications of team members.
     */
    List<LeaveApplication> getTeamLeaves(String managerId);


    // =========================================================
    // LEAVE BALANCE OPERATIONS
    // =========================================================

    /**
     * Get leave balances for employee for a specific year.
     */
    List<LeaveBalanceDTO> getLeaveBalances(String employeeId, Integer year);

    /**
     * Initialize leave balances for new employee.
     */
    void initializeLeaveBalances(Employee employee);

    /**
     * Get existing leave balance or create if not exists.
     */
    LeaveBalance getOrCreateLeaveBalance(Employee employee, LeaveType type, Integer year);


    // =========================================================
    // VALIDATION UTILITIES (Business Rules)
    // =========================================================

    /**
     * Validate overlapping leaves.
     */
    void validateLeaveOverlap(Employee employee,
                              java.time.LocalDate startDate,
                              java.time.LocalDate endDate);

    /**
     * Validate working days calculation (exclude weekends & holidays).
     */
    int calculateWorkingDays(java.time.LocalDate startDate,
                             java.time.LocalDate endDate);

    /**
     * Validate continuous leave limit.
     */
    void validateContinuousLeaveLimit(Employee employee,
                                      java.time.LocalDate startDate,
                                      java.time.LocalDate endDate);

    /**
     * Validate leave balance sufficiency.
     */
    void validateLeaveBalance(LeaveBalance leaveBalance,
                              int requestedDays);
    
   
}