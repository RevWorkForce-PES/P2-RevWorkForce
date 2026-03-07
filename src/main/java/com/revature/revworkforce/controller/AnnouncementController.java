package com.revature.revworkforce.controller;

import com.revature.revworkforce.dto.AnnouncementDTO;
import com.revature.revworkforce.model.Announcement;
import com.revature.revworkforce.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class AnnouncementController {

    private final AnnouncementService announcementService;

    @Autowired
    public AnnouncementController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    // --- Admin Endpoints ---

    @GetMapping("/admin/announcements")
    @PreAuthorize("hasRole('ADMIN')")
    public String listAdminAnnouncements(Model model) {
        List<Announcement> announcements = announcementService.getAllAnnouncements();
        model.addAttribute("announcements", announcements);
        model.addAttribute("pageTitle", "Announcement Management");
        return "admin/announcements/list";
    }

    @GetMapping("/admin/announcements/create")
    @PreAuthorize("hasRole('ADMIN')")
    public String showCreateAnnouncementForm(Model model) {
        model.addAttribute("announcementDTO", new AnnouncementDTO());
        model.addAttribute("pageTitle", "Create Announcement");
        model.addAttribute("isEdit", false);
        return "admin/announcements/form";
    }

    @PostMapping("/admin/announcements/create")
    @PreAuthorize("hasRole('ADMIN')")
    public String createAnnouncement(@ModelAttribute("announcementDTO") AnnouncementDTO dto,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {
        try {
            String createdBy = authentication.getName();
            announcementService.createAnnouncement(dto, createdBy);
            redirectAttributes.addFlashAttribute("success", "Announcement created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error creating announcement: " + e.getMessage());
            return "redirect:/admin/announcements/create";
        }
        return "redirect:/admin/announcements";
    }

    @GetMapping("/admin/announcements/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String showEditAnnouncementForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            AnnouncementDTO dto = announcementService.getAnnouncementById(id);
            model.addAttribute("announcementDTO", dto);
            model.addAttribute("pageTitle", "Edit Announcement");
            model.addAttribute("isEdit", true);
            return "admin/announcements/form";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Announcement not found: " + e.getMessage());
            return "redirect:/admin/announcements";
        }
    }

    @PostMapping("/admin/announcements/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateAnnouncement(@PathVariable Long id,
            @ModelAttribute("announcementDTO") AnnouncementDTO dto,
            RedirectAttributes redirectAttributes) {
        try {
            announcementService.updateAnnouncement(id, dto);
            redirectAttributes.addFlashAttribute("success", "Announcement updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating announcement: " + e.getMessage());
            return "redirect:/admin/announcements/edit/" + id;
        }
        return "redirect:/admin/announcements";
    }

    @PostMapping("/admin/announcements/deactivate/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deactivateAnnouncement(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            announcementService.deactivateAnnouncement(id);
            redirectAttributes.addFlashAttribute("success", "Announcement deactivated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deactivating announcement.");
        }
        return "redirect:/admin/announcements";
    }

    @PostMapping("/admin/announcements/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteAnnouncement(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            announcementService.deleteAnnouncement(id);
            redirectAttributes.addFlashAttribute("success", "Announcement deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting announcement.");
        }
        return "redirect:/admin/announcements";
    }

    // --- Employee Endpoints ---

    @GetMapping("/announcements")
    public String viewActiveAnnouncements(Model model) {
        List<AnnouncementDTO> activeAnnouncements = announcementService.getActiveAnnouncements();
        model.addAttribute("announcements", activeAnnouncements);
        model.addAttribute("pageTitle", "Company Announcements");
        return "pages/employee/announcements";
    }

    @GetMapping("/announcements/{id}")
    public String viewAnnouncementDetails(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            AnnouncementDTO announcement = announcementService.getAnnouncementById(id);
            model.addAttribute("announcement", announcement);
            model.addAttribute("pageTitle", announcement.getTitle());
            return "pages/employee/announcement-detail";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Announcement not found.");
            return "redirect:/announcements";
        }
    }
}
