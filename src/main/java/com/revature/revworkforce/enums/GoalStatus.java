package com.revature.revworkforce.enums;

/**
 * Represents the progress status of an employee goal.
 *
 * This enum maps to the STATUS column in the GOALS table.
 * Allowed database values:
 *  - NOT_STARTED
 *  - IN_PROGRESS
 *  - COMPLETED
 *  - CANCELLED
 *  - DEFERRED
 */
public enum GoalStatus {

    NOT_STARTED,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED,
    DEFERRED;

    /**
     * Converts a string value to GoalStatus enum.
     * Matching is case-insensitive and trims spaces.
     *
     * @param value status value from database or user input
     * @return corresponding GoalStatus enum
     * @throws IllegalArgumentException if value is invalid
     */
    public static GoalStatus fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("GoalStatus cannot be null or empty");
        }

        try {
            return GoalStatus.valueOf(value.trim().toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid GoalStatus: " + value);
        }
    }
}