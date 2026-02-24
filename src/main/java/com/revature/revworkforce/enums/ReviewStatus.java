package com.revature.revworkforce.enums;

/**
 * Represents the lifecycle state of a performance review.
 *
 * This enum maps to the STATUS column in the PERFORMANCE_REVIEWS table.
 * Allowed database values:
 *  - DRAFT
 *  - SUBMITTED
 *  - REVIEWED
 *  - COMPLETED
 */
public enum ReviewStatus {

    DRAFT,
    SUBMITTED,
    REVIEWED,
    COMPLETED;

    /**
     * Converts a string value to ReviewStatus enum.
     * Matching is case-insensitive and trims spaces.
     *
     * @param value status value from database or user input
     * @return corresponding ReviewStatus enum
     * @throws IllegalArgumentException if value is invalid
     */
    public static ReviewStatus fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("ReviewStatus cannot be null or empty");
        }

        try {
            return ReviewStatus.valueOf(value.trim().toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid ReviewStatus: " + value);
        }
    }
}