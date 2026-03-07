package com.revature.revworkforce.scheduler;

import com.revature.revworkforce.service.NotificationService;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class NotificationCleanupScheduler {

    private final NotificationService notificationService;

    public NotificationCleanupScheduler(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // Runs daily at 2:00 AM
    @Scheduled(cron = "0 0 2 * * ?")
    public void cleanupExpiredNotifications() {
        notificationService.deleteExpiredNotifications();
    }
}