package com.revature.revworkforce.service;

import com.revature.revworkforce.dto.ChangePasswordRequest;

/**
 * Service interface for authentication-related operations.
 * 
 * Provides methods for:
 * - Changing passwords
 * - Recording login attempts (successful or failed)
 * - Account lock/unlock management
 * - Checking first login status
 * - Retrieving remaining lock time
 * 
 * Implementations should handle all business logic for authentication.
 */
public interface AuthService {

    /**
     * Change the password of an employee.
     *
     * @param employeeId the ID of the employee
     * @param request the change password request containing current and new passwords
     * @throws ValidationException if the new password is invalid or does not match confirmation
     * @throws AuthenticationException if the current password is incorrect
     */
    void changePassword(String employeeId, ChangePasswordRequest request);

    /**
     * Record a successful login for an employee.
     * Resets failed login attempts and updates last login timestamp.
     *
     * @param employeeId the ID of the employee
     */
    void recordSuccessfulLogin(String employeeId);

    /**
     * Record a failed login attempt for an employee.
     * If maximum failed attempts are reached, the account is locked temporarily.
     *
     * @param username the username or employee ID
     */
    void recordFailedLogin(String username);

    /**
     * Unlock the account if the lock duration has expired.
     *
     * @param employeeId the ID of the employee
     * @return true if the account was unlocked, false otherwise
     */
    boolean unlockIfExpired(String employeeId);

    /**
     * Check if an employee account is currently locked.
     *
     * @param employeeId the ID of the employee
     * @return true if the account is locked, false otherwise
     */
    boolean isAccountLocked(String employeeId);

    /**
     * Get the remaining lock duration in minutes.
     *
     * @param employeeId the ID of the employee
     * @return the remaining minutes until the account is unlocked, or 0 if not locked
     */
    long getRemainingLockTime(String employeeId);

    /**
     * Check if this is the employee's first login.
     *
     * @param employeeId the ID of the employee
     * @return true if it is the first login, false otherwise
     */
    boolean isFirstLogin(String employeeId);
}