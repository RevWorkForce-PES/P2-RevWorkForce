package com.revature.revworkforce.util;

/**
 * Application Constants.
 * 
 * Contains all application-wide constant values used throughout the system.
 * 
 * @author RevWorkForce Team
 */
public final class Constants {
    
    // Private constructor to prevent instantiation
    private Constants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    // ============================================
    // APPLICATION CONSTANTS
    // ============================================
    public static final String APPLICATION_NAME = "RevWorkForce";
    public static final String APPLICATION_VERSION = "1.0.0";
    public static final String COMPANY_NAME = "Revature";
    
    // ============================================
    // ROLE CONSTANTS
    // ============================================
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_MANAGER = "MANAGER";
    public static final String ROLE_EMPLOYEE = "EMPLOYEE";
    
    // Role prefixes for Spring Security
    public static final String ROLE_PREFIX = "ROLE_";
    public static final String SPRING_ROLE_ADMIN = ROLE_PREFIX + ROLE_ADMIN;
    public static final String SPRING_ROLE_MANAGER = ROLE_PREFIX + ROLE_MANAGER;
    public static final String SPRING_ROLE_EMPLOYEE = ROLE_PREFIX + ROLE_EMPLOYEE;
    
    // ============================================
    // LEAVE CONSTANTS
    // ============================================
    
    // Leave Types
    public static final String LEAVE_TYPE_CL = "CL";   // Casual Leave
    public static final String LEAVE_TYPE_SL = "SL";   // Sick Leave
    public static final String LEAVE_TYPE_PL = "PL";   // Paid Leave
    public static final String LEAVE_TYPE_PRIV = "PRIV"; // Privilege Leave
    
    // Default Leave Balances
    public static final int DEFAULT_CL_DAYS = 12;
    public static final int DEFAULT_SL_DAYS = 12;
    public static final int DEFAULT_PL_DAYS = 18;
    public static final int DEFAULT_PRIV_DAYS = 15;
    
    // Leave Carry Forward
    public static final int MAX_CL_CARRY_FORWARD = 5;
    public static final int MAX_SL_CARRY_FORWARD = 5;
    public static final int MAX_PL_CARRY_FORWARD = 10;
    public static final int MAX_PRIV_CARRY_FORWARD = 0;
    
    // Leave Constraints
    public static final int MIN_LEAVE_DAYS = 1;
    public static final int MAX_CONTINUOUS_LEAVE_DAYS = 30;
    public static final int MIN_LEAVE_REASON_LENGTH = 10;
    public static final int MAX_LEAVE_REASON_LENGTH = 500;
    public static final int MIN_REJECTION_REASON_LENGTH = 20;
    
    // ============================================
    // EMPLOYEE CONSTANTS
    // ============================================
    
    // Employee Status
    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_INACTIVE = "INACTIVE";
    public static final String STATUS_ON_LEAVE = "ON_LEAVE";
    public static final String STATUS_TERMINATED = "TERMINATED";
    
    // Employee ID Prefixes
    public static final String PREFIX_ADMIN = "ADM";
    public static final String PREFIX_MANAGER = "MGR";
    public static final String PREFIX_EMPLOYEE = "EMP";
    
    // Employee Constraints
    public static final int MIN_AGE = 18;
    public static final int MAX_AGE = 65;
    public static final int MIN_NAME_LENGTH = 2;
    public static final int MAX_NAME_LENGTH = 50;
    public static final int MIN_PASSWORD_LENGTH = 8;
    public static final int MAX_PASSWORD_LENGTH = 100;
    
    // ============================================
    // PERFORMANCE REVIEW CONSTANTS
    // ============================================
    
    // Rating Scale
    public static final double MIN_RATING = 1.0;
    public static final double MAX_RATING = 5.0;
    public static final double RATING_INCREMENT = 0.5;
    
    // Rating Labels
    public static final String RATING_EXCEPTIONAL = "Exceptional";           // 5.0
    public static final String RATING_EXCEEDS = "Exceeds Expectations";      // 4.0
    public static final String RATING_MEETS = "Meets Expectations";          // 3.0
    public static final String RATING_NEEDS_IMPROVEMENT = "Needs Improvement"; // 2.0
    public static final String RATING_UNSATISFACTORY = "Unsatisfactory";     // 1.0
    
