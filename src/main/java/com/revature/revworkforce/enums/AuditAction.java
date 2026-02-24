package com.revature.revworkforce.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Represents the type of action recorded in the audit log
 * within the RevWorkForce system.
 *
 * <p>This enum maps to the {@code action} column
 * in the AUDIT_LOGS table.</p>
 *
 * <p>Supported database values:
 * <ul>
 *     <li>INSERT</li>
 *     <li>UPDATE</li>
 *     <li>DELETE</li>
 *     <li>LOGIN</li>
 *     <li>LOGOUT</li>
 *     <li>APPROVE</li>
 *     <li>REJECT</li>
 * </ul>
 * </p>
 *
 * <p>This enum ensures consistent and type-safe tracking
 * of user and system operations across the application.</p>
 */
public enum AuditAction {

    INSERT,
    UPDATE,
    DELETE,
    LOGIN,
    LOGOUT,
    APPROVE,
    REJECT;

    /**
     * Converts a string value to the corresponding {@link AuditAction}.
     * Matching is case-insensitive and ignores leading/trailing spaces.
     *
     * @param value the audit action value to convert
     * @return the matching AuditAction enum constant
     * @throws IllegalArgumentException if the value does not match any constant
     */
    @JsonCreator
    public static AuditAction fromString(String value) {
        if (value == null) {
            return null;
        }

        for (AuditAction action : AuditAction.values()) {
            if (action.name().equalsIgnoreCase(value.trim())) {
                return action;
            }
        }

        throw new IllegalArgumentException("Invalid AuditAction: " + value);
    }
}