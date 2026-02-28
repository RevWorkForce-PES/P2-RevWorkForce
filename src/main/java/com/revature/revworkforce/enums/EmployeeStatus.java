package com.revature.revworkforce.enums;

/**
 * Enum representing the status of an employee in the system.
 * 
 * <p>An employee can be in one of the following states:
 * <ul>
 *   <li>ACTIVE - Currently working and active in the system</li>
 *   <li>INACTIVE - No longer working (resigned, terminated, or retired)</li>
 *   <li>ON_LEAVE - Currently on extended leave</li>
 *   <li>TERMINATED - Employment terminated by company</li>
 * </ul>
 * 
 * @author RevWorkForce Team
 * @version 1.0
 * @since 2026
 */
public enum EmployeeStatus {
    
    /**
     * Employee is currently active and working
     */
    ACTIVE("Active", "Employee is currently active and working"),
    
    /**
     * Employee is no longer active (general inactive status)
     */
    INACTIVE("Inactive", "Employee is no longer active"),
    
    /**
     * Employee is currently on extended leave (maternity, sabbatical, etc.)
     */
    ON_LEAVE("On Leave", "Employee is currently on extended leave"),
    
    /**
     * Employee's employment has been terminated
     */
    TERMINATED("Terminated", "Employee's employment has been terminated");
    
    private final String displayName;
    private final String description;
    
    /**
     * Constructor for EmployeeStatus enum.
     * 
     * @param displayName the user-friendly display name
     * @param description detailed description of the status
     */
    EmployeeStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    /**
     * Gets the user-friendly display name of the status.
     * 
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Gets the detailed description of the status.
     * 
     * @return the description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Converts a string value to EmployeeStatus enum.
     * Case-insensitive matching.
     * 
     * @param value the string value to convert
     * @return the corresponding EmployeeStatus, or null if not found
     */
    public static EmployeeStatus fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        
        for (EmployeeStatus status : EmployeeStatus.values()) {
            if (status.name().equalsIgnoreCase(value) || 
                status.displayName.equalsIgnoreCase(value)) {
                return status;
            }
        }
        return null;
    }
    
    /**
     * Checks if the employee status allows login.
     * 
     * @return true if employee can login with this status
     */
    public boolean canLogin() {
        return this == ACTIVE || this == ON_LEAVE;
    }
    
    /**
     * Checks if the employee can apply for leave with this status.
     * 
     * @return true if employee can apply for leave
     */
    public boolean canApplyLeave() {
        return this == ACTIVE;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}