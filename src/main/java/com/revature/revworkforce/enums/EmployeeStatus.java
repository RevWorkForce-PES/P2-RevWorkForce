package com.revature.revworkforce.enums;

/**
 * Represents the employment status of an employee.
 * 
 * This enum maps to the STATUS column in the EMPLOYEES table.
 * Allowed database values:
 *  - Active
 *  - Inactive
 *  - On Leave
 *  - Terminated
 */
public enum EmployeeStatus {

    ACTIVE,
    INACTIVE,
    ON_LEAVE,
    TERMINATED
}