package com.revature.revworkforce.exception;

/**
 * Exception thrown when an employee is not found.
 * 
 * @author RevWorkForce Team
 */
public class EmployeeNotFoundException extends ResourceNotFoundException {
    
    /**
     * Constructor with employee ID.
     * 
     * @param employeeId the employee ID
     */
    public EmployeeNotFoundException(String employeeId) {
        super("Employee", "employeeId", employeeId);
    }
    
    /**
     * Constructor with custom message.
     * 
     * @param message the error message
     */
    public EmployeeNotFoundException(String message, boolean isCustomMessage) {
        super(message);
    }
    
    /**
     * Constructor with field name and value.
     * 
     * @param fieldName the field name (e.g., "email")
     * @param fieldValue the field value
     */
    public EmployeeNotFoundException(String fieldName, Object fieldValue) {
        super("Employee", fieldName, fieldValue);
    }
}