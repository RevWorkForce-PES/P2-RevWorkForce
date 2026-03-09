package com.revature.revworkforce.dto;

import com.revature.revworkforce.enums.NotificationPriority;
import com.revature.revworkforce.enums.NotificationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class NotificationDTOTest {

    private NotificationDTO dto;

    @BeforeEach
    void setUp() {
        dto = new NotificationDTO();
    }

    @Test
    void testSettersAndGetters() {
        LocalDateTime now = LocalDateTime.now();

        dto.setNotificationId(101L);
        dto.setEmployeeId("E123");
        dto.setTitle("System Update");
        dto.setMessage("Your system will be down for maintenance.");
        dto.setNotificationType(NotificationType.SYSTEM);
        dto.setReferenceType("Task");
        dto.setReferenceId(1001L);
        dto.setIsRead('N');
        dto.setReadAt(now);
        dto.setPriority(NotificationPriority.HIGH);
        dto.setCreatedAt(now);
        dto.setExpiresAt(now.plusDays(7));

        assertEquals(101L, dto.getNotificationId());
        assertEquals("E123", dto.getEmployeeId());
        assertEquals("System Update", dto.getTitle());
        assertEquals("Your system will be down for maintenance.", dto.getMessage());
        assertEquals(NotificationType.SYSTEM, dto.getNotificationType());
        assertEquals("Task", dto.getReferenceType());
        assertEquals(1001L, dto.getReferenceId());
        assertEquals('N', dto.getIsRead());
        assertEquals(now, dto.getReadAt());
        assertEquals(NotificationPriority.HIGH, dto.getPriority());
        assertEquals(now, dto.getCreatedAt());
        assertEquals(now.plusDays(7), dto.getExpiresAt());
    }
}