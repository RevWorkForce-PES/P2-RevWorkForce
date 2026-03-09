package com.revature.revworkforce.controller;

import com.revature.revworkforce.dto.NotificationDTO;
import com.revature.revworkforce.service.NotificationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class NotificationViewControllerTest {

    private MockMvc mockMvc;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private NotificationViewController notificationViewController;

    private final String EMPLOYEE_ID = "EMP001";

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(notificationViewController).build();
    }

    // =============================
    // View All Notifications
    // =============================
    @Test
    void viewAllNotifications_ShouldReturnListPage() throws Exception {

        when(notificationService.getEmployeeNotifications(EMPLOYEE_ID))
                .thenReturn(List.of(new NotificationDTO()));

        Authentication auth =
                new UsernamePasswordAuthenticationToken(EMPLOYEE_ID, null);

        mockMvc.perform(get("/notifications").principal(auth))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/notifications/list"))
                .andExpect(model().attributeExists("notifications"));
    }

    // =============================
    // View Unread Notifications
    // =============================
    @Test
    void viewUnread_ShouldReturnUnreadPage() throws Exception {

        when(notificationService.getUnreadNotifications(EMPLOYEE_ID))
                .thenReturn(List.of());

        Authentication auth =
                new UsernamePasswordAuthenticationToken(EMPLOYEE_ID, null);

        mockMvc.perform(get("/notifications/unread").principal(auth))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/notifications/unread"))
                .andExpect(model().attributeExists("notifications"));
    }

    // =============================
    // Mark Read
    // =============================
    @Test
    void markAsRead_ShouldRedirect() throws Exception {

        Authentication auth =
                new UsernamePasswordAuthenticationToken(EMPLOYEE_ID, null);

        mockMvc.perform(post("/notifications/mark-read/1").principal(auth))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/notifications"));

        verify(notificationService).markAsRead(1L, EMPLOYEE_ID);
    }

    // =============================
    // Mark All Read
    // =============================
    @Test
    void markAll_ShouldRedirect() throws Exception {

        Authentication auth =
                new UsernamePasswordAuthenticationToken(EMPLOYEE_ID, null);

        mockMvc.perform(post("/notifications/mark-all-read").principal(auth))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/notifications"));

        verify(notificationService).markAllAsRead(EMPLOYEE_ID);
    }

    // =============================
    // Delete Notification
    // =============================
    @Test
    void deleteNotification_ShouldRedirect() throws Exception {

        Authentication auth =
                new UsernamePasswordAuthenticationToken(EMPLOYEE_ID, null);

        mockMvc.perform(post("/notifications/delete/1").principal(auth))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/notifications"));

        verify(notificationService).deleteNotification(1L, EMPLOYEE_ID);
    }
}