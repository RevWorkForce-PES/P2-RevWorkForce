package com.revature.revworkforce.repository;

import com.revature.revworkforce.model.Holiday;
import com.revature.revworkforce.service.HolidayService;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Holiday entity.
 * 
 * Provides CRUD operations and custom query methods for Holiday.
 * 
 * @author RevWorkForce Team
 */
@Repository
public interface HolidayRepository extends JpaRepository<Holiday, Long> {
	
    /**
     * Find holiday by date.
     * 
     * @param holidayDate the holiday date
     * @return Optional containing the holiday if found
     */
    Optional<Holiday> findByHolidayDate(LocalDate holidayDate);
    
    /**
     * Find all holidays in a year.
     * 
     * @param year the year
     * @return list of holidays
     */
    @Query("""
    	    SELECT h FROM Holiday h
    	    WHERE EXTRACT(YEAR FROM h.holidayDate) = :year
    	    AND h.isActive = 'Y'
    	    ORDER BY h.holidayDate
    	""")
    	List<Holiday> findByYear(@Param("year") Integer year);
    
    @Query("""
    	    SELECT h FROM Holiday h
    	    WHERE h.holidayDate BETWEEN :startDate AND :endDate
    	    AND h.isActive = 'Y'
    	    ORDER BY h.holidayDate
    	""")
    	List<Holiday> findByDateRange(LocalDate startDate,
    	                              LocalDate endDate);
    
    /**
     * Find holidays by type.
     * 
     * @param holidayType the holiday type (National, Regional, Festival, etc.)
     * @return list of holidays
     */
    List<Holiday> findByHolidayType(String holidayType);
    
    @Query("""
    	    SELECT h FROM Holiday h
    	    WHERE h.holidayDate >= :today
    	    AND h.isActive = 'Y'
    	    ORDER BY h.holidayDate
    	""")
    	List<Holiday> findUpcomingHolidays(@Param("today") LocalDate today);
   
    List<Holiday> findByIsActiveOrderByHolidayDateAsc(Character isActive);
    
    boolean existsByHolidayDateAndIsActive(LocalDate date, Character isActive); 
    /**
     * Count holidays in a date range.
     * 
     * @param startDate the start date
     * @param endDate the end date
     * @return count of holidays
     */
    @Query("SELECT COUNT(h) FROM Holiday h WHERE h.holidayDate BETWEEN :startDate AND :endDate")
    long countHolidaysInRange(@Param("startDate") LocalDate startDate, 
                              @Param("endDate") LocalDate endDate);
    
}