package com.revature.revworkforce.exception;

/**
 * Exception thrown when business validation fails.
 * 
 * @author RevWorkForce Team
 */
public class ValidationException extends RuntimeException {
    
    /**
     * Constructor with message only.
     * 
     * @param message the validation error message
     */
    public ValidationException(String message) {
        super(message);
    }
    
    /**
     * Constructor with message and cause.
     * 
     * @param message the validation error message
     * @param cause the cause
     */
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}