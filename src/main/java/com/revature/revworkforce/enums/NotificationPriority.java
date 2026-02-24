package com.revature.revworkforce.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Represents the priority level assigned to a notification
 * in the RevWorkForce system.
 *
 * <p>This enum maps to the {@code priority} column
 * in the NOTIFICATIONS table.</p>
 *
 * <p>Supported database values:
 * <ul>
 *     <li>LOW</li>
 *     <li>NORMAL</li>
 *     <li>HIGH</li>
 *     <li>URGENT</li>
 * </ul>
 * </p>
 *
 * <p>This enum ensures consistent classification of
 * notification importance and enables type-safe
 * priority handling across the application.</p>
 */
public enum NotificationPriority {

    LOW,
    NORMAL,
    HIGH,
    URGENT;

    /**
     * Converts a string value to the corresponding {@link NotificationPriority}.
     * Matching is case-insensitive and ignores leading/trailing spaces.
     *
     * @param value the priority value to convert
     * @return the matching NotificationPriority enum constant
     * @throws IllegalArgumentException if the value does not match any constant
     */
    @JsonCreator
    public static NotificationPriority fromString(String value) {
        if (value == null) {
            return null;
        }

        for (NotificationPriority priority : NotificationPriority.values()) {
            if (priority.name().equalsIgnoreCase(value.trim())) {
                return priority;
            }
        }

        throw new IllegalArgumentException("Invalid NotificationPriority: " + value);
    }
}