    // Review Constraints
    public static final int MIN_REVIEW_TEXT_LENGTH = 100;
    public static final int MAX_REVIEW_TEXT_LENGTH = 2000;
    public static final int MIN_FEEDBACK_LENGTH = 100;
    public static final int MAX_FEEDBACK_LENGTH = 2000;
    
    // ============================================
    // GOAL CONSTANTS
    // ============================================
    
    // Goal Progress
    public static final int MIN_PROGRESS = 0;
    public static final int MAX_PROGRESS = 100;
    public static final int PROGRESS_NOT_STARTED = 0;
    public static final int PROGRESS_IN_PROGRESS = 50;
    public static final int PROGRESS_COMPLETED = 100;
    
    // Goal Constraints
    public static final int MAX_ACTIVE_GOALS = 10;
    public static final int MIN_GOAL_TITLE_LENGTH = 5;
    public static final int MAX_GOAL_TITLE_LENGTH = 200;
    public static final int MIN_GOAL_DESCRIPTION_LENGTH = 10;
    public static final int MAX_GOAL_DESCRIPTION_LENGTH = 1000;
    
    // ============================================
    // SECURITY CONSTANTS
    // ============================================
    
    // Password Policy
    public static final int BCRYPT_STRENGTH = 12;
    public static final int MAX_FAILED_LOGIN_ATTEMPTS = 5;
    public static final int ACCOUNT_LOCK_DURATION_MINUTES = 15;
    public static final int PASSWORD_HISTORY_COUNT = 3;
    
    // Session
    public static final int SESSION_TIMEOUT_MINUTES = 30;
    public static final int REMEMBER_ME_VALIDITY_SECONDS = 86400; // 24 hours
    
    // ============================================
    // NOTIFICATION CONSTANTS
    // ============================================
    
    // Notification Types
    public static final String NOTIF_TYPE_LEAVE = "LEAVE";
    public static final String NOTIF_TYPE_PERFORMANCE = "PERFORMANCE";
    public static final String NOTIF_TYPE_GOAL = "GOAL";
    public static final String NOTIF_TYPE_ANNOUNCEMENT = "ANNOUNCEMENT";
    public static final String NOTIF_TYPE_SYSTEM = "SYSTEM";
    
    // Notification Constraints
    public static final int NOTIFICATION_EXPIRY_DAYS = 90;
    public static final int MAX_UNREAD_NOTIFICATIONS = 100;
    
    // ============================================
    // AUDIT LOG CONSTANTS
    // ============================================
    
    // Audit Actions
    public static final String AUDIT_INSERT = "INSERT";
    public static final String AUDIT_UPDATE = "UPDATE";
    public static final String AUDIT_DELETE = "DELETE";
    public static final String AUDIT_LOGIN = "LOGIN";
    public static final String AUDIT_LOGOUT = "LOGOUT";
    public static final String AUDIT_APPROVE = "APPROVE";
    public static final String AUDIT_REJECT = "REJECT";
    
    // Audit Retention
    public static final int AUDIT_RETENTION_YEARS = 7;
    
    // ============================================
    // DATE/TIME CONSTANTS
    // ============================================
    
    // Date Formats
    public static final String DATE_FORMAT = "dd-MM-yyyy";
    public static final String DATE_TIME_FORMAT = "dd-MM-yyyy HH:mm:ss";
    public static final String DISPLAY_DATE_FORMAT = "dd MMM yyyy";
    public static final String DISPLAY_DATE_TIME_FORMAT = "dd MMM yyyy HH:mm";
    
    // Time Zones
    public static final String DEFAULT_TIMEZONE = "Asia/Kolkata";
    
    // ============================================
    // PAGINATION CONSTANTS
    // ============================================
    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MAX_PAGE_SIZE = 100;
    public static final int DEFAULT_PAGE_NUMBER = 0;
    
