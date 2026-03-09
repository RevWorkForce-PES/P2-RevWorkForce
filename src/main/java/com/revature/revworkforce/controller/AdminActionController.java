package com.revature.revworkforce.controller;

import com.revature.revworkforce.dto.AnnouncementDTO;
import com.revature.revworkforce.security.SecurityUtils;
import com.revature.revworkforce.service.AnnouncementService;
import com.revature.revworkforce.service.AuditService;
import com.revature.revworkforce.service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

/**
 * Admin Action Controller.
 * Handles specialized administrative tasks from the Audit & Reports dashboard.
 */
@Controller
@RequestMapping("/admin")
public class AdminActionController {

    @Autowired
    private AuditService auditService;

    @Autowired
    private AnnouncementService announcementService;

    @Autowired
    private LeaveService leaveService;

    @PostMapping("/jobs/trigger")
    public String triggerJob(@RequestParam String jobName, RedirectAttributes redirectAttributes) {

        if ("Audit Cleanup".equalsIgnoreCase(jobName)) {
            auditService.cleanOldAuditLogs();
            redirectAttributes.addFlashAttribute("successMessage", "Audit Cleanup job triggered successfully.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Unknown job name: " + jobName);
        }

        return "redirect:/admin/audit-reports";
    }

    @PostMapping("/notifications/broadcast")
    public String broadcastAnnouncement(
            @RequestParam String subject,
            @RequestParam String message,
            @RequestParam String audience,
            RedirectAttributes redirectAttributes) {

        String currentAdmin = SecurityUtils.getCurrentUsername();

        AnnouncementDTO dto = new AnnouncementDTO();
        dto.setTitle(subject);
        dto.setContent(message);
        dto.setType("INFO"); // Defaulting to INFO for broadcast
        dto.setExpiryDate(LocalDate.now().plusDays(7)); // Default 1 week expiry
        dto.setIsActive('Y');

        // The audience field can be used for more specific logic if needed,
        // but for now we create a general announcement.

        announcementService.createAnnouncement(dto, currentAdmin);

        redirectAttributes.addFlashAttribute("successMessage", "Announcement broadcasted to " + audience + ".");
        return "redirect:/admin/audit-reports";
    }

    @PostMapping("/leave/revoke/{id}")
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN')")
    public String revokeLeave(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        String currentAdmin = SecurityUtils.getCurrentUsername();

        try {
            leaveService.revokeLeave(id, currentAdmin);
            redirectAttributes.addFlashAttribute("successMessage", "Leave application #" + id + " has been revoked.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to revoke leave: " + e.getMessage());
        }

        return "redirect:/admin/audit-reports";
    }
}
