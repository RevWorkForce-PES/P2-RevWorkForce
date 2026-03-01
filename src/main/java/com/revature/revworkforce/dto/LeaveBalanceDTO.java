package com.revature.revworkforce.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object for Leave Balance.
 *
 * Used to display leave balance details for an employee.
 */
@Getter
@Setter
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
}