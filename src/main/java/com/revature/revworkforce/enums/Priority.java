package com.revature.revworkforce.enums;

/**
 * Represents priority levels used across the system.
 * 
 * Used in:
 *  - GOALS table (HIGH, MEDIUM, LOW)
 *  - NOTIFICATIONS table (LOW, NORMAL, HIGH, URGENT)
 *  - ANNOUNCEMENTS table (LOW, NORMAL, HIGH, URGENT)
 */
public enum Priority {

    LOW,
    NORMAL,
    MEDIUM,
    HIGH,
    URGENT
}