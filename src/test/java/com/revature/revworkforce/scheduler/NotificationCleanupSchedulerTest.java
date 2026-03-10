package com.revature.revworkforce.scheduler;

import com.revature.revworkforce.service.NotificationService;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

class NotificationCleanupSchedulerTest {

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private NotificationCleanupScheduler scheduler;

    public NotificationCleanupSchedulerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void cleanupExpiredNotifications_ShouldCallService() {

        scheduler.cleanupExpiredNotifications();

        verify(notificationService).deleteExpiredNotifications();
    }
}