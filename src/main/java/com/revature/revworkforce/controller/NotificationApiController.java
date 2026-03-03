package com.revature.revworkforce.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.revature.revworkforce.dto.NotificationDTO;
import com.revature.revworkforce.service.NotificationService;

@RestController
@RequestMapping("/api/notifications")
public class NotificationApiController {

    private final NotificationService notificationService;

    public NotificationApiController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/unread-count")
    public long getUnreadCount(Authentication authentication) {
        String employeeId = authentication.getName();
        return notificationService.getUnreadCount(employeeId);
    }

    @GetMapping("/recent")
    public List<NotificationDTO> getRecentNotifications(
            @RequestParam(defaultValue = "5") int limit,
            Authentication authentication) {

        String employeeId = authentication.getName();
        return notificationService.getRecentNotifications(employeeId, limit);
    }

    @PostMapping("/mark-read/{id}")
    public void markReadApi(@PathVariable Long id,
                            Authentication authentication) {

        String employeeId = authentication.getName();
        notificationService.markAsRead(id, employeeId);
    }
}