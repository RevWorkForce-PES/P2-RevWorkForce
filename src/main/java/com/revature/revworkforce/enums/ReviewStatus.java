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
    COMPLETED
}