package com.revature.revworkforce.dto;

import com.revature.revworkforce.enums.LeaveStatus;
import com.revature.revworkforce.util.Constants;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Data Transfer Object for Leave Application.
 *
 * Used to transfer leave request data between
 * Controller and Service layers.
 */
@Getter
@Setter
public class LeaveApplicationDTO {

    /**
     * Leave Type Code (CL, SL, PL, etc.)
     */
    @NotBlank(message = "Leave type is required")
    private String leaveType;

    /**
     * Leave start date.
     */
    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    /**
     * Leave end date.
     */
    @NotNull(message = "End date is required")
    private LocalDate endDate;

    /**
     * Reason for leave.
     * Minimum length defined in Constants.
     */
    @NotBlank(message = "Reason is required")
    @Size(
        min = Constants.MIN_LEAVE_REASON_LENGTH,
        max = Constants.MAX_LEAVE_REASON_LENGTH,
        message = "Reason must be between " +
                Constants.MIN_LEAVE_REASON_LENGTH +
                " and " +
                Constants.MAX_LEAVE_REASON_LENGTH +
                " characters"
    )
    private String reason;

    /**
     * Total working days (calculated in service).
     */
    @Positive(message = "Total days must be greater than 0")
    private Integer totalDays;

    /**
     * Leave status (PENDING / APPROVED / REJECTED / CANCELLED).
     */
    private LeaveStatus status;

    /**
     * Manager comments (optional).
     */
    @Size(max = 500, message = "Manager comments cannot exceed 500 characters")
    private String managerComments;

    /**
     * Rejection reason (required when rejecting).
     */
    @Size(
        min = Constants.MIN_REJECTION_REASON_LENGTH,
        message = "Rejection reason must be at least " +
                Constants.MIN_REJECTION_REASON_LENGTH +
                " characters"
    )
    private String rejectionReason;
}