package com.revature.revworkforce.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Represents the priority level assigned to a goal
 * in the RevWorkForce system.
 *
 * <p>This enum maps to the {@code priority} column
 * in the GOALS table.</p>
 *
 * <p>Supported database values:
 * <ul>
 *     <li>HIGH</li>
 *     <li>MEDIUM</li>
 *     <li>LOW</li>
 * </ul>
 * </p>
 *
 * <p>This enum ensures consistent classification of
 * goal importance and supports type-safe priority handling
 * throughout the application.</p>
 */
public enum GoalPriority {

    HIGH,
    MEDIUM,
    LOW;

    /**
     * Converts a string value to the corresponding {@link GoalPriority}.
     * Matching is case-insensitive and ignores leading/trailing spaces.
     *
     * @param value the priority value to convert
     * @return the matching GoalPriority enum constant
     * @throws IllegalArgumentException if the value does not match any constant
     */
    @JsonCreator
    public static GoalPriority fromString(String value) {
        if (value == null) {
            return null;
        }

        for (GoalPriority priority : GoalPriority.values()) {
            if (priority.name().equalsIgnoreCase(value.trim())) {
                return priority;
            }
        }

        throw new IllegalArgumentException("Invalid GoalPriority: " + value);
    }
}