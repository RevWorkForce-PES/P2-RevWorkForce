package com.revature.revworkforce.util;

/**
 * Utility class that holds application-wide constants
 * for RevWorkForce HRM system.
 *
 * This class cannot be instantiated.
 */
public final class Constants {

    // Private constructor to prevent instantiation
    private Constants() {
        throw new UnsupportedOperationException("Constants class cannot be instantiated");
    }

    // ============================================
    // Application Info
    // ============================================
    public static final String APP_NAME = "RevWorkForce";
    public static final String APP_VERSION = "1.0";

    // ============================================
    // Default Roles
    // ============================================
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_MANAGER = "MANAGER";
    public static final String ROLE_EMPLOYEE = "EMPLOYEE";

    // ============================================
    // Employee Status
    // ============================================
    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_INACTIVE = "INACTIVE";
    public static final String STATUS_ON_LEAVE = "ON_LEAVE";
    public static final String STATUS_TERMINATED = "TERMINATED";

    // ============================================
    // Leave Status
    // ============================================
    public static final String LEAVE_PENDING = "PENDING";
    public static final String LEAVE_APPROVED = "APPROVED";
    public static final String LEAVE_REJECTED = "REJECTED";
    public static final String LEAVE_CANCELLED = "CANCELLED";

    // ============================================
    // Goal Status
    // ============================================
    public static final String GOAL_NOT_STARTED = "NOT_STARTED";
    public static final String GOAL_IN_PROGRESS = "IN_PROGRESS";
    public static final String GOAL_COMPLETED = "COMPLETED";

    // ============================================
    // Notification Priority
    // ============================================
    public static final String PRIORITY_LOW = "LOW";
    public static final String PRIORITY_NORMAL = "NORMAL";
    public static final String PRIORITY_HIGH = "HIGH";
    public static final String PRIORITY_URGENT = "URGENT";

}