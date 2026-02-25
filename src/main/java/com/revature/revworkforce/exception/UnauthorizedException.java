package com.revature.revworkforce.exception;

/**
 * Exception thrown when user is not authorized to perform an action.
 * 
 * @author RevWorkForce Team
 */
public class UnauthorizedException extends RuntimeException {
    
    /**
     * Constructor with message only.
     * 
     * @param message the authorization error message
     */
    public UnauthorizedException(String message) {
        super(message);
    }
    
    /**
     * Constructor with default message.
     */
    public UnauthorizedException() {
        super("You are not authorized to perform this action");
    }
    
    /**
     * Constructor with message and cause.
     * 
     * @param message the authorization error message
     * @param cause the cause
     */
    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}