package com.revature.revworkforce.repository;

import com.revature.revworkforce.model.Holiday;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface HolidayRepository extends JpaRepository<Holiday, Long> {
	
    // ============================================
    // BASIC
    // ============================================

    Optional<Holiday> findByHolidayDate(LocalDate holidayDate);

    List<Holiday> findAllByOrderByHolidayDateAsc();

    List<Holiday> findByIsActiveOrderByHolidayDateAsc(Character isActive);

    boolean existsByHolidayDateAndIsActive(LocalDate date, Character isActive);

    // ============================================
    // YEAR QUERY
    // ============================================

    @Query("""
        SELECT h FROM Holiday h
        WHERE EXTRACT(YEAR FROM h.holidayDate) = :year
        AND h.isActive = 'Y'
        ORDER BY h.holidayDate
    """)
    List<Holiday> findByYear(@Param("year") Integer year);

    // ============================================
    // DATE RANGE
    // ============================================

    @Query("""
        SELECT h FROM Holiday h
        WHERE h.holidayDate BETWEEN :startDate AND :endDate
        AND h.isActive = 'Y'
        ORDER BY h.holidayDate
    """)
    List<Holiday> findByDateRange(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    long countByHolidayDateBetweenAndIsActive(
            LocalDate start,
            LocalDate end,
            Character isActive
    );

    // ============================================
    // UPCOMING
    // ============================================

    @Query("""
        SELECT h FROM Holiday h
        WHERE h.holidayDate >= :today
        AND h.isActive = 'Y'
        ORDER BY h.holidayDate
    """)
    List<Holiday> findUpcomingHolidays(@Param("today") LocalDate today);
    List<Holiday> findByHolidayDateBetweenAndIsActive(
            LocalDate start,
            LocalDate end,
            Character isActive
    );
    
    List<Holiday> findByHolidayDateAfterAndIsActiveOrderByHolidayDateAsc(
            LocalDate date,
            Character isActive
    );
    List<Holiday> findByHolidayDateAfterOrderByHolidayDateAsc(LocalDate date);
    
}