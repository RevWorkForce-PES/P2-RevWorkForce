package com.revature.revworkforce.service.impl;

import com.revature.revworkforce.dto.ChangePasswordRequest;
import com.revature.revworkforce.exception.AuthenticationException;
import com.revature.revworkforce.exception.ValidationException;
import com.revature.revworkforce.model.Employee;
import com.revature.revworkforce.repository.EmployeeRepository;
import com.revature.revworkforce.service.AuthService;
import com.revature.revworkforce.service.AuditService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Implementation of {@link AuthService}.
 *
 * Handles authentication logic, including:
 * - Password management
 * - Account locking and unlocking
 * - Tracking failed login attempts
 * - First login checks
 * 
 * This class is transactional and uses Spring Data JPA repository for
 * persistence.
 */
@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuditService auditService;

    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final int LOCK_DURATION_MINUTES = 15;

    /**
     * {@inheritDoc}
     */
    @Override
    public void changePassword(String employeeId, ChangePasswordRequest request) {
        if (!request.passwordsMatch()) {
            throw new ValidationException("New password and confirm password do not match");
        }

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new AuthenticationException("Employee not found"));

        if (!passwordEncoder.matches(request.getCurrentPassword(), employee.getPasswordHash())) {
            throw new AuthenticationException("Current password is incorrect");
        }

        validatePasswordStrength(request.getNewPassword());

        String encodedPassword = passwordEncoder.encode(request.getNewPassword());
        employee.setPasswordHash(encodedPassword);
        employee.setFirstLogin('N');
        employee.setUpdatedAt(LocalDateTime.now());

        employeeRepository.save(employee);

        // Audit Logging
        auditService.createAuditLog(
                employeeId,
                "PASSWORD_CHANGE",
                "EMPLOYEES",
                employeeId,
                null,
                "User changed their password",
                null,
                null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void recordSuccessfulLogin(String employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElse(null);

        if (employee != null) {
            employee.setLastLogin(LocalDateTime.now());
            employee.setFailedLoginAttempts(0);
            employee.setAccountLocked('N');
            employee.setLockedUntil(null);
            employeeRepository.save(employee);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void recordFailedLogin(String username) {
        Employee employee = employeeRepository.findByEmailOrEmployeeId(username, username).orElse(null);

        if (employee != null) {
            int attempts = employee.getFailedLoginAttempts() + 1;
            employee.setFailedLoginAttempts(attempts);

            if (attempts >= MAX_FAILED_ATTEMPTS) {
                employee.setAccountLocked('Y');
                employee.setLockedUntil(LocalDateTime.now().plusMinutes(LOCK_DURATION_MINUTES));

                // Audit account lock
                auditService.createAuditLog(
                        "SYSTEM",
                        "ACCOUNT_LOCKED",
                        "EMPLOYEES",
                        employee.getEmployeeId(),
                        "ACTIVE",
                        "LOCKED",
                        null,
                        "Too many failed login attempts");
            }

            employeeRepository.save(employee);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean unlockIfExpired(String employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElse(null);

        if (employee != null &&
                employee.getAccountLocked() == 'Y' &&
                employee.getLockedUntil() != null &&
                LocalDateTime.now().isAfter(employee.getLockedUntil())) {

            employee.setAccountLocked('N');
            employee.setLockedUntil(null);
            employee.setFailedLoginAttempts(0);
            employeeRepository.save(employee);

            // Audit account unlock
            auditService.createAuditLog(
                    "SYSTEM",
                    "ACCOUNT_UNLOCKED",
                    "EMPLOYEES",
                    employee.getEmployeeId(),
                    "LOCKED",
                    "ACTIVE",
                    null,
                    "Account lock expired");
            return true;
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAccountLocked(String employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElse(null);

        if (employee == null)
            return false;

        if (employee.getAccountLocked() == 'Y' &&
                employee.getLockedUntil() != null &&
                LocalDateTime.now().isAfter(employee.getLockedUntil())) {
            unlockIfExpired(employeeId);
            return false;
        }

        return employee.getAccountLocked() == 'Y';
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getRemainingLockTime(String employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElse(null);

        if (employee == null || employee.getAccountLocked() != 'Y' || employee.getLockedUntil() == null) {
            return 0;
        }

        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(employee.getLockedUntil()))
            return 0;

        return java.time.Duration.between(now, employee.getLockedUntil()).toMinutes();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFirstLogin(String employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElse(null);
        return employee != null && employee.getFirstLogin() == 'Y';
    }

    /**
     * Validates password strength.
     *
     * @param password the password to validate
     * @throws ValidationException if password does not meet security requirements
     */
    private void validatePasswordStrength(String password) {
        if (password == null || password.length() < 8) {
            throw new ValidationException("Password must be at least 8 characters long");
        }

        boolean hasUpperCase = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLowerCase = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);

        if (!hasUpperCase || !hasLowerCase || !hasDigit) {
            throw new ValidationException(
                    "Password must contain at least one uppercase letter, one lowercase letter, and one digit");
        }
    }
}