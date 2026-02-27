package com.revature.revworkforce.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Date Utility Class.
 * 
 * Provides utility methods for date operations including:
 * - Working days calculation (excluding weekends and holidays)
 * - Date formatting
 * - Date validation
 * - Date range operations
 * 
 * @author RevWorkForce Team
 */
public class DateUtil {
    
    // Date formatters
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    public static final DateTimeFormatter DISPLAY_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd MMM yyyy");
    public static final DateTimeFormatter DISPLAY_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");
    
    /**
     * Calculate working days between two dates (excluding weekends and holidays).
     * 
     * Algorithm:
     * 1. Iterate through each day from start to end
     * 2. Check if day is Saturday or Sunday (skip)
     * 3. Check if day is a holiday (skip)
     * 4. Count valid working days
     * 
     * @param startDate the start date (inclusive)
     * @param endDate the end date (inclusive)
     * @param holidays set of holiday dates
     * @return number of working days
     */
    public static int calculateWorkingDays(LocalDate startDate, LocalDate endDate, Set<LocalDate> holidays) {
        if (startDate == null || endDate == null) {
            return 0;
        }
        
        if (startDate.isAfter(endDate)) {
            return 0;
        }
        
        int workingDays = 0;
        LocalDate currentDate = startDate;
        
        while (!currentDate.isAfter(endDate)) {
            // Check if it's a weekend
            DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
            boolean isWeekend = (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY);
            
            // Check if it's a holiday
            boolean isHoliday = (holidays != null && holidays.contains(currentDate));
            
            // Count if it's a working day
            if (!isWeekend && !isHoliday) {
                workingDays++;
            }
            
            currentDate = currentDate.plusDays(1);
        }
        
        return workingDays;
    }
    
    /**
     * Calculate working days between two dates (excluding only weekends).
     * 
     * @param startDate the start date (inclusive)
     * @param endDate the end date (inclusive)
     * @return number of working days
     */
    public static int calculateWorkingDays(LocalDate startDate, LocalDate endDate) {
        return calculateWorkingDays(startDate, endDate, null);
    }
    
    /**
     * Check if a date is a weekend (Saturday or Sunday).
     * 
     * @param date the date to check
     * @return true if weekend
     */
    public static boolean isWeekend(LocalDate date) {
        if (date == null) {
            return false;
        }
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }
    
    /**
     * Check if a date is a working day (not weekend and not holiday).
     * 
     * @param date the date to check
     * @param holidays set of holiday dates
     * @return true if working day
     */
    public static boolean isWorkingDay(LocalDate date, Set<LocalDate> holidays) {
        if (date == null) {
            return false;
        }
        
        if (isWeekend(date)) {
            return false;
        }
        
        if (holidays != null && holidays.contains(date)) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Get all dates between start and end (inclusive).
     * 
     * @param startDate the start date
     * @param endDate the end date
     * @return list of dates
     */
    public static List<LocalDate> getDateRange(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> dates = new ArrayList<>();
        
        if (startDate == null || endDate == null || startDate.isAfter(endDate)) {
            return dates;
        }
        
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            dates.add(currentDate);
            currentDate = currentDate.plusDays(1);
        }
        
        return dates;
    }
    
    /**
     * Get all working days between start and end (excluding weekends and holidays).
     * 
     * @param startDate the start date
     * @param endDate the end date
     * @param holidays set of holiday dates
     * @return list of working days
     */
    public static List<LocalDate> getWorkingDays(LocalDate startDate, LocalDate endDate, Set<LocalDate> holidays) {
        List<LocalDate> workingDays = new ArrayList<>();
        
        if (startDate == null || endDate == null || startDate.isAfter(endDate)) {
            return workingDays;
        }
        
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            if (isWorkingDay(currentDate, holidays)) {
                workingDays.add(currentDate);
            }
            currentDate = currentDate.plusDays(1);
        }
        
        return workingDays;
    }
    
    /**
     * Calculate total days between two dates (inclusive).
     * 
     * @param startDate the start date
     * @param endDate the end date
     * @return number of days
     */
    public static long calculateTotalDays(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null || startDate.isAfter(endDate)) {
            return 0;
        }
        return ChronoUnit.DAYS.between(startDate, endDate) + 1; // +1 to make it inclusive
    }
    
    /**
     * Format date to display format (dd MMM yyyy).
     * 
     * @param date the date to format
     * @return formatted date string
     */
    public static String formatDate(LocalDate date) {
        if (date == null) {
            return "";
        }
        return date.format(DISPLAY_DATE_FORMATTER);
    }
    
    /**
     * Format date-time to display format (dd MMM yyyy HH:mm).
     * 
     * @param dateTime the date-time to format
     * @return formatted date-time string
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        return dateTime.format(DISPLAY_DATE_TIME_FORMATTER);
    }
    
    /**
     * Parse date string to LocalDate.
     * 
     * @param dateString the date string (dd-MM-yyyy)
     * @return LocalDate object
     */
    public static LocalDate parseDate(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(dateString, DATE_FORMATTER);
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Get current date.
     * 
     * @return current date
     */
    public static LocalDate getCurrentDate() {
        return LocalDate.now();
    }
    
    /**
     * Get current date-time.
     * 
     * @return current date-time
     */
    public static LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }
    
    /**
     * Get current year.
     * 
     * @return current year
     */
    public static int getCurrentYear() {
        return LocalDate.now().getYear();
    }
    
    /**
     * Check if date is in the past.
     * 
     * @param date the date to check
     * @return true if date is before today
     */
    public static boolean isPastDate(LocalDate date) {
        if (date == null) {
            return false;
        }
        return date.isBefore(LocalDate.now());
    }
    
    /**
     * Check if date is in the future.
     * 
     * @param date the date to check
     * @return true if date is after today
     */
    public static boolean isFutureDate(LocalDate date) {
        if (date == null) {
            return false;
        }
        return date.isAfter(LocalDate.now());
    }
    
    /**
     * Check if date is today.
     * 
     * @param date the date to check
     * @return true if date is today
     */
    public static boolean isToday(LocalDate date) {
        if (date == null) {
            return false;
        }
        return date.equals(LocalDate.now());
    }
    
    /**
     * Add days to a date.
     * 
     * @param date the date
     * @param days number of days to add
     * @return new date
     */
    public static LocalDate addDays(LocalDate date, int days) {
        if (date == null) {
            return null;
        }
        return date.plusDays(days);
    }
    
    /**
     * Add months to a date.
     * 
     * @param date the date
     * @param months number of months to add
     * @return new date
     */
    public static LocalDate addMonths(LocalDate date, int months) {
        if (date == null) {
            return null;
        }
        return date.plusMonths(months);
    }
    
    /**
     * Get first day of month.
     * 
     * @param date the date
     * @return first day of month
     */
    public static LocalDate getFirstDayOfMonth(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.withDayOfMonth(1);
    }
    
    /**
     * Get last day of month.
     * 
     * @param date the date
     * @return last day of month
     */
    public static LocalDate getLastDayOfMonth(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.withDayOfMonth(date.lengthOfMonth());
    }
    
    /**
     * Get first day of year.
     * 
     * @param year the year
     * @return first day of year
     */
    public static LocalDate getFirstDayOfYear(int year) {
        return LocalDate.of(year, 1, 1);
    }
    
    /**
     * Get last day of year.
     * 
     * @param year the year
     * @return last day of year
     */
    public static LocalDate getLastDayOfYear(int year) {
        return LocalDate.of(year, 12, 31);
    }
    
    /**
     * Check if two date ranges overlap.
     * 
     * @param start1 start of first range
     * @param end1 end of first range
     * @param start2 start of second range
     * @param end2 end of second range
     * @return true if ranges overlap
     */
    public static boolean doDateRangesOverlap(LocalDate start1, LocalDate end1, 
                                              LocalDate start2, LocalDate end2) {
        if (start1 == null || end1 == null || start2 == null || end2 == null) {
            return false;
        }
        
        // Two ranges overlap if: (start1 <= end2) AND (start2 <= end1)
        return !start1.isAfter(end2) && !start2.isAfter(end1);
    }
}