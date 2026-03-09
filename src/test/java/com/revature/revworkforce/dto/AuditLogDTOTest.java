package com.revature.revworkforce.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AuditLogDTOTest {

    @Test
    void testGettersAndSetters() {
        AuditLogDTO dto = new AuditLogDTO();

        dto.setAuditId(100L);
        dto.setPerformedBy("E123");
        dto.setPerformedByName("John Doe");
        dto.setAction("INSERT");
        dto.setTableName("employees");
        dto.setRecordId("1");
        dto.setOldValue("{\"name\":\"Old\"}");
        dto.setNewValue("{\"name\":\"New\"}");
        dto.setIpAddress("127.0.0.1");
        dto.setUserAgent("Chrome");
        LocalDateTime now = LocalDateTime.now();
        dto.setPerformedAt(now);

        assertEquals(100L, dto.getAuditId());
        assertEquals("E123", dto.getPerformedBy());
        assertEquals("John Doe", dto.getPerformedByName());
        assertEquals("INSERT", dto.getAction());
        assertEquals("Created", dto.getActionLabel()); // derived
        assertEquals("employees", dto.getTableName());
        assertEquals("1", dto.getRecordId());
        assertEquals("{\"name\":\"Old\"}", dto.getOldValue());
        assertEquals("{\"name\":\"New\"}", dto.getNewValue());
        assertEquals("127.0.0.1", dto.getIpAddress());
        assertEquals("Chrome", dto.getUserAgent());
        assertNotNull(dto.getPerformedAt());
        assertNotNull(dto.getTimeAgo());
    }

    @Test
    void testActionLabelMapping() {
        AuditLogDTO dto = new AuditLogDTO();

        dto.setAction("INSERT");
        assertEquals("Created", dto.getActionLabel());

        dto.setAction("UPDATE");
        assertEquals("Updated", dto.getActionLabel());

        dto.setAction("DELETE");
        assertEquals("Deleted", dto.getActionLabel());

        dto.setAction("LOGIN");
        assertEquals("Logged In", dto.getActionLabel());

        dto.setAction("LOGOUT");
        assertEquals("Logged Out", dto.getActionLabel());

        dto.setAction("APPROVE");
        assertEquals("Approved", dto.getActionLabel());

        dto.setAction("REJECT");
        assertEquals("Rejected", dto.getActionLabel());

        dto.setAction("CUSTOM");
        assertEquals("CUSTOM", dto.getActionLabel());
    }

    @Test
    void testTimeAgoCalculation() {
        AuditLogDTO dto = new AuditLogDTO();
        LocalDateTime now = LocalDateTime.now();

        // Just now
        dto.setPerformedAt(now);
        assertEquals("Just now", dto.getTimeAgo());

        // 5 minutes ago
        dto.setPerformedAt(now.minusMinutes(5));
        assertEquals("5 minutes ago", dto.getTimeAgo());

        // 1 hour ago
        dto.setPerformedAt(now.minusHours(1));
        assertEquals("1 hour ago", dto.getTimeAgo());

        // 3 hours ago
        dto.setPerformedAt(now.minusHours(3));
        assertEquals("3 hours ago", dto.getTimeAgo());

        // 1 day ago
        dto.setPerformedAt(now.minusDays(1));
        assertEquals("1 day ago", dto.getTimeAgo());

        // 5 days ago
        dto.setPerformedAt(now.minusDays(5));
        assertEquals("5 days ago", dto.getTimeAgo());
    }
}