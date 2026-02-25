package com.revature.revworkforce.exception;

/**
 * Exception thrown when creating a resource that already exists.
 *
 * <p>
 * Extends: RuntimeException
 * </p>
 *
 * <p>
 * Stores:
 * </p>
 * <ul>
 * <li>resourceName</li>
 * <li>fieldName</li>
 * <li>fieldValue</li>
 * </ul>
 *
 * Usage:
 * 
 * <pre>
 * throw new DuplicateResourceException("Employee", "email", "user@example.com");
 * </pre>
 *
 * @author RevWorkForce Team
 */
public class DuplicateResourceException extends RuntimeException {

    private String resourceName;
    private String fieldName;
    private Object fieldValue;

    /**
     * Constructor with message only.
     *
     * @param message the error message
     */
    public DuplicateResourceException(String message) {
        super(message);
    }

    /**
     * Constructor with resource details.
     *
     * @param resourceName the name of the resource (e.g., "Employee", "Department")
     * @param fieldName    the field name (e.g., "email", "username")
     * @param fieldValue   the field value that caused the duplicate
     */
    public DuplicateResourceException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s already exists with %s: '%s'", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    /**
     * Constructor with message and cause.
     *
     * @param message the error message
     * @param cause   the cause
     */
    public DuplicateResourceException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Object getFieldValue() {
        return fieldValue;
    }
}