    // ============================================
    // FILE UPLOAD CONSTANTS
    // ============================================
    public static final long MAX_FILE_SIZE_MB = 10;
    public static final long MAX_FILE_SIZE_BYTES = MAX_FILE_SIZE_MB * 1024 * 1024;
    public static final String[] ALLOWED_IMAGE_TYPES = {"jpg", "jpeg", "png", "gif"};
    public static final String[] ALLOWED_DOCUMENT_TYPES = {"pdf", "doc", "docx", "xls", "xlsx"};
    
    // ============================================
    // VALIDATION ERROR MESSAGES
    // ============================================
    public static final String ERROR_REQUIRED_FIELD = "This field is required";
    public static final String ERROR_INVALID_EMAIL = "Invalid email format";
    public static final String ERROR_INVALID_PHONE = "Invalid phone number format";
    public static final String ERROR_INVALID_DATE_RANGE = "Start date must be before end date";
    public static final String ERROR_PAST_DATE = "Date cannot be in the past";
    public static final String ERROR_INVALID_PASSWORD = "Password must contain at least 8 characters with uppercase, lowercase, and digit";
    public static final String ERROR_PASSWORD_MISMATCH = "Passwords do not match";
    public static final String ERROR_INSUFFICIENT_BALANCE = "Insufficient leave balance";
    public static final String ERROR_OVERLAP_LEAVE = "Leave dates overlap with existing leave";
    public static final String ERROR_INVALID_RATING = "Rating must be between 1.0 and 5.0";
    public static final String ERROR_INVALID_PROGRESS = "Progress must be between 0 and 100";
    
    // ============================================
    // SUCCESS MESSAGES
    // ============================================
    public static final String SUCCESS_LOGIN = "Login successful";
    public static final String SUCCESS_LOGOUT = "Logout successful";
    public static final String SUCCESS_PASSWORD_CHANGED = "Password changed successfully";
    public static final String SUCCESS_LEAVE_APPLIED = "Leave application submitted successfully";
    public static final String SUCCESS_LEAVE_APPROVED = "Leave approved successfully";
    public static final String SUCCESS_LEAVE_REJECTED = "Leave rejected successfully";
    public static final String SUCCESS_PROFILE_UPDATED = "Profile updated successfully";
    public static final String SUCCESS_GOAL_CREATED = "Goal created successfully";
    public static final String SUCCESS_REVIEW_SUBMITTED = "Performance review submitted successfully";
    
    // ============================================
    // HTTP STATUS MESSAGES
    // ============================================
    public static final String HTTP_STATUS_OK = "Success";
    public static final String HTTP_STATUS_CREATED = "Created";
    public static final String HTTP_STATUS_BAD_REQUEST = "Bad Request";
    public static final String HTTP_STATUS_UNAUTHORIZED = "Unauthorized";
    public static final String HTTP_STATUS_FORBIDDEN = "Forbidden";
    public static final String HTTP_STATUS_NOT_FOUND = "Not Found";
    public static final String HTTP_STATUS_CONFLICT = "Conflict";
    public static final String HTTP_STATUS_INTERNAL_ERROR = "Internal Server Error";
    
    // ============================================
    // DASHBOARD ROUTES
    // ============================================
    public static final String ROUTE_LOGIN = "/login";
    public static final String ROUTE_LOGOUT = "/logout";
    public static final String ROUTE_DASHBOARD = "/dashboard";
    public static final String ROUTE_ADMIN_DASHBOARD = "/admin/dashboard";
    public static final String ROUTE_MANAGER_DASHBOARD = "/manager/dashboard";
    public static final String ROUTE_EMPLOYEE_DASHBOARD = "/employee/dashboard";
    public static final String ROUTE_CHANGE_PASSWORD = "/change-password";
    
    // ============================================
    // CACHE KEYS
    // ============================================
    public static final String CACHE_EMPLOYEES = "employees";
    public static final String CACHE_DEPARTMENTS = "departments";
    public static final String CACHE_DESIGNATIONS = "designations";
    public static final String CACHE_LEAVE_TYPES = "leaveTypes";
    public static final String CACHE_HOLIDAYS = "holidays";
}