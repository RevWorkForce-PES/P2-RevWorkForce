package com.revature.revworkforce.util;

import java.time.LocalDate;
import java.util.regex.Pattern;

/**
 * Validation Utility Class.
 * 
 * Provides utility methods for validating various data types and business rules.
 * 
 * @author RevWorkForce Team
 */
public class ValidationUtil {
    
    // Email validation pattern (RFC 5322 compliant)
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );
    
    // Phone validation pattern (Indian mobile numbers)
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^[6-9]\\d{9}$" // 10 digits starting with 6-9
    );
    
    // Employee ID pattern (e.g., EMP001, ADM001, MGR001)
    private static final Pattern EMPLOYEE_ID_PATTERN = Pattern.compile(
        "^[A-Z]{3}\\d{3,}$"
    );
    
    // Password validation pattern
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$"
    );
    
    // Postal code pattern (Indian PIN code)
    private static final Pattern POSTAL_CODE_PATTERN = Pattern.compile(
        "^[1-9][0-9]{5}$"
    );
    
    /**
     * Check if string is null or empty.
     * 
     * @param str the string to check
     * @return true if null or empty
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
    
    /**
     * Check if string is not null and not empty.
     * 
     * @param str the string to check
     * @return true if not empty
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }
    
    /**
     * Validate email address format.
     * 
     * @param email the email to validate
     * @return true if valid email format
     */
    public static boolean isValidEmail(String email) {
        if (isEmpty(email)) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }
    
    /**
     * Validate phone number format (Indian mobile).
     * 
     * @param phone the phone number to validate
     * @return true if valid phone format
     */
    public static boolean isValidPhone(String phone) {
        if (isEmpty(phone)) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone.trim()).matches();
    }
    
    /**
     * Validate employee ID format.
     * 
     * @param employeeId the employee ID to validate
     * @return true if valid employee ID format
     */
    public static boolean isValidEmployeeId(String employeeId) {
        if (isEmpty(employeeId)) {
            return false;
        }
        return EMPLOYEE_ID_PATTERN.matcher(employeeId.trim()).matches();
    }
    
    /**
     * Validate password strength.
     * Must contain:
     * - At least 8 characters
     * - At least one uppercase letter
     * - At least one lowercase letter
     * - At least one digit
     * 
     * @param password the password to validate
     * @return true if valid password
     */
    public static boolean isValidPassword(String password) {
        if (isEmpty(password)) {
            return false;
        }
        return PASSWORD_PATTERN.matcher(password).matches();
    }
    
    /**
     * Validate postal code (Indian PIN code).
     * 
     * @param postalCode the postal code to validate
     * @return true if valid postal code
     */
    public static boolean isValidPostalCode(String postalCode) {
        if (isEmpty(postalCode)) {
            return false;
        }
        return POSTAL_CODE_PATTERN.matcher(postalCode.trim()).matches();
    }
    
    /**
     * Validate that a number is positive.
     * 
     * @param number the number to validate
     * @return true if positive (> 0)
     */
    public static boolean isPositive(Integer number) {
        return number != null && number > 0;
    }
    
    /**
     * Validate that a number is non-negative.
     * 
     * @param number the number to validate
     * @return true if non-negative (>= 0)
     */
    public static boolean isNonNegative(Integer number) {
        return number != null && number >= 0;
    }
    
    /**
     * Validate that a number is within a range (inclusive).
     * 
     * @param number the number to validate
     * @param min minimum value (inclusive)
     * @param max maximum value (inclusive)
     * @return true if within range
     */
    public static boolean isInRange(Integer number, int min, int max) {
        return number != null && number >= min && number <= max;
    }
    
    /**
     * Validate that a decimal number is within a range (inclusive).
     * 
     * @param number the number to validate
     * @param min minimum value (inclusive)
     * @param max maximum value (inclusive)
     * @return true if within range
     */
    public static boolean isInRange(Double number, double min, double max) {
        return number != null && number >= min && number <= max;
    }
    
    /**
     * Validate date range (start date must be before or equal to end date).
     * 
     * @param startDate the start date
     * @param endDate the end date
     * @return true if valid date range
     */
    public static boolean isValidDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return false;
        }
        return !startDate.isAfter(endDate);
    }
    
    /**
     * Validate that date is not in the past.
     * 
     * @param date the date to validate
     * @return true if date is today or in future
     */
    public static boolean isNotPastDate(LocalDate date) {
        if (date == null) {
            return false;
        }
        return !date.isBefore(LocalDate.now());
    }
    
    /**
     * Validate that date is in the future.
     * 
     * @param date the date to validate
     * @return true if date is after today
     */
    public static boolean isFutureDate(LocalDate date) {
        if (date == null) {
            return false;
        }
        return date.isAfter(LocalDate.now());
    }
    
    /**
     * Validate age (must be 18 or older).
     * 
     * @param dateOfBirth the date of birth
     * @return true if age >= 18
     */
    public static boolean isValidAge(LocalDate dateOfBirth) {
        if (dateOfBirth == null) {
            return false;
        }
        LocalDate minDate = LocalDate.now().minusYears(18);
        return !dateOfBirth.isAfter(minDate);
    }
    
    /**
     * Validate string length.
     * 
     * @param str the string to validate
     * @param minLength minimum length
     * @param maxLength maximum length
     * @return true if length is within range
     */
    public static boolean isValidLength(String str, int minLength, int maxLength) {
        if (str == null) {
            return false;
        }
        int length = str.trim().length();
        return length >= minLength && length <= maxLength;
    }
    
    /**
     * Validate that string contains only letters.
     * 
     * @param str the string to validate
     * @return true if contains only letters
     */
    public static boolean isAlphabetic(String str) {
        if (isEmpty(str)) {
            return false;
        }
        return str.trim().matches("^[a-zA-Z]+$");
    }
    
    /**
     * Validate that string contains only letters and spaces.
     * 
     * @param str the string to validate
     * @return true if contains only letters and spaces
     */
    public static boolean isAlphabeticWithSpaces(String str) {
        if (isEmpty(str)) {
            return false;
        }
        return str.trim().matches("^[a-zA-Z ]+$");
    }
    
    /**
     * Validate that string contains only digits.
     * 
     * @param str the string to validate
     * @return true if contains only digits
     */
    public static boolean isNumeric(String str) {
        if (isEmpty(str)) {
            return false;
        }
        return str.trim().matches("^\\d+$");
    }
    
    /**
     * Validate that string contains only alphanumeric characters.
     * 
     * @param str the string to validate
     * @return true if contains only letters and digits
     */
    public static boolean isAlphanumeric(String str) {
        if (isEmpty(str)) {
            return false;
        }
        return str.trim().matches("^[a-zA-Z0-9]+$");
    }
    
    /**
     * Sanitize string by trimming whitespace.
     * 
     * @param str the string to sanitize
     * @return sanitized string or null
     */
    public static String sanitize(String str) {
        if (str == null) {
            return null;
        }
        return str.trim();
    }
    
    /**
     * Validate rating value (must be between 1.0 and 5.0).
     * 
     * @param rating the rating to validate
     * @return true if valid rating
     */
    public static boolean isValidRating(Double rating) {
        return rating != null && rating >= 1.0 && rating <= 5.0;
    }
    
    /**
     * Validate progress percentage (must be between 0 and 100).
     * 
     * @param progress the progress to validate
     * @return true if valid progress
     */
    public static boolean isValidProgress(Integer progress) {
        return progress != null && progress >= 0 && progress <= 100;
    }
    
    /**
     * Validate salary amount (must be positive).
     * 
     * @param salary the salary to validate
     * @return true if valid salary
     */
    public static boolean isValidSalary(Double salary) {
        return salary != null && salary > 0;
    }
    
    /**
     * Validate leave days (must be positive and not exceed max).
     * 
     * @param days the number of days
     * @param maxDays maximum allowed days
     * @return true if valid
     */
    public static boolean isValidLeaveDays(Integer days, Integer maxDays) {
        if (days == null || days <= 0) {
            return false;
        }
        if (maxDays != null && days > maxDays) {
            return false;
        }
        return true;
    }
}