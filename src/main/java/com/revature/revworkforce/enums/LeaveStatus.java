package com.revature.revworkforce.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Represents the status of a leave application in the RevWorkForce system.
 *
 * <p>This enum maps to the {@code status} column in the LEAVE_APPLICATIONS table.</p>
 *
 * <p>Supported database values:
 * <ul>
 *     <li>PENDING</li>
 *     <li>APPROVED</li>
 *     <li>REJECTED</li>
 *     <li>CANCELLED</li>
 * </ul>
 * </p>
 *
 * <p>This enum ensures type safety and consistent handling of leave
 * lifecycle states across the application.</p>
 */
public enum LeaveStatus {

    PENDING,
    APPROVED,
    REJECTED,
    CANCELLED;

    /**
     * Converts a string value to the corresponding {@link LeaveStatus}.
     * Matching is case-insensitive and ignores leading/trailing spaces.
     *
     * @param value the leave status value to convert
     * @return the matching LeaveStatus enum constant
     * @throws IllegalArgumentException if the value does not match any constant
     */
    @JsonCreator
    public static LeaveStatus fromString(String value) {
        if (value == null) {
            return null;
        }

        for (LeaveStatus status : LeaveStatus.values()) {
            if (status.name().equalsIgnoreCase(value.trim())) {
                return status;
            }
        }

        throw new IllegalArgumentException("Invalid LeaveStatus: " + value);
    }
}