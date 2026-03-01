package com.revature.revworkforce.service;

import com.revature.revworkforce.model.Announcement;
import com.revature.revworkforce.model.Employee;
import com.revature.revworkforce.enums.NotificationType;
import com.revature.revworkforce.enums.NotificationPriority;
import com.revature.revworkforce.model.Notification;

public interface NotificationService {
    void createAnnouncementForAll(Announcement announcement);

    void createNotification(Employee employee, NotificationType type, String title, String message,
            NotificationPriority priority);
}
