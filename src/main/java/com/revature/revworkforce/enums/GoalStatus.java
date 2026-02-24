package com.revature.revworkforce.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Represents the progress status of a goal
 * in the RevWorkForce system.
 *
 * <p>This enum maps to the {@code status} column
 * in the GOALS table.</p>
 *
 * <p>Supported database values:
 * <ul>
 *     <li>NOT_STARTED</li>
 *     <li>IN_PROGRESS</li>
 *     <li>COMPLETED</li>
 *     <li>CANCELLED</li>
 *     <li>DEFERRED</li>
 * </ul>
 * </p>
 *
 * <p>This enum ensures consistent tracking of goal lifecycle
 * states and provides type-safe handling across the application.</p>
 */
public enum GoalStatus {

    NOT_STARTED,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED,
    DEFERRED;

    /**
     * Converts a string value to the corresponding {@link GoalStatus}.
     * Matching is case-insensitive and ignores leading/trailing spaces.
     *
     * @param value the goal status value to convert
     * @return the matching GoalStatus enum constant
     * @throws IllegalArgumentException if the value does not match any constant
     */
    @JsonCreator
    public static GoalStatus fromString(String value) {
        if (value == null) {
            return null;
        }

        for (GoalStatus status : GoalStatus.values()) {
            if (status.name().equalsIgnoreCase(value.trim())) {
                return status;
            }
        }

        throw new IllegalArgumentException("Invalid GoalStatus: " + value);
    }
}