package com.revature.revworkforce.enums;

/**
 * Represents the type of notification sent to employees.
 * 
 * This enum maps to the NOTIFICATION_TYPE column in the NOTIFICATIONS table.
 * Supported types:
 *  - LEAVE
 *  - PERFORMANCE
 *  - GOAL
 *  - ANNOUNCEMENT
 *  - SYSTEM
 */
public enum NotificationType {

    LEAVE,
    PERFORMANCE,
    GOAL,
    ANNOUNCEMENT,
    SYSTEM
}