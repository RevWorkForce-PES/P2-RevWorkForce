package com.revature.revworkforce.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Represents the priority level assigned to an announcement
 * in the RevWorkForce system.
 *
 * <p>This enum maps to the {@code priority} column
 * in the ANNOUNCEMENTS table.</p>
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
 * announcement importance and enables type-safe
 * priority handling across the application.</p>
 */
public enum AnnouncementPriority {

    LOW,
    NORMAL,
    HIGH,
    URGENT;

    /**
     * Converts a string value to the corresponding {@link AnnouncementPriority}.
     * Matching is case-insensitive and ignores leading/trailing spaces.
     *
     * @param value the priority value to convert
     * @return the matching AnnouncementPriority enum constant
     * @throws IllegalArgumentException if the value does not match any constant
     */
    @JsonCreator
    public static AnnouncementPriority fromString(String value) {
        if (value == null) {
            return null;
        }

        for (AnnouncementPriority priority : AnnouncementPriority.values()) {
            if (priority.name().equalsIgnoreCase(value.trim())) {
                return priority;
            }
        }

        throw new IllegalArgumentException("Invalid AnnouncementPriority: " + value);
    }
}