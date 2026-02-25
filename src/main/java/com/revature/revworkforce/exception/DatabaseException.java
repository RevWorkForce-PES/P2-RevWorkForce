package com.revature.revworkforce.exception;

/**
 * Generic database-related exception.
 * Used for unexpected DB failures.
 *
 * <p>
 * Extends: RuntimeException
 * </p>
 *
 * @author RevWorkForce Team
 */
public class DatabaseException extends RuntimeException {

    /**
     * Constructor with message only.
     *
     * @param message the database error message
     */
    public DatabaseException(String message) {
        super(message);
    }

    /**
     * Constructor with message and cause.
     *
     * @param message the database error message
     * @param cause   the cause
     */
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
