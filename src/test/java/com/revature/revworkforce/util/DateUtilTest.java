package com.revature.revworkforce.util;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DateUtilTest {

    // ===============================
    // WORKING DAYS TEST
    // ===============================

    @Test
    void calculateWorkingDays_WithWeekends() {

        LocalDate start = LocalDate.of(2024, 1, 1); // Monday
        LocalDate end = LocalDate.of(2024, 1, 5);   // Friday

        int result = DateUtil.calculateWorkingDays(start, end);

        assertEquals(5, result);
    }

    @Test
    void calculateWorkingDays_WithHoliday() {

        LocalDate start = LocalDate.of(2024, 1, 1);
        LocalDate end = LocalDate.of(2024, 1, 5);

        Set<LocalDate> holidays = new HashSet<>();
        holidays.add(LocalDate.of(2024, 1, 3));

        int result = DateUtil.calculateWorkingDays(start, end, holidays);

        assertEquals(4, result);
    }

    @Test
    void calculateWorkingDays_InvalidRange_ReturnZero() {

        LocalDate start = LocalDate.of(2024, 2, 10);
        LocalDate end = LocalDate.of(2024, 2, 1);

        int result = DateUtil.calculateWorkingDays(start, end);

        assertEquals(0, result);
    }

    // ===============================
    // WEEKEND TEST
    // ===============================

    @Test
    void isWeekend_Saturday_ReturnTrue() {

        LocalDate date = LocalDate.of(2024, 1, 6); // Saturday

        assertTrue(DateUtil.isWeekend(date));
    }

    @Test
    void isWeekend_Monday_ReturnFalse() {

        LocalDate date = LocalDate.of(2024, 1, 8);

        assertFalse(DateUtil.isWeekend(date));
    }

    // ===============================
    // WORKING DAY TEST
    // ===============================

    @Test
    void isWorkingDay_Weekday_ReturnTrue() {

        LocalDate date = LocalDate.of(2024, 1, 8);

        boolean result = DateUtil.isWorkingDay(date, null);

        assertTrue(result);
    }

    @Test
    void isWorkingDay_Holiday_ReturnFalse() {

        LocalDate date = LocalDate.of(2024, 1, 8);

        Set<LocalDate> holidays = new HashSet<>();
        holidays.add(date);

        boolean result = DateUtil.isWorkingDay(date, holidays);

        assertFalse(result);
    }

    // ===============================
    // DATE RANGE TEST
    // ===============================

    @Test
    void getDateRange_ReturnList() {

        LocalDate start = LocalDate.of(2024, 1, 1);
        LocalDate end = LocalDate.of(2024, 1, 3);

        List<LocalDate> dates = DateUtil.getDateRange(start, end);

        assertEquals(3, dates.size());
    }

    // ===============================
    // TOTAL DAYS TEST
    // ===============================

    @Test
    void calculateTotalDays_Inclusive() {

        LocalDate start = LocalDate.of(2024, 1, 1);
        LocalDate end = LocalDate.of(2024, 1, 3);

        long days = DateUtil.calculateTotalDays(start, end);

        assertEquals(3, days);
    }

    // ===============================
    // FORMAT TEST
    // ===============================

    @Test
    void formatDate_ReturnFormattedString() {

        LocalDate date = LocalDate.of(2024, 1, 1);

        String formatted = DateUtil.formatDate(date);

        assertEquals("01 Jan 2024", formatted);
    }

    @Test
    void formatDateTime_ReturnFormattedString() {

        LocalDateTime dateTime = LocalDateTime.of(2024, 1, 1, 10, 30);

        String formatted = DateUtil.formatDateTime(dateTime);

        assertTrue(formatted.contains("01 Jan 2024"));
    }

    // ===============================
    // PARSE DATE TEST
    // ===============================

    @Test
    void parseDate_ValidString() {

        LocalDate date = DateUtil.parseDate("01-01-2024");

        assertEquals(LocalDate.of(2024, 1, 1), date);
    }

    @Test
    void parseDate_InvalidString_ReturnNull() {

        LocalDate date = DateUtil.parseDate("invalid");

        assertNull(date);
    }

    // ===============================
    // CURRENT DATE TEST
    // ===============================

    @Test
    void getCurrentDate_NotNull() {

        assertNotNull(DateUtil.getCurrentDate());
    }

    @Test
    void getCurrentYear_Correct() {

        int year = DateUtil.getCurrentYear();

        assertEquals(LocalDate.now().getYear(), year);
    }

    // ===============================
    // DATE CHECK TEST
    // ===============================

    @Test
    void isPastDate_ReturnTrue() {

        LocalDate past = LocalDate.now().minusDays(1);

        assertTrue(DateUtil.isPastDate(past));
    }

    @Test
    void isFutureDate_ReturnTrue() {

        LocalDate future = LocalDate.now().plusDays(1);

        assertTrue(DateUtil.isFutureDate(future));
    }

    @Test
    void isToday_ReturnTrue() {

        assertTrue(DateUtil.isToday(LocalDate.now()));
    }

    // ===============================
    // ADD DAYS / MONTHS TEST
    // ===============================

    @Test
    void addDays_ReturnNewDate() {

        LocalDate date = LocalDate.of(2024, 1, 1);

        LocalDate result = DateUtil.addDays(date, 5);

        assertEquals(LocalDate.of(2024, 1, 6), result);
    }

    @Test
    void addMonths_ReturnNewDate() {

        LocalDate date = LocalDate.of(2024, 1, 1);

        LocalDate result = DateUtil.addMonths(date, 1);

        assertEquals(LocalDate.of(2024, 2, 1), result);
    }

    // ===============================
    // MONTH / YEAR TEST
    // ===============================

    @Test
    void getFirstDayOfMonth() {

        LocalDate date = LocalDate.of(2024, 5, 15);

        LocalDate result = DateUtil.getFirstDayOfMonth(date);

        assertEquals(LocalDate.of(2024, 5, 1), result);
    }

    @Test
    void getLastDayOfMonth() {

        LocalDate date = LocalDate.of(2024, 2, 10);

        LocalDate result = DateUtil.getLastDayOfMonth(date);

        assertEquals(LocalDate.of(2024, 2, 29), result);
    }

    // ===============================
    // DATE RANGE OVERLAP
    // ===============================

    @Test
    void doDateRangesOverlap_ReturnTrue() {

        LocalDate start1 = LocalDate.of(2024, 1, 1);
        LocalDate end1 = LocalDate.of(2024, 1, 10);

        LocalDate start2 = LocalDate.of(2024, 1, 5);
        LocalDate end2 = LocalDate.of(2024, 1, 15);

        assertTrue(DateUtil.doDateRangesOverlap(start1, end1, start2, end2));
    }

    @Test
    void doDateRangesOverlap_ReturnFalse() {

        LocalDate start1 = LocalDate.of(2024, 1, 1);
        LocalDate end1 = LocalDate.of(2024, 1, 5);

        LocalDate start2 = LocalDate.of(2024, 1, 10);
        LocalDate end2 = LocalDate.of(2024, 1, 15);

        assertFalse(DateUtil.doDateRangesOverlap(start1, end1, start2, end2));
    }

}