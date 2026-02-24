package com.revature.revworkforce.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Represents the classification type of a holiday in the RevWorkForce system.
 *
 * <p>This enum maps to the {@code holiday_type} column in the HOLIDAYS table.</p>
 *
 * <p>Supported database values:
 * <ul>
 *     <li>NATIONAL</li>
 *     <li>REGIONAL</li>
 *     <li>FESTIVAL</li>
 *     <li>COMPANY_SPECIFIC</li>
 * </ul>
 * </p>
 *
 * <p>This enum ensures consistent categorization of holidays
 * across the application.</p>
 */
public enum HolidayType {

    NATIONAL,
    REGIONAL,
    FESTIVAL,
    COMPANY_SPECIFIC;

    /**
     * Converts a string value to the corresponding {@link HolidayType}.
     * Matching is case-insensitive and ignores leading/trailing spaces.
     *
     * @param value the holiday type value to convert
     * @return the matching HolidayType enum constant
     * @throws IllegalArgumentException if the value does not match any constant
     */
    @JsonCreator
    public static HolidayType fromString(String value) {
        if (value == null) {
            return null;
        }

        for (HolidayType type : HolidayType.values()) {
            if (type.name().equalsIgnoreCase(value.trim())) {
                return type;
            }
        }

        throw new IllegalArgumentException("Invalid HolidayType: " + value);
    }
}