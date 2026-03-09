package com.revature.revworkforce.controller;

import com.revature.revworkforce.dto.AnnouncementDTO;
import com.revature.revworkforce.repository.EmployeeRepository;
import com.revature.revworkforce.service.AnnouncementService;
import com.revature.revworkforce.service.AuditService;
import com.revature.revworkforce.service.LeaveService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminActionController.class)
class AdminActionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuditService auditService;

    @MockBean
    private AnnouncementService announcementService;

    @MockBean
    private LeaveService leaveService;

    @MockBean
    private EmployeeRepository employeeRepository;

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    void triggerJob_AuditCleanup_Success() throws Exception {
        mockMvc.perform(post("/admin/jobs/trigger")
                .param("jobName", "Audit Cleanup")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/audit-reports"))
                .andExpect(flash().attribute("successMessage", "Audit Cleanup job triggered successfully."));

        verify(auditService).cleanOldAuditLogs();
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    void triggerJob_UnknownJob() throws Exception {
        mockMvc.perform(post("/admin/jobs/trigger")
                .param("jobName", "Unknown")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/audit-reports"))
                .andExpect(flash().attribute("errorMessage", "Unknown job name: Unknown"));
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    void broadcastAnnouncement_Success() throws Exception {
        mockMvc.perform(post("/admin/notifications/broadcast")
                .param("subject", "Subject")
                .param("message", "Message")
                .param("audience", "All")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/audit-reports"))
                .andExpect(flash().attribute("successMessage", "Announcement broadcasted to All."));

        verify(announcementService).createAnnouncement(any(AnnouncementDTO.class), eq("admin"));
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    void revokeLeave_Success() throws Exception {
        mockMvc.perform(post("/admin/leave/revoke/1")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/audit-reports"))
                .andExpect(flash().attribute("successMessage", "Leave application #1 has been revoked."));

        verify(leaveService).revokeLeave(eq(1L), eq("admin"));
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    void revokeLeave_Exception() throws Exception {
        doThrow(new RuntimeException("Error")).when(leaveService).revokeLeave(anyLong(), anyString());

        mockMvc.perform(post("/admin/leave/revoke/1")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/audit-reports"))
                .andExpect(flash().attribute("errorMessage", "Failed to revoke leave: Error"));
    }
}
