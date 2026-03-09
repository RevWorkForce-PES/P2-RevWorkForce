package com.revature.revworkforce.controller;

import com.revature.revworkforce.dto.AnnouncementDTO;
import com.revature.revworkforce.model.Announcement;
import com.revature.revworkforce.security.SecurityUtils;
import com.revature.revworkforce.service.AnnouncementService;
import com.revature.revworkforce.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controller for managing announcements
 * 
 * @author RevWorkForce Team
 */
@Controller
public class AnnouncementController {

    private final AnnouncementService announcementService;
    private final EmployeeService employeeService;

    @Autowired
    public AnnouncementController(AnnouncementService announcementService,
                                  EmployeeService employeeService) {
        this.announcementService = announcementService;
        this.employeeService = employeeService;
    }

    // ========================================
    // ADMIN ENDPOINTS
    // ========================================

    /**
     * Show admin announcement list page
     */
    @GetMapping("/admin/announcements")
    @PreAuthorize("hasRole('ADMIN')")
    public String listAdminAnnouncements(Model model) {
        String employeeId = SecurityUtils.getCurrentUsername();
        String fullName = employeeService.getEmployeeById(employeeId).getFullName();
        
        List<Announcement> announcements = announcementService.getAllAnnouncements();
        
        model.addAttribute("announcements", announcements);
        model.addAttribute("fullName", fullName);
        model.addAttribute("userRole", "Admin");
        
        // Return path under frontend/pages/admin/
        return "pages/admin/announcements-management";
    }

    /**
     * Show create announcement form
     */
    @GetMapping("/admin/announcements/create")
    @PreAuthorize("hasRole('ADMIN')")
    public String showCreateAnnouncementForm(Model model) {
        String employeeId = SecurityUtils.getCurrentUsername();
        String fullName = employeeService.getEmployeeById(employeeId).getFullName();
        
        model.addAttribute("announcementDTO", new AnnouncementDTO());
        model.addAttribute("fullName", fullName);
        model.addAttribute("isEdit", false);
        
        return "pages/admin/announcement-form";
    }

    /**
     * Create new announcement
     */
    @PostMapping("/admin/announcements/create")
    @PreAuthorize("hasRole('ADMIN')")
    public String createAnnouncement(@ModelAttribute("announcementDTO") AnnouncementDTO dto,
                                     Authentication authentication,
                                     RedirectAttributes redirectAttributes) {
        try {
            String createdBy = authentication.getName();
            announcementService.createAnnouncement(dto, createdBy);
            redirectAttributes.addFlashAttribute("success", 
                "Announcement created and published successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", 
                "Error creating announcement: " + e.getMessage());
            return "redirect:/admin/announcements/create";
        }
        return "redirect:/admin/announcements";
    }

    /**
     * Show edit announcement form
     */
    @GetMapping("/admin/announcements/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String showEditAnnouncementForm(@PathVariable Long id, 
                                           Model model, 
                                           RedirectAttributes redirectAttributes) {
        try {
            String employeeId = SecurityUtils.getCurrentUsername();
            String fullName = employeeService.getEmployeeById(employeeId).getFullName();
            
            AnnouncementDTO dto = announcementService.getAnnouncementById(id);
            
            model.addAttribute("announcementDTO", dto);
            model.addAttribute("fullName", fullName);
            model.addAttribute("isEdit", true);
            
            return "pages/admin/announcement-form";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", 
                "Announcement not found: " + e.getMessage());
            return "redirect:/admin/announcements";
        }
    }

    /**
     * Update announcement
     */
    @PostMapping("/admin/announcements/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateAnnouncement(@PathVariable Long id,
                                     @ModelAttribute("announcementDTO") AnnouncementDTO dto,
                                     RedirectAttributes redirectAttributes) {
        try {
            announcementService.updateAnnouncement(id, dto);
            redirectAttributes.addFlashAttribute("success", 
                "Announcement updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", 
                "Error updating announcement: " + e.getMessage());
            return "redirect:/admin/announcements/edit/" + id;
        }
        return "redirect:/admin/announcements";
    }

    /**
     * Deactivate announcement
     */
    @PostMapping("/admin/announcements/deactivate/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deactivateAnnouncement(@PathVariable Long id, 
                                         RedirectAttributes redirectAttributes) {
        try {
            announcementService.deactivateAnnouncement(id);
            redirectAttributes.addFlashAttribute("success", 
                "Announcement deactivated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", 
                "Error deactivating announcement: " + e.getMessage());
        }
        return "redirect:/admin/announcements";
    }

    /**
     * Delete announcement
     */
    @PostMapping("/admin/announcements/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteAnnouncement(@PathVariable Long id, 
                                     RedirectAttributes redirectAttributes) {
        try {
            announcementService.deleteAnnouncement(id);
            redirectAttributes.addFlashAttribute("success", 
                "Announcement deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", 
                "Error deleting announcement: " + e.getMessage());
        }
        return "redirect:/admin/announcements";
    }

    // ========================================
    // EMPLOYEE ENDPOINTS
    // ========================================

    /**
     * Show active announcements for all employees
     */
    @GetMapping("/employee/announcements")
    public String viewActiveAnnouncements(Model model) {
        String employeeId = SecurityUtils.getCurrentUsername();
        String fullName = employeeService.getEmployeeById(employeeId).getFullName();
        
        List<AnnouncementDTO> activeAnnouncements = announcementService.getActiveAnnouncements();
        
        model.addAttribute("announcements", activeAnnouncements);
        model.addAttribute("fullName", fullName);
        model.addAttribute("userRole", "Employee");
        
        // Return path under frontend/pages/employee/
        return "pages/employee/announcements";
    }

    /**
     * View announcement details
     */
    @GetMapping("/employee/announcements/{id}")
    public String viewAnnouncementDetails(@PathVariable Long id, 
                                          Model model, 
                                          RedirectAttributes redirectAttributes) {
        try {
            String employeeId = SecurityUtils.getCurrentUsername();
            String fullName = employeeService.getEmployeeById(employeeId).getFullName();
            
            AnnouncementDTO announcement = announcementService.getAnnouncementById(id);
            
            model.addAttribute("announcement", announcement);
            model.addAttribute("fullName", fullName);
            model.addAttribute("userRole", "Employee");
            
            return "pages/employee/announcement-detail";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", 
                "Announcement not found or has been removed.");
            return "redirect:/employee/announcements";
        }
    }

    /**
     * Manager can also view announcements
     */
    @GetMapping("/manager/announcements")
    public String viewManagerAnnouncements(Model model) {
        String employeeId = SecurityUtils.getCurrentUsername();
        String fullName = employeeService.getEmployeeById(employeeId).getFullName();
        
        List<AnnouncementDTO> activeAnnouncements = announcementService.getActiveAnnouncements();
        
        model.addAttribute("announcements", activeAnnouncements);
        model.addAttribute("fullName", fullName);
        model.addAttribute("userRole", "Manager");
        
        return "pages/employee/announcements";
    }

    /**
     * Manager announcement details
     */
    @GetMapping("/manager/announcements/{id}")
    public String viewManagerAnnouncementDetails(@PathVariable Long id, 
                                                 Model model, 
                                                 RedirectAttributes redirectAttributes) {
        try {
            String employeeId = SecurityUtils.getCurrentUsername();
            String fullName = employeeService.getEmployeeById(employeeId).getFullName();
            
            AnnouncementDTO announcement = announcementService.getAnnouncementById(id);
            
            model.addAttribute("announcement", announcement);
            model.addAttribute("fullName", fullName);
            model.addAttribute("userRole", "Manager");
            
            return "pages/employee/announcement-detail";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", 
                "Announcement not found or has been removed.");
            return "redirect:/manager/announcements";
        }
    }
}