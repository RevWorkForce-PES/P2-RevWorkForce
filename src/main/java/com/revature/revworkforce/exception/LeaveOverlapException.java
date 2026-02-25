package com.revature.revworkforce.exception;

import java.time.LocalDate;

/**
 * Exception thrown when a requested leave period overlaps
 * with an existing leave record.
 *
 * <p>Extends: ValidationException</p>
 *
 * <p>Stores:</p>
 * <ul>
 *     <li>requestedStart</li>
 *     <li>requestedEnd</li>
 *     <li>existingStart</li>
 *     <li>existingEnd</li>
 * </ul>
 *
 * Usage:
 * <pre>
 * throw new LeaveOverlapException(startDate, endDate, existingStart, existingEnd);
 * </pre>
 *
 * @author RevWorkForce Team
 */
public class LeaveOverlapException extends ValidationException {

    private LocalDate requestedStart;
    private LocalDate requestedEnd;
    private LocalDate existingStart;
    private LocalDate existingEnd;

    /**
     * Constructor with leave overlap details.
     */
    public LeaveOverlapException(LocalDate requestedStart,
                                 LocalDate requestedEnd,
                                 LocalDate existingStart,
                                 LocalDate existingEnd) {

        super(String.format(
                "Requested leave (%s to %s) overlaps with existing leave (%s to %s)",
                requestedStart, requestedEnd, existingStart, existingEnd
        ));

        this.requestedStart = requestedStart;
        this.requestedEnd = requestedEnd;
        this.existingStart = existingStart;
        this.existingEnd = existingEnd;
    }

    /**
     * Constructor with message and cause.
     */
    public LeaveOverlapException(String message, Throwable cause) {
        super(message, cause);
    }

    public LocalDate getRequestedStart() {
        return requestedStart;
    }

    public LocalDate getRequestedEnd() {
        return requestedEnd;
    }

    public LocalDate getExistingStart() {
        return existingStart;
    }

    public LocalDate getExistingEnd() {
        return existingEnd;
    }
}