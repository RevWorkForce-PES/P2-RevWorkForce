package com.revature.revworkforce.enums;

/**
 * Represents the status of a leave application.
 * 
 * This enum maps to the STATUS column in the LEAVE_APPLICATIONS table.
 * Allowed database values:
 *  - PENDING
 *  - APPROVED
 *  - REJECTED
 *  - CANCELLED
 */
public enum LeaveStatus {

    PENDING,
    APPROVED,
    REJECTED,
    CANCELLED
}