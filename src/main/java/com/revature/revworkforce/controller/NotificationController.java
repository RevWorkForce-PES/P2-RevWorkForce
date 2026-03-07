package com.revature.revworkforce.controller;

import com.revature.revworkforce.dto.NotificationDTO;
import com.revature.revworkforce.service.NotificationService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /** GET /notifications - fetch all notifications for current user */
    @GetMapping
    @ResponseBody
    public ResponseEntity<List<NotificationDTO>> getNotifications(Authentication auth) {
        return ResponseEntity.ok(notificationService.getEmployeeNotifications(auth.getName()));
    }

    /** GET /notifications/unread - fetch unread notifications only */
    @GetMapping("/unread")
    @ResponseBody
    public ResponseEntity<List<NotificationDTO>> getUnreadNotifications(Authentication auth) {
        return ResponseEntity.ok(notificationService.getUnreadNotifications(auth.getName()));
    }

    /** GET /notifications/count - get unread count for bell badge */
    @GetMapping("/count")
    @ResponseBody
    public ResponseEntity<Map<String, Long>> getUnreadCount(Authentication auth) {
        long count = notificationService.getUnreadCount(auth.getName());
        return ResponseEntity.ok(Map.of("count", count));
    }

    /** POST /notifications/{id}/mark-read - mark a single notification as read */
    @PostMapping("/{id}/mark-read")
    @ResponseBody
    public ResponseEntity<String> markAsRead(@PathVariable Long id, Authentication auth) {
        notificationService.markAsRead(id, auth.getName());
        return ResponseEntity.ok("Notification marked as read.");
    }

    /** POST /notifications/mark-all-read - mark all notifications as read */
    @PostMapping("/mark-all-read")
    @ResponseBody
    public ResponseEntity<String> markAllAsRead(Authentication auth) {
        notificationService.markAllAsRead(auth.getName());
        return ResponseEntity.ok("All notifications marked as read.");
    }
}