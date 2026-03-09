package com.revature.revworkforce.controller;

import com.revature.revworkforce.repository.EmployeeRepository;
import com.revature.revworkforce.repository.LeaveTypeRepository;
import com.revature.revworkforce.service.EmployeeService;
import com.revature.revworkforce.service.HolidayService;
import com.revature.revworkforce.service.LeaveService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LeaveController.class)
class LeaveControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LeaveService leaveService;

    @MockBean
    private LeaveTypeRepository leaveTypeRepository;

    @MockBean
    private HolidayService holidayService;

    @MockBean
    private EmployeeRepository employeeRepository;

    @MockBean
    private EmployeeService employeeService;

    // =========================================================
    // EMPLOYEE APPLY PAGE
    // =========================================================
    @Test
    @WithMockUser(username = "EMP001", roles = { "EMPLOYEE" })
    void showApplyPage_ReturnsLeaveManagementPage() throws Exception {

        when(leaveTypeRepository.findAll()).thenReturn(java.util.List.of());

        mockMvc.perform(get("/employee/apply"))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/employee/leave-management"));
    }

    // =========================================================
    // APPLY LEAVE
    // =========================================================
    @Test
    @WithMockUser(username = "EMP001", roles = { "EMPLOYEE" })
    void applyLeave_RedirectsToLeaveManagement() throws Exception {

        mockMvc.perform(post("/employee/apply")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/employee/leave-management"));

        verify(leaveService, times(1)).applyLeave(any(), anyString());
    }

    // =========================================================
    // CANCEL LEAVE
    // =========================================================
    @Test
    @WithMockUser(username = "EMP001", roles = { "EMPLOYEE" })
    void cancelLeave_RedirectsToLeaveManagement() throws Exception {

        mockMvc.perform(post("/employee/cancel/1")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/employee/leave-management"));

        verify(leaveService, times(1)).cancelLeave(1L, anyString());
    }

    // =========================================================
    // MANAGER PAGE
    // =========================================================
    @Test
    @WithMockUser(username = "MGR001", roles = { "MANAGER" })
    void showPendingLeaves_ReturnsManagerPage() throws Exception {

        when(leaveService.getPendingLeavesForManager("MGR001")).thenReturn(java.util.List.of());
        when(leaveService.getTeamLeaves("MGR001")).thenReturn(java.util.List.of());
        when(holidayService.getAllActiveHolidays()).thenReturn(java.util.List.of());

        mockMvc.perform(get("/manager/pending"))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/manager/leave-approvals"));
    }

    // =========================================================
    // APPROVE LEAVE
    // =========================================================
    @Test
    @WithMockUser(username = "MGR001", roles = { "MANAGER" })
    void approveLeave_RedirectsToManagerPage() throws Exception {

        mockMvc.perform(post("/manager/approve/1")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/manager/leave-approvals"));

        verify(leaveService, times(1)).approveLeave(1L, "MGR001", null);
    }

    // =========================================================
    // REJECT LEAVE
    // =========================================================
    @Test
    @WithMockUser(username = "MGR001", roles = { "MANAGER" })
    void rejectLeave_RedirectsToManagerPage() throws Exception {

        mockMvc.perform(post("/manager/reject/1")
                .param("rejectionReason", "Invalid")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/manager/leave-approvals"));

        verify(leaveService, times(1)).rejectLeave(1L, "MGR001", "Invalid");
    }

    // =========================================================
    // ADMIN REVOKE LEAVE - MOVED TO AdminActionController
    // =========================================================
}