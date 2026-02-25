package com.revature.revworkforce.repository;

import com.revature.revworkforce.enums.Priority;
import com.revature.revworkforce.model.Announcement;
import com.revature.revworkforce.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for Announcement entity.
 * 
 * Provides CRUD operations and custom query methods for Announcement.
 * 
 * @author RevWorkForce Team
 */
@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    
    /**
     * Find all active announcements.
     * 
     * @param isActive 'Y' for active
     * @return list of active announcements
     */
    List<Announcement> findByIsActiveOrderByPublishDateDesc(Character isActive);
    
    /**
     * Find announcements by created by employee.
     * 
     * @param createdBy the employee who created the announcement
     * @return list of announcements
     */
    List<Announcement> findByCreatedByOrderByCreatedAtDesc(Employee createdBy);
    
    /**
     * Find announcements by type.
     * 
     * @param announcementType the announcement type
     * @return list of announcements
     */
    List<Announcement> findByAnnouncementType(String announcementType);
    
    /**
     * Find announcements by priority.
     * 
     * @param priority the priority
     * @return list of announcements
     */
    List<Announcement> findByPriorityOrderByPublishDateDesc(Priority priority);
    
    /**
     * Find current active announcements (published and not expired).
     * 
     * @param today the current date
     * @return list of current announcements
     */
    @Query("SELECT a FROM Announcement a WHERE a.isActive = 'Y' " +
           "AND (a.publishDate IS NULL OR a.publishDate <= :today) " +
           "AND (a.expiryDate IS NULL OR a.expiryDate >= :today) " +
           "ORDER BY a.publishDate DESC")
    List<Announcement> findCurrentAnnouncements(@Param("today") LocalDate today);
    
    /**
     * Find announcements by target audience.
     * 
     * @param targetAudience the target audience (ALL, DEPARTMENT, etc.)
     * @return list of announcements
     */
    List<Announcement> findByTargetAudience(String targetAudience);
    
    /**
     * Find high-priority active announcements.
     * 
     * @param priority the priority (HIGH or URGENT)
     * @param isActive 'Y' for active
     * @return list of announcements
     */
    List<Announcement> findByPriorityAndIsActiveOrderByPublishDateDesc(Priority priority, Character isActive);
    
    /**
     * Find upcoming announcements (publish date in future).
     * 
     * @param today the current date
     * @return list of upcoming announcements
     */
    @Query("SELECT a FROM Announcement a WHERE a.publishDate > :today ORDER BY a.publishDate")
    List<Announcement> findUpcomingAnnouncements(@Param("today") LocalDate today);
    
    /**
     * Count active announcements.
     * 
     * @param isActive 'Y' for active
     * @return count of active announcements
     */
    long countByIsActive(Character isActive);
}