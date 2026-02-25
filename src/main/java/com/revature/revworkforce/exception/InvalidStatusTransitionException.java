package com.revature.revworkforce.exception;

/**
 * Exception thrown when an invalid status transition is attempted
 * on a resource such as Leave, Goal, or Performance review.
 *
 * <p>Extends: ValidationException</p>
 *
 * Usage:
 * <pre>
 * throw new InvalidStatusTransitionException("Leave", "APPROVED", "PENDING");
 * </pre>
 *
 * @author RevWorkForce Team
 */
public class InvalidStatusTransitionException extends ValidationException {

    private String resourceName;
    private String currentStatus;
    private String attemptedStatus;

    /**
     * Constructor with transition details.
     *
     * @param resourceName Name of the resource (e.g., Leave)
     * @param currentStatus Current status
     * @param attemptedStatus Attempted new status
     */
    public InvalidStatusTransitionException(String resourceName,
                                            String currentStatus,
                                            String attemptedStatus) {

        super(String.format(
                "Invalid status transition for %s. Cannot change from %s to %s",
                resourceName, currentStatus, attemptedStatus
        ));

        this.resourceName = resourceName;
        this.currentStatus = currentStatus;
        this.attemptedStatus = attemptedStatus;
    }

    /**
     * Constructor with message and cause.
     */
    public InvalidStatusTransitionException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public String getAttemptedStatus() {
        return attemptedStatus;
    }
}