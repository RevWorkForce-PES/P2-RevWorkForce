package com.revature.revworkforce.converter;

import com.revature.revworkforce.enums.HolidayType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * JPA AttributeConverter for HolidayType enum.
 * Handles case-insensitive DB values (e.g. "National" → NATIONAL).
 */
@Converter(autoApply = true)
public class HolidayTypeConverter implements AttributeConverter<HolidayType, String> {

    @Override
    public String convertToDatabaseColumn(HolidayType attribute) {
        return attribute == null ? null : attribute.name();
    }

    @Override
    public HolidayType convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank())
            return null;
        try {
            return HolidayType.valueOf(dbData.trim().toUpperCase().replace(" ", "_"));
        } catch (IllegalArgumentException e) {
            return null; // gracefully skip unknown values
        }
    }
}
