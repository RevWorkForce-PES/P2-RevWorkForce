package com.revature.revworkforce.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Utility class for date-related operations
 * such as working day calculation and date differences.
 *
 * This class cannot be instantiated.
 */
public final class DateUtil {

    private DateUtil() {
        throw new UnsupportedOperationException("DateUtil cannot be instantiated");
    }

    /**
     * Calculates number of working days between two dates
     * (excluding Saturdays and Sundays).
     *
     * @param startDate start date (inclusive)
     * @param endDate   end date (inclusive)
     * @return number of working days
     */
    public static long calculateWorkingDays(LocalDate startDate, LocalDate endDate) {

        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Dates cannot be null");
        }

        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }

        long workingDays = 0;
        LocalDate date = startDate;

        while (!date.isAfter(endDate)) {
            if (!(date.getDayOfWeek() == DayOfWeek.SATURDAY ||
                  date.getDayOfWeek() == DayOfWeek.SUNDAY)) {
                workingDays++;
            }
            date = date.plusDays(1);
        }

        return workingDays;
    }

    /**
     * Calculates difference in days between two dates.
     *
     * @param startDate start date
     * @param endDate end date
     * @return number of days
     */
    public static long calculateDaysBetween(LocalDate startDate, LocalDate endDate) {
        return ChronoUnit.DAYS.between(startDate, endDate);
    }

    /**
     * Checks if a date is in the past.
     *
     * @param date date to check
     * @return true if past date
     */
    public static boolean isPastDate(LocalDate date) {
        return date.isBefore(LocalDate.now());
    }

}