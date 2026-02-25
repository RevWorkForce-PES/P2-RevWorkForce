package com.revature.revworkforce.exception;

/**
 * Exception thrown when an employee does not have enough leave balance
 * to fulfill the requested leave duration.
 *
 * <p>Extends: ValidationException</p>
 *
 * <p>Stores:</p>
 * <ul>
 *     <li>leaveType</li>
 *     <li>requiredDays</li>
 *     <li>availableDays</li>
 * </ul>
 *
 * Usage:
 * <pre>
 * throw new InsufficientLeaveBalanceException("CL", 5, 2);
 * </pre>
 *
 * @author RevWorkForce Team
 */
public class InsufficientLeaveBalanceException extends ValidationException {

    private String leaveType;
    private int requiredDays;
    private int availableDays;

    /**
     * Constructor with leave balance details.
     *
     * @param leaveType Type of leave (e.g., CL, SL, PL)
     * @param requiredDays Number of days requested
     * @param availableDays Number of days currently available
     */
    public InsufficientLeaveBalanceException(String leaveType,
                                             int requiredDays,
                                             int availableDays) {
        super(String.format(
                "Insufficient %s leave balance. Required: %d days, Available: %d days",
                leaveType, requiredDays, availableDays
        ));
        this.leaveType = leaveType;
        this.requiredDays = requiredDays;
        this.availableDays = availableDays;
    }

    /**
     * Constructor with message and cause.
     *
     * @param message error message
     * @param cause root cause
     */
    public InsufficientLeaveBalanceException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getLeaveType() {
        return leaveType;
    }

    public int getRequiredDays() {
        return requiredDays;
    }

    public int getAvailableDays() {
        return availableDays;
    }
}