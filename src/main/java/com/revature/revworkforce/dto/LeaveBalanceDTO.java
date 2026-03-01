package com.revature.revworkforce.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object for Leave Balance.
 *
 * Used to display leave balance details for an employee.
 */

public class LeaveBalanceDTO {

    /**
     * Leave Type Code (CL, SL, PL, etc.)
     */
    @NotBlank(message = "Leave type is required")
    private String leaveType;

    /**
     * Total leave allocated for the year.
     */
    @NotNull
    @Min(value = 0, message = "Total allocated cannot be negative")
    private Integer totalAllocated;

    /**
     * Leave days already used.
     */
    @NotNull
    @Min(value = 0, message = "Used leaves cannot be negative")
    private Integer usedLeaves;

    /**
     * Remaining leave balance.
     */
    @NotNull
    @Min(value = 0, message = "Remaining balance cannot be negative")
    private Integer remainingBalance;

    /**
     * Calendar year.
     */
    @NotNull(message = "Year is required")
    @Min(value = 2000, message = "Invalid year")
    private Integer year;
    
    // ================= GETTERS =================

    public String getLeaveType() {
        return leaveType;
    }

    public Integer getTotalAllocated() {
        return totalAllocated;
    }

    public Integer getUsedLeaves() {
        return usedLeaves;
    }

    public Integer getRemainingBalance() {
        return remainingBalance;
    }

    public Integer getYear() {
        return year;
    }

    // ================= SETTERS =================

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public void setTotalAllocated(Integer totalAllocated) {
        this.totalAllocated = totalAllocated;
    }

    public void setUsedLeaves(Integer usedLeaves) {
        this.usedLeaves = usedLeaves;
    }

    public void setRemainingBalance(Integer remainingBalance) {
        this.remainingBalance = remainingBalance;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}