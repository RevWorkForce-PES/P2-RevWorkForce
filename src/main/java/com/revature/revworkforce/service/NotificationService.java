package com.revature.revworkforce.service;

import com.revature.revworkforce.dto.NotificationDTO;
import com.revature.revworkforce.model.Announcement;
import com.revature.revworkforce.model.Employee;
import com.revature.revworkforce.model.Notification;
import com.revature.revworkforce.enums.NotificationPriority;
import com.revature.revworkforce.enums.NotificationType;

import java.util.List;

public interface NotificationService {

    // Creation
    void createNotification(Employee employee,
                            NotificationType type,
                            String title,
                            String message,
                            NotificationPriority priority);

    void createAnnouncementForAll(Announcement announcement);

    // Retrieval
    List<NotificationDTO> getEmployeeNotifications(String employeeId);

    List<NotificationDTO> getUnreadNotifications(String employeeId);

    List<NotificationDTO> getRecentNotifications(String employeeId, int limit);

    long getUnreadCount(String employeeId);

    // Read operations
    void markAsRead(Long notificationId, String employeeId);

    void markAllAsRead(String employeeId);

    // Delete
    void deleteNotification(Long notificationId, String employeeId);

    void deleteExpiredNotifications();
   }