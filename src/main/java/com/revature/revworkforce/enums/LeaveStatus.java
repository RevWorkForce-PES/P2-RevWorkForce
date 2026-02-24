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
    CANCELLED;

    /**
     * Converts a string value to LeaveStatus enum.
     * Matching is case-insensitive and trims spaces.
     *
     * @param value status value from database or user input
     * @return corresponding LeaveStatus enum
     * @throws IllegalArgumentException if value is invalid
     */
    public static LeaveStatus fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("LeaveStatus cannot be null or empty");
        }

        try {
            return LeaveStatus.valueOf(value.trim().toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid LeaveStatus: " + value);
        }
    }
}