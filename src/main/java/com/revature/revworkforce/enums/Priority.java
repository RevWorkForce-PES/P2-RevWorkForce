package com.revature.revworkforce.enums;

/**
 * Represents priority levels used across the system.
 *
 * Used in:
 *  - GOALS table (HIGH, MEDIUM, LOW)
 *  - NOTIFICATIONS table (LOW, NORMAL, HIGH, URGENT)
 *  - ANNOUNCEMENTS table (LOW, NORMAL, HIGH, URGENT)
 */
public enum Priority {

    LOW,
    NORMAL,
    MEDIUM,
    HIGH,
    URGENT;

    /**
     * Converts a string value to Priority enum.
     * Matching is case-insensitive and trims spaces.
     *
     * @param value priority value from database or user input
     * @return corresponding Priority enum
     * @throws IllegalArgumentException if value is invalid
     */
    public static Priority fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Priority cannot be null or empty");
        }

        try {
            return Priority.valueOf(value.trim().toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid Priority: " + value);
        }
    }
}