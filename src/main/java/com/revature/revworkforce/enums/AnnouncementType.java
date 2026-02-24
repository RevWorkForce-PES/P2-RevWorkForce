package com.revature.revworkforce.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Represents the category of an announcement
 * in the RevWorkForce system.
 *
 * <p>This enum maps to the {@code announcement_type}
 * column in the ANNOUNCEMENTS table.</p>
 *
 * <p>Supported database values:
 * <ul>
 *     <li>GENERAL</li>
 *     <li>POLICY</li>
 *     <li>EVENT</li>
 *     <li>EMERGENCY</li>
 *     <li>CELEBRATION</li>
 * </ul>
 * </p>
 *
 * <p>This enum ensures consistent classification of
 * announcements and enables type-safe handling
 * across the application.</p>
 */
public enum AnnouncementType {

    GENERAL,
    POLICY,
    EVENT,
    EMERGENCY,
    CELEBRATION;

    /**
     * Converts a string value to the corresponding {@link AnnouncementType}.
     * Matching is case-insensitive and ignores leading/trailing spaces.
     *
     * @param value the announcement type value to convert
     * @return the matching AnnouncementType enum constant
     * @throws IllegalArgumentException if the value does not match any constant
     */
    @JsonCreator
    public static AnnouncementType fromString(String value) {
        if (value == null) {
            return null;
        }

        for (AnnouncementType type : AnnouncementType.values()) {
            if (type.name().equalsIgnoreCase(value.trim())) {
                return type;
            }
        }

        throw new IllegalArgumentException("Invalid AnnouncementType: " + value);
    }
}