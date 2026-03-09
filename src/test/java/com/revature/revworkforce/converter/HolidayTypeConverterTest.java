package com.revature.revworkforce.converter;

import com.revature.revworkforce.enums.HolidayType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HolidayTypeConverterTest {

    private HolidayTypeConverter converter;

    @BeforeEach
    void setUp() {
        converter = new HolidayTypeConverter();
    }

    @Test
    void convertToDatabaseColumn_ShouldReturnName() {
        assertEquals("NATIONAL", converter.convertToDatabaseColumn(HolidayType.NATIONAL));
        assertEquals("FESTIVAL", converter.convertToDatabaseColumn(HolidayType.FESTIVAL));
        assertNull(converter.convertToDatabaseColumn(null));
    }

    @Test
    void convertToEntityAttribute_ShouldReturnEnumIgnoringCaseAndSpaces() {
        assertEquals(HolidayType.NATIONAL, converter.convertToEntityAttribute("national"));
        assertEquals(HolidayType.NATIONAL, converter.convertToEntityAttribute("National"));
        assertEquals(HolidayType.NATIONAL, converter.convertToEntityAttribute(" NATIONAL "));
        assertEquals(HolidayType.NATIONAL, converter.convertToEntityAttribute("NaTioNal"));
        assertNull(converter.convertToEntityAttribute(null));
        assertNull(converter.convertToEntityAttribute(""));
        assertNull(converter.convertToEntityAttribute("   "));
        assertNull(converter.convertToEntityAttribute("unknown")); // unknown value gracefully returns null
    }
}