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

}