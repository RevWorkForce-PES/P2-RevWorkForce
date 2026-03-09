package com.revature.revworkforce.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Represents the lifecycle state of a performance review
 * in the RevWorkForce system.
 *
 * <p>
 * This enum maps to the {@code status} column
 * in the PERFORMANCE_REVIEWS table.
 * </p>
 *
 * <p>
 * Supported database values:
 * <ul>
 * <li>DRAFT</li>
 * <li>SUBMITTED</li>
 * <li>REVIEWED</li>
 * <li>COMPLETED</li>
 * </ul>
 * </p>
 *
 * <p>
 * This enum ensures consistent handling of performance
 * review states across the application.
 * </p>
 */
public enum ReviewStatus {

    PENDING_SELF_ASSESSMENT,
    PENDING_MANAGER_REVIEW,
    COMPLETED;

    /**
     * Converts a string value to the corresponding {@link ReviewStatus}.
     * Matching is case-insensitive and ignores leading/trailing spaces.
     *
     * @param value the review status value to convert
     * @return the matching ReviewStatus enum constant
     * @throws IllegalArgumentException if the value does not match any constant
     */
    @JsonCreator
    public static ReviewStatus fromString(String value) {
        if (value == null) {
            return null;
        }

        for (ReviewStatus status : ReviewStatus.values()) {
            if (status.name().equalsIgnoreCase(value.trim())) {
                return status;
            }
        }

        throw new IllegalArgumentException("Invalid ReviewStatus: " + value);
    }
}