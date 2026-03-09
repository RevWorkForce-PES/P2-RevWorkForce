package com.revature.revworkforce.controller;

import com.revature.revworkforce.repository.LeaveApplicationRepository;
import com.revature.revworkforce.repository.EmployeeRepository;
import com.revature.revworkforce.service.AuditService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuditController.class)
class AuditControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuditService auditService;

    @MockBean
    private LeaveApplicationRepository leaveApplicationRepository;

    @MockBean
    private EmployeeRepository employeeRepository;

    @Test
    @WithMockUser(roles = "ADMIN")
    void auditReports_Success() throws Exception {
        when(auditService.searchAuditLogs(any(), any(), any(), any())).thenReturn(new ArrayList<>());
        when(leaveApplicationRepository.findAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/admin/audit-reports")
                .param("module", "EMPLOYEES")
                .param("startDate", "2024-01-01")
                .param("endDate", "2024-12-31"))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/admin/audit-reports"))
                .andExpect(model().attributeExists("auditLogs", "leaveReports"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void exportAuditLogs_Success() throws Exception {
        byte[] csvContent = "CSV content".getBytes();
        when(auditService.searchAuditLogs(any(), any(), any(), any())).thenReturn(new ArrayList<>());
        when(auditService.exportAuditLogsToCSV(anyList())).thenReturn(csvContent);

        mockMvc.perform(get("/admin/audit/export"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION,
                        org.hamcrest.Matchers.containsString("attachment; filename=audit_log_")))
                .andExpect(content().contentType("text/csv"))
                .andExpect(content().bytes(csvContent));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void loginActivities_Success() throws Exception {
        when(auditService.getLoginActivities()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/admin/audit-logins"))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/admin/login-activities"))
                .andExpect(model().attributeExists("auditLogs"));
    }
}
