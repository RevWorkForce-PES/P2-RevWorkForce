package com.revature.revworkforce.exception;

import java.time.LocalDateTime;

/**
 * Exception thrown when an account is locked due to failed login attempts.
 *
 * <p>
 * Extends: AuthenticationException
 * </p>
 *
 * <p>
 * Stores:
 * </p>
 * <ul>
 * <li>lockedUntil</li>
 * </ul>
 *
 * Usage:
 * 
 * <pre>
 * throw new AccountLockedException(lockedUntil);
 * </pre>
 *
 * @author RevWorkForce Team
 */
public class AccountLockedException extends AuthenticationException {

    private LocalDateTime lockedUntil;

    /**
     * Constructor with locked until date.
     *
     * @param lockedUntil The time until which the account is locked
     */
    public AccountLockedException(LocalDateTime lockedUntil) {
        super(String.format("Account is locked. Try again after %s", lockedUntil));
        this.lockedUntil = lockedUntil;
    }

    /**
     * Constructor with message and cause.
     *
     * @param message the validation error message
     * @param cause   the cause
     */
    public AccountLockedException(String message, Throwable cause) {
        super(message, cause);
    }

    public LocalDateTime getLockedUntil() {
        return lockedUntil;
    }
}
