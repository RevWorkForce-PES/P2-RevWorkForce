package com.revature.revworkforce.controller;

import com.revature.revworkforce.service.NotificationService;
import com.revature.revworkforce.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationApiController.class)
class NotificationApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    @MockBean
    private EmployeeRepository employeeRepository;

    @Test
    @WithMockUser(username = "user")
    void getUnreadCount_Success() throws Exception {
        when(notificationService.getUnreadCount("user")).thenReturn(5L);

        mockMvc.perform(get("/api/notifications/unread-count"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }

    @Test
    @WithMockUser(username = "user")
    void getRecentNotifications_Success() throws Exception {
        when(notificationService.getRecentNotifications(eq("user"), anyInt())).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/notifications/recent")
                .param("limit", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @WithMockUser(username = "user")
    void markReadApi_Success() throws Exception {
        mockMvc.perform(post("/api/notifications/mark-read/1")
                .with(csrf()))
                .andExpect(status().isOk());

        verify(notificationService).markAsRead(1L, "user");
    }

    @Test
    @WithMockUser(username = "user")
    void markAllReadApi_Success() throws Exception {
        mockMvc.perform(post("/api/notifications/mark-all-read")
                .with(csrf()))
                .andExpect(status().isOk());

        verify(notificationService).markAllAsRead("user");
    }
}
