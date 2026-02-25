package com.revature.revworkforce.exception;

/**
 * Exception thrown when authentication fails.
 * 
 * @author RevWorkForce Team
 */
public class AuthenticationException extends RuntimeException {
    
    /**
     * Constructor with message only.
     * 
     * @param message the authentication error message
     */
    public AuthenticationException(String message) {
        super(message);
    }
    
    /**
     * Constructor with message and cause.
     * 
     * @param message the authentication error message
     * @param cause the cause
     */
    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}