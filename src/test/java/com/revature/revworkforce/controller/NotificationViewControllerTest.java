//package com.revature.revworkforce.controller;
//
//import com.revature.revworkforce.dto.NotificationDTO;
//import com.revature.revworkforce.service.NotificationService;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.List;
//
//import static org.mockito.Mockito.*;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(NotificationViewController.class)
//@AutoConfigureMockMvc(addFilters = false)
//class NotificationViewControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private NotificationService notificationService;
//
//    // Required for Spring Security
//    @MockBean
//    private UserDetailsService userDetailsService;
//
//    // =========================================================
//    // VIEW ALL NOTIFICATIONS
//    // =========================================================
//    @Test
//    @WithMockUser(username = "EMP001", roles = {"EMPLOYEE"})
//    void viewAllNotifications_ReturnsNotificationListPage() throws Exception {
//
//        when(notificationService.getEmployeeNotifications("EMP001"))
//                .thenReturn(List.of(new NotificationDTO()));
//
//        mockMvc.perform(get("/notifications"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("pages/notifications/list"));
//    }
//
//    // =========================================================
//    // VIEW UNREAD NOTIFICATIONS
//    // =========================================================
//    @Test
//    @WithMockUser(username = "EMP001", roles = {"EMPLOYEE"})
//    void viewUnread_ReturnsUnreadPage() throws Exception {
//
//        when(notificationService.getUnreadNotifications("EMP001"))
//                .thenReturn(List.of(new NotificationDTO()));
//
//        mockMvc.perform(get("/notifications/unread"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("pages/notifications/unread"));
//    }
//
//    // =========================================================
//    // MARK SINGLE NOTIFICATION AS READ
//    // =========================================================
//    @Test
//    @WithMockUser(username = "EMP001", roles = {"EMPLOYEE"})
//    void markAsRead_RedirectsToNotifications() throws Exception {
//
//        doNothing().when(notificationService).markAsRead(1L, "EMP001");
//
//        mockMvc.perform(post("/notifications/mark-read/1")
//                        .with(csrf()))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/notifications"));
//    }
//
//    // =========================================================
//    // MARK ALL NOTIFICATIONS AS READ
//    // =========================================================
//    @Test
//    @WithMockUser(username = "EMP001", roles = {"EMPLOYEE"})
//    void markAll_RedirectsToNotifications() throws Exception {
//
//        doNothing().when(notificationService).markAllAsRead("EMP001");
//
//        mockMvc.perform(post("/notifications/mark-all-read")
//                        .with(csrf()))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/notifications"));
//    }
//
//    // =========================================================
//    // DELETE NOTIFICATION
//    // =========================================================
//    @Test
//    @WithMockUser(username = "EMP001", roles = {"EMPLOYEE"})
//    void deleteNotification_RedirectsToNotifications() throws Exception {
//
//        doNothing().when(notificationService).deleteNotification(1L, "EMP001");
//
//        mockMvc.perform(post("/notifications/delete/1")
//                        .with(csrf()))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/notifications"));
//    }
//}