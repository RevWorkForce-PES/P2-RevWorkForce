package com.revature.revworkforce.controller;

import com.revature.revworkforce.service.NotificationService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationApiController.class)
class NotificationApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    // =========================================================
    // GET UNREAD COUNT
    // =========================================================
    @Test
    @WithMockUser(username = "EMP001", roles = {"EMPLOYEE"})
    void getUnreadCount_ReturnsCount() throws Exception {

        when(notificationService.getUnreadCount("EMP001")).thenReturn(3L);

        mockMvc.perform(get("/api/notifications/unread-count"))
                .andExpect(status().isOk());
    }

    // =========================================================
    // GET RECENT NOTIFICATIONS
    // =========================================================
    @Test
    @WithMockUser(username = "EMP001", roles = {"EMPLOYEE"})
    void getRecentNotifications_ReturnsList() throws Exception {

        when(notificationService.getRecentNotifications("EMP001",5))
                .thenReturn(java.util.List.of());

        mockMvc.perform(get("/api/notifications/recent"))
                .andExpect(status().isOk());
    }

    // =========================================================
    // MARK SINGLE NOTIFICATION READ
    // =========================================================
    @Test
    @WithMockUser(username = "EMP001", roles = {"EMPLOYEE"})
    void markReadApi_ReturnsOk() throws Exception {

        doNothing().when(notificationService).markAsRead(1L,"EMP001");

        mockMvc.perform(post("/api/notifications/mark-read/1")
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    // =========================================================
    // MARK ALL NOTIFICATIONS READ
    // =========================================================
    @Test
    @WithMockUser(username = "EMP001", roles = {"EMPLOYEE"})
    void markAllReadApi_ReturnsOk() throws Exception {

        doNothing().when(notificationService).markAllAsRead("EMP001");

        mockMvc.perform(post("/api/notifications/mark-all-read")
                        .with(csrf()))
                .andExpect(status().isOk());
    }
}