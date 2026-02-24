package com.revature.revworkforce.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Represents the gender of an employee.
 *
 * <p>This enum maps to the {@code gender} column in the EMPLOYEES table.</p>
 *
 * <p>Supported values:
 * <ul>
 *   <li>M - Male</li>
 *   <li>F - Female</li>
 *   <li>O - Other</li>
 * </ul>
 * </p>
 */
public enum Gender {

    M,
    F,
    O;

    /**
     * Converts a string value to a {@link Gender} enum.
     * Matching is case-insensitive.
     *
     * @param value the gender value
     * @return corresponding Gender enum
     * @throws IllegalArgumentException if value does not match any enum constant
     */
    @JsonCreator
    public static Gender fromString(String value) {
        if (value == null) return null;

        for (Gender gender : Gender.values()) {
            if (gender.name().equalsIgnoreCase(value.trim())) {
                return gender;
            }
        }

        throw new IllegalArgumentException("Invalid Gender: " + value);
    }
}