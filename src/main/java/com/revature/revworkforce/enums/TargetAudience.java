package com.revature.revworkforce.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Represents the target audience for an announcement
 * in the RevWorkForce system.
 *
 * <p>This enum maps to the {@code target_audience}
 * column in the ANNOUNCEMENTS table.</p>
 *
 * <p>Supported database values:
 * <ul>
 *     <li>ALL</li>
 *     <li>DEPARTMENT</li>
 *     <li>DESIGNATION</li>
 *     <li>SPECIFIC</li>
 * </ul>
 * </p>
 *
 * <p>This enum ensures consistent and type-safe handling
 * of announcement visibility rules across the application.</p>
 */
public enum TargetAudience {

    ALL,
    DEPARTMENT,
    DESIGNATION,
    SPECIFIC;

    /**
     * Converts a string value to the corresponding {@link TargetAudience}.
     * Matching is case-insensitive and ignores leading/trailing spaces.
     *
     * @param value the target audience value to convert
     * @return the matching TargetAudience enum constant
     * @throws IllegalArgumentException if the value does not match any constant
     */
    @JsonCreator
    public static TargetAudience fromString(String value) {
        if (value == null) {
            return null;
        }

        for (TargetAudience audience : TargetAudience.values()) {
            if (audience.name().equalsIgnoreCase(value.trim())) {
                return audience;
            }
        }

        throw new IllegalArgumentException("Invalid TargetAudience: " + value);
    }
}