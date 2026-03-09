package com.revature.revworkforce.controller;

import org.springframework.security.core.Authentication;
import com.revature.revworkforce.dto.NotificationDTO;
import com.revature.revworkforce.service.NotificationService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/notifications")
public class NotificationViewController {

    private final NotificationService notificationService;

    // ✅ Constructor name fixed
    public NotificationViewController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // ================================
    // View All Notifications
    // ================================
    @GetMapping
    public String viewAllNotifications(Authentication authentication,
            Model model) {

        String employeeId = authentication.getName();

        // Notifications are now marked as read manually by the user
        // Load updated notifications
        List<NotificationDTO> notifications = notificationService.getEmployeeNotifications(employeeId);

        model.addAttribute("notifications", notifications);

        return "pages/notifications/list";
    }
    

    // ================================
    // View Unread Notifications
    // ================================
    @GetMapping("/unread")
    public String viewUnread(Authentication authentication,
            Model model) {

        String employeeId = authentication.getName();

        List<NotificationDTO> notifications = notificationService.getUnreadNotifications(employeeId);

        model.addAttribute("notifications", notifications);

        return "pages/notifications/unread";
    }

    // ================================
    // Mark Single Notification as Read (Form)
    // ================================
    @PostMapping("/mark-read/{id}")
    public String markAsRead(@PathVariable Long id,
            Authentication authentication) {

        String employeeId = authentication.getName();

        notificationService.markAsRead(id, employeeId);

        return "redirect:/notifications";
    }

    // ================================
    // Mark All as Read (Form)
    // ================================
    @PostMapping("/mark-all-read")
    public String markAll(Authentication authentication) {

        String employeeId = authentication.getName();

        notificationService.markAllAsRead(employeeId);

        return "redirect:/notifications";
    }

    // ================================
    // Delete Notification
    // ================================
    @PostMapping("/delete/{id}")
    public String deleteNotification(@PathVariable Long id,
            Authentication authentication) {

        String employeeId = authentication.getName();

        notificationService.deleteNotification(id, employeeId);

        return "redirect:/notifications";
    }
}