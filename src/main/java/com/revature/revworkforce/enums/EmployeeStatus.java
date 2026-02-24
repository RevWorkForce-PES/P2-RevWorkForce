package com.revature.revworkforce.enums;

/**
 * Represents the employment status of an employee.
 *
 * This enum maps to the STATUS column in the EMPLOYEES table.
 * Allowed database values:
 *  - ACTIVE
 *  - INACTIVE
 *  - ON_LEAVE
 *  - TERMINATED
 */
public enum EmployeeStatus {

    ACTIVE,
    INACTIVE,
    ON_LEAVE,
    TERMINATED;

    /**
     * Converts a string value to EmployeeStatus enum.
     * Matching is case-insensitive and trims spaces.
     *
     * @param value status value from database or user input
     * @return corresponding EmployeeStatus enum
     * @throws IllegalArgumentException if value is invalid
     */
    public static EmployeeStatus fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("EmployeeStatus cannot be null or empty");
        }

        try {
            return EmployeeStatus.valueOf(value.trim().toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid EmployeeStatus: " + value);
        }
    }
}