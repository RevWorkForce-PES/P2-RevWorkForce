package com.revature.revworkforce.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Represents the employment status of an employee within the RevWorkForce system.
 *
 * <p>This enum maps to the {@code status} column in the EMPLOYEES table.</p>
 *
 * <p>Supported database values:
 * <ul>
 *     <li>ACTIVE</li>
 *     <li>INACTIVE</li>
 *     <li>ON_LEAVE</li>
 *     <li>TERMINATED</li>
 * </ul>
 * </p>
 *
 * <p>This enum ensures type safety and consistency across the application
 * when handling employee status values.</p>
 */
public enum EmployeeStatus {

    ACTIVE,
    INACTIVE,
    ON_LEAVE,
    TERMINATED;

    /**
     * Converts a string value to the corresponding {@link EmployeeStatus}.
     * Matching is case-insensitive and ignores leading/trailing spaces.
     *
     * @param value the status value to convert
     * @return the matching EmployeeStatus enum constant
     * @throws IllegalArgumentException if the value does not match any constant
     */
    @JsonCreator
    public static EmployeeStatus fromString(String value) {
        if (value == null) {
            return null;
        }

        for (EmployeeStatus status : EmployeeStatus.values()) {
            if (status.name().equalsIgnoreCase(value.trim())) {
                return status;
            }
        }

        throw new IllegalArgumentException("Invalid EmployeeStatus: " + value);
    }
}