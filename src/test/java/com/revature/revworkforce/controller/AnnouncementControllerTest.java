package com.revature.revworkforce.controller;

import com.revature.revworkforce.dto.AnnouncementDTO;
import com.revature.revworkforce.model.Announcement;
import com.revature.revworkforce.repository.EmployeeRepository;
import com.revature.revworkforce.service.AnnouncementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AnnouncementController.class)
class AnnouncementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnnouncementService announcementService;

    @MockBean
    private EmployeeRepository employeeRepository;

    @Test
    @WithMockUser(roles = "ADMIN")
    void listAdminAnnouncements_Success() throws Exception {
        when(announcementService.getAllAnnouncements()).thenReturn(Arrays.asList(new Announcement()));

        mockMvc.perform(get("/admin/announcements"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/announcements/list"))
                .andExpect(model().attributeExists("announcements"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void createAnnouncement_Success() throws Exception {
        mockMvc.perform(post("/admin/announcements/create")
                .flashAttr("announcementDTO", new AnnouncementDTO())
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/announcements"))
                .andExpect(flash().attribute("success", "Announcement created successfully!"));

        verify(announcementService).createAnnouncement(any(AnnouncementDTO.class), eq("admin"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void showEditAnnouncementForm_Success() throws Exception {
        when(announcementService.getAnnouncementById(1L)).thenReturn(new AnnouncementDTO());

        mockMvc.perform(get("/admin/announcements/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/announcements/form"))
                .andExpect(model().attribute("isEdit", true));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateAnnouncement_Success() throws Exception {
        mockMvc.perform(post("/admin/announcements/edit/1")
                .flashAttr("announcementDTO", new AnnouncementDTO())
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/announcements"))
                .andExpect(flash().attribute("success", "Announcement updated successfully!"));

        verify(announcementService).updateAnnouncement(eq(1L), any(AnnouncementDTO.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deactivateAnnouncement_Success() throws Exception {
        mockMvc.perform(post("/admin/announcements/deactivate/1")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/announcements"))
                .andExpect(flash().attribute("success", "Announcement deactivated successfully!"));

        verify(announcementService).deactivateAnnouncement(1L);
    }

    @Test
    @WithMockUser(roles = "USER")
    void viewActiveAnnouncements_Success() throws Exception {
        when(announcementService.getActiveAnnouncements()).thenReturn(Arrays.asList(new AnnouncementDTO()));

        mockMvc.perform(get("/announcements"))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/employee/announcements"))
                .andExpect(model().attributeExists("announcements"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void viewAnnouncementDetails_Success() throws Exception {
        AnnouncementDTO dto = new AnnouncementDTO();
        dto.setTitle("Title");
        when(announcementService.getAnnouncementById(1L)).thenReturn(dto);

        mockMvc.perform(get("/announcements/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/employee/announcement-detail"))
                .andExpect(model().attribute("announcement", dto));
    }
}
