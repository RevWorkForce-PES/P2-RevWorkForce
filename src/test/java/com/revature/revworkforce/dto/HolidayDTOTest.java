package com.revature.revworkforce.dto;

import com.revature.revworkforce.enums.HolidayType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class HolidayDTOTest {

    private HolidayDTO holiday;

    @BeforeEach
    void setUp() {
        holiday = new HolidayDTO();
    }

    // =========================
    // Test Getters and Setters
    // =========================
    @Test
    void testSettersAndGetters() {
        LocalDate date = LocalDate.now().plusDays(1);
        LocalDateTime now = LocalDateTime.now();

        holiday.setHolidayId(1L);
        holiday.setHolidayName("New Year");
        holiday.setHolidayDate(date);
        holiday.setType(HolidayType.NATIONAL);
        holiday.setDescription("New Year Holiday");
        holiday.setIsActive(true);
        holiday.setCreatedAt(now);
        holiday.setUpdatedAt(now);

        assertEquals(1L, holiday.getHolidayId());
        assertEquals("New Year", holiday.getHolidayName());
        assertEquals(date, holiday.getHolidayDate());
        assertEquals(HolidayType.NATIONAL, holiday.getType());
        assertEquals("New Year Holiday", holiday.getDescription());
        assertTrue(holiday.getIsActive());
        assertEquals(now, holiday.getCreatedAt());
        assertEquals(now, holiday.getUpdatedAt());
    }

    // =========================
    // Test Builder
    // =========================
    @Test
    void testBuilder() {
        LocalDate date = LocalDate.now().plusDays(2);
        HolidayDTO builtHoliday = HolidayDTO.builder()
                .holidayId(5L)
                .holidayName("Diwali")
                .holidayDate(date)
                .description("Festival of lights")
                .isActive(true)
                .build();

        assertEquals(5L, builtHoliday.getHolidayId());
        assertEquals("Diwali", builtHoliday.getHolidayName());
        assertEquals(date, builtHoliday.getHolidayDate());
        assertEquals("Festival of lights", builtHoliday.getDescription());
        assertTrue(builtHoliday.getIsActive());
    }

    // =========================
    // Optional: Test default values
    // =========================
    @Test
    void testDefaultValues() {
        HolidayDTO defaultHoliday = new HolidayDTO();
        assertNull(defaultHoliday.getHolidayId());
        assertNull(defaultHoliday.getHolidayName());
        assertNull(defaultHoliday.getHolidayDate());
        assertNull(defaultHoliday.getType());
        assertNull(defaultHoliday.getDescription());
        assertNull(defaultHoliday.getIsActive());
        assertNull(defaultHoliday.getCreatedAt());
        assertNull(defaultHoliday.getUpdatedAt());
    }
}