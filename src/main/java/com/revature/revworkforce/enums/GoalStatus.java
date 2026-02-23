package com.revature.revworkforce.enums;

/**
 * Represents the progress status of an employee goal.
 * 
 * This enum maps to the STATUS column in the GOALS table.
 * Allowed database values:
 *  - NOT_STARTED
 *  - IN_PROGRESS
 *  - COMPLETED
 *  - CANCELLED
 *  - DEFERRED
 */
public enum GoalStatus {

    NOT_STARTED,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED,
    DEFERRED
}