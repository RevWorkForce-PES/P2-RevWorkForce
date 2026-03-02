package com.revature.revworkforce.repository;

import com.revature.revworkforce.enums.NotificationPriority;
import com.revature.revworkforce.enums.Priority;
import com.revature.revworkforce.model.Employee;
import com.revature.revworkforce.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for Notification entity.
 * 
 * Provides CRUD operations and custom query methods for Notification.
 * 
 * @author RevWorkForce Team
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    /**
     * Find all notifications for an employee.
     * 
     * @param employee the employee
     * @return list of notifications
     */
    List<Notification> findByEmployeeOrderByCreatedAtDesc(Employee employee);
    
    /**
     * Find unread notifications for an employee.
     * 
     * @param employee the employee
     * @param isRead 'N' for unread
     * @return list of unread notifications
     */
    List<Notification> findByEmployeeAndIsReadOrderByCreatedAtDesc(Employee employee, Character isRead);
    
    /**
     * Find notifications by employee and type.
     * 
     * @param employee the employee
     * @param notificationType the notification type
     * @return list of notifications
     */
    List<Notification> findByEmployeeAndNotificationType(Employee employee, String notificationType);
    
    @Query("SELECT n FROM Notification n WHERE n.employee = :employee ORDER BY n.createdAt DESC")
    List<Notification> findRecentByEmployee(
            @Param("employee") Employee employee,
            Pageable pageable);
    /**
     * Count unread notifications for employee.
     * 
     * @param employee the employee
     * @param isRead 'N' for unread
     * @return count of unread notifications
     */
    long countByEmployeeAndIsRead(Employee employee, Character isRead);
    
    
    List<Notification> findByEmployeeAndPriorityAndIsReadOrderByCreatedAtDesc(
            Employee employee,
            NotificationPriority priority,
            Character isRead);
    
    /**
     * Mark notification as read.
     * 
     * @param notificationId the notification ID
     */
    @Modifying
    @Query("UPDATE Notification n SET n.isRead = 'Y', n.readAt = :readAt WHERE n.notificationId = :notificationId")
    void markAsRead(@Param("notificationId") Long notificationId, @Param("readAt") LocalDateTime readAt);
    
    /**
     * Mark all notifications as read for employee.
     * 
     * @param employee the employee
     * @param readAt the read timestamp
     */
    @Modifying
    @Query("UPDATE Notification n SET n.isRead = 'Y', n.readAt = :readAt WHERE n.employee = :employee AND n.isRead = 'N'")
    void markAllAsRead(@Param("employee") Employee employee, @Param("readAt") LocalDateTime readAt);
    
    /**
     * Delete expired notifications.
     * 
     * @param now the current timestamp
     */
    @Modifying
    @Query("DELETE FROM Notification n WHERE n.expiresAt < :now")
    void deleteExpiredNotifications(@Param("now") LocalDateTime now);
    
    /**
     * Find notifications by reference.
     * 
     * @param referenceType the reference type (e.g., "LEAVE_APPLICATION")
     * @param referenceId the reference ID
     * @return list of notifications
     */
    List<Notification> findByReferenceTypeAndReferenceId(String referenceType, Long referenceId);

}