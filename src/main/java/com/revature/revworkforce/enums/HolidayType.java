package com.revature.revworkforce.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Holiday type classification.
 * Stored in DB as uppercase string using EnumType.STRING.
 */
public enum HolidayType {

    NATIONAL,
    REGIONAL,
    FESTIVAL,
    COMPANY_SPECIFIC;

    @JsonCreator
    public static HolidayType fromString(String value) {
        if (value == null) {
            return null;
        }
        return HolidayType.valueOf(value.trim().toUpperCase().replace(" ", "_"));
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}