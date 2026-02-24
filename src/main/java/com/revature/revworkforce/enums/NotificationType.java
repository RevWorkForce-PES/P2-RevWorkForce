package com.revature.revworkforce.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Represents the type of notification generated within
 * the RevWorkForce system.
 *
 * <p>This enum maps to the {@code notification_type}
 * column in the NOTIFICATIONS table.</p>
 *
 * <p>Supported database values:
 * <ul>
 *     <li>LEAVE</li>
 *     <li>PERFORMANCE</li>
 *     <li>GOAL</li>
 *     <li>ANNOUNCEMENT</li>
 *     <li>SYSTEM</li>
 * </ul>
 * </p>
 *
 * <p>This enum ensures consistent categorization of
 * notifications and provides type-safe handling
 * across the application.</p>
 */
public enum NotificationType {

    LEAVE,
    PERFORMANCE,
    GOAL,
    ANNOUNCEMENT,
    SYSTEM;

    /**
     * Converts a string value to the corresponding {@link NotificationType}.
     * Matching is case-insensitive and ignores leading/trailing spaces.
     *
     * @param value the notification type value to convert
     * @return the matching NotificationType enum constant
     * @throws IllegalArgumentException if the value does not match any constant
     */
    @JsonCreator
    public static NotificationType fromString(String value) {
        if (value == null) {
            return null;
        }

        for (NotificationType type : NotificationType.values()) {
            if (type.name().equalsIgnoreCase(value.trim())) {
                return type;
            }
        }

        throw new IllegalArgumentException("Invalid NotificationType: " + value);
    }
}