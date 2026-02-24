package com.revature.revworkforce.enums;

/**
 * Represents the type of notification sent to employees.
 *
 * This enum maps to the NOTIFICATION_TYPE column in the NOTIFICATIONS table.
 * Supported types:
 *  - LEAVE
 *  - PERFORMANCE
 *  - GOAL
 *  - ANNOUNCEMENT
 *  - SYSTEM
 */
public enum NotificationType {

    LEAVE,
    PERFORMANCE,
    GOAL,
    ANNOUNCEMENT,
    SYSTEM;

    /**
     * Converts a string value to NotificationType enum.
     * Matching is case-insensitive and trims spaces.
     *
     * @param value notification type from database or user input
     * @return corresponding NotificationType enum
     * @throws IllegalArgumentException if value is invalid
     */
    public static NotificationType fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("NotificationType cannot be null or empty");
        }

        try {
            return NotificationType.valueOf(value.trim().toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid NotificationType: " + value);
        }
    }
}