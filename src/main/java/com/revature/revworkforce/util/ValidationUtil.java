package com.revature.revworkforce.util;

import java.util.regex.Pattern;

/**
 * Utility class for validating user inputs
 * such as email, phone number, and business rules.
 *
 * This class cannot be instantiated.
 */
public final class ValidationUtil {

    private ValidationUtil() {
        throw new UnsupportedOperationException("ValidationUtil cannot be instantiated");
    }

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^[6-9]\\d{9}$");

    private static final Pattern EMPLOYEE_ID_PATTERN =
            Pattern.compile("^(EMP|MGR|ADM)\\d{3}$");

    /**
     * Validates email format.
     *
     * @param email email to validate
     * @return true if valid
     */
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Validates Indian phone number (10 digits starting with 6-9).
     *
     * @param phone phone number
     * @return true if valid
     */
    public static boolean isValidPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone).matches();
    }

    /**
     * Validates employee ID format (EMP001, MGR002, ADM003).
     *
     * @param employeeId employee ID
     * @return true if valid
     */
    public static boolean isValidEmployeeId(String employeeId) {
        return employeeId != null && EMPLOYEE_ID_PATTERN.matcher(employeeId).matches();
    }

    /**
     * Validates password strength.
     * Minimum 8 characters, at least one uppercase, one lowercase, one digit.
     *
     * @param password raw password
     * @return true if strong
     */
    public static boolean isStrongPassword(String password) {

        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;

        for (char ch : password.toCharArray()) {
            if (Character.isUpperCase(ch)) hasUpper = true;
            if (Character.isLowerCase(ch)) hasLower = true;
            if (Character.isDigit(ch)) hasDigit = true;
        }

        return hasUpper && hasLower && hasDigit;
    }

    /**
     * Validates leave reason (minimum 10 characters).
     *
     * @param reason leave reason
     * @return true if valid
     */
    public static boolean isValidLeaveReason(String reason) {
        return reason != null && reason.trim().length() >= 10;
    }
}