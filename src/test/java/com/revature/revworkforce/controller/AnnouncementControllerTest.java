package com.revature.revworkforce.controller;

import com.revature.revworkforce.dto.AnnouncementDTO;
import com.revature.revworkforce.model.Announcement;
import com.revature.revworkforce.model.Employee;
import com.revature.revworkforce.security.SecurityUtils;
import com.revature.revworkforce.service.AnnouncementService;
import com.revature.revworkforce.service.EmployeeService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AnnouncementControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AnnouncementService announcementService;

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private AnnouncementController controller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    // =========================================
    // ADMIN LIST
    // =========================================

    @Test
    void listAdminAnnouncements_ShouldReturnPage() throws Exception {

        Employee emp = new Employee();
        emp.setFirstName("Admin User");

        try (MockedStatic<SecurityUtils> security = mockStatic(SecurityUtils.class)) {

            security.when(SecurityUtils::getCurrentUsername).thenReturn("EMP001");

            when(employeeService.getEmployeeById("EMP001")).thenReturn(emp);
            when(announcementService.getAllAnnouncements()).thenReturn(List.of(new Announcement()));

            mockMvc.perform(get("/admin/announcements"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("pages/admin/announcements-management"))
                    .andExpect(model().attributeExists("announcements"));
        }
    }

    // =========================================
    // SHOW CREATE FORM
    // =========================================

    @Test
    void showCreateAnnouncementForm_ShouldReturnForm() throws Exception {

        Employee emp = new Employee();
        emp.setFirstName("Admin User");

        try (MockedStatic<SecurityUtils> security = mockStatic(SecurityUtils.class)) {

            security.when(SecurityUtils::getCurrentUsername).thenReturn("EMP001");

            when(employeeService.getEmployeeById("EMP001")).thenReturn(emp);

            mockMvc.perform(get("/admin/announcements/create"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("pages/admin/announcement-form"))
                    .andExpect(model().attributeExists("announcementDTO"));
        }
    }

    // =========================================
    // CREATE ANNOUNCEMENT
    // =========================================

    @Test
    void createAnnouncement_ShouldRedirect() throws Exception {

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken("ADMIN001", null);

        mockMvc.perform(post("/admin/announcements/create")
                .principal(auth)
                .param("title", "Important Notice")
                .param("message", "System maintenance tonight"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/announcements"));

        verify(announcementService).createAnnouncement(any(AnnouncementDTO.class), eq("ADMIN001"));
    }

    // =========================================
    // UPDATE ANNOUNCEMENT
    // =========================================

    @Test
    void updateAnnouncement_ShouldRedirect() throws Exception {

        mockMvc.perform(post("/admin/announcements/edit/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/announcements"));

        verify(announcementService).updateAnnouncement(eq(1L), any(AnnouncementDTO.class));
    }

    // =========================================
    // DELETE ANNOUNCEMENT
    // =========================================

    @Test
    void deleteAnnouncement_ShouldRedirect() throws Exception {

        mockMvc.perform(post("/admin/announcements/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/announcements"));

        verify(announcementService).deleteAnnouncement(1L);
    }

    // =========================================
    // EMPLOYEE VIEW ANNOUNCEMENTS
    // =========================================

    @Test
    void viewActiveAnnouncements_ShouldReturnPage() throws Exception {

        Employee emp = new Employee();
        emp.setFirstName("John Employee");

        try (MockedStatic<SecurityUtils> security = mockStatic(SecurityUtils.class)) {

            security.when(SecurityUtils::getCurrentUsername).thenReturn("EMP002");

            when(employeeService.getEmployeeById("EMP002")).thenReturn(emp);
            when(announcementService.getActiveAnnouncements()).thenReturn(List.of(new AnnouncementDTO()));

            mockMvc.perform(get("/employee/announcements"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("pages/employee/announcements"))
                    .andExpect(model().attributeExists("announcements"));
        }
    }

    // =========================================
    // VIEW ANNOUNCEMENT DETAILS
    // =========================================

    @Test
    void viewAnnouncementDetails_ShouldReturnDetailPage() throws Exception {

        Employee emp = new Employee();
        emp.setFirstName("John Employee");

        AnnouncementDTO dto = new AnnouncementDTO();

        try (MockedStatic<SecurityUtils> security = mockStatic(SecurityUtils.class)) {

            security.when(SecurityUtils::getCurrentUsername).thenReturn("EMP002");

            when(employeeService.getEmployeeById("EMP002")).thenReturn(emp);
            when(announcementService.getAnnouncementById(1L)).thenReturn(dto);

            mockMvc.perform(get("/employee/announcements/1"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("pages/employee/announcement-detail"))
                    .andExpect(model().attributeExists("announcement"));
        }
    }
}

