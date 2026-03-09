package com.revature.revworkforce.controller;

import com.revature.revworkforce.model.Employee;
import com.revature.revworkforce.model.LeaveBalance;
import com.revature.revworkforce.model.LeaveType;
import com.revature.revworkforce.repository.DepartmentRepository;
import com.revature.revworkforce.repository.EmployeeRepository;
import com.revature.revworkforce.repository.LeaveBalanceRepository;
import com.revature.revworkforce.repository.LeaveTypeRepository;
import com.revature.revworkforce.service.AdminService;
import com.revature.revworkforce.service.LeaveService;
import com.revature.revworkforce.service.LeaveTypeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LeaveService leaveService;

    @MockBean
    private DepartmentRepository departmentRepository;

    @MockBean
    private LeaveTypeService leaveTypeService;

    @MockBean
    private AdminService adminService;

    @MockBean
    private EmployeeRepository employeeRepository;

    @MockBean
    private LeaveTypeRepository leaveTypeRepository;

    @MockBean
    private LeaveBalanceRepository leaveBalanceRepository;

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAdminSettings_Success() throws Exception {
        mockMvc.perform(get("/admin/settings"))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/auth/settings"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void resetPassword_Success() throws Exception {
        mockMvc.perform(post("/admin/reset-password/EMP001")
                .param("newPassword", "password123")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Password reset successfully. User must change it on first login."));

        verify(adminService).resetPassword("EMP001", "password123");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void unlockAccount_Success() throws Exception {
        mockMvc.perform(post("/admin/unlock-account/EMP001")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Account unlocked successfully."));

        verify(adminService).unlockAccount("EMP001");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getDatabaseHealth_Success() throws Exception {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        when(adminService.getDatabaseHealth()).thenReturn(health);

        mockMvc.perform(get("/admin/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void bulkActivateEmployees_Success() throws Exception {
        when(adminService.bulkActivateEmployees(any())).thenReturn(2);

        mockMvc.perform(post("/admin/api/bulk-activate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("[\"EMP001\", \"EMP002\"]")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("2 employees activated successfully."));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void assignLeaveQuota_Success() throws Exception {
        Employee employee = new Employee();
        LeaveType leaveType = new LeaveType();
        when(employeeRepository.findById("EMP001")).thenReturn(Optional.of(employee));
        when(leaveTypeRepository.findById(1L)).thenReturn(Optional.of(leaveType));
        when(leaveBalanceRepository.findByEmployeeAndLeaveTypeAndYear(any(), any(), anyInt()))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/admin/leave-quota/assign")
                .param("employeeId", "EMP001")
                .param("leaveTypeId", "1")
                .param("year", "2024")
                .param("days", "20")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/system-config?tab=leave-config"));

        verify(leaveBalanceRepository).save(any(LeaveBalance.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void saveLeaveType_Success() throws Exception {
        mockMvc.perform(post("/admin/leave-types/save")
                .flashAttr("leaveType", new LeaveType())
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/system-config?tab=leave-config"));

        verify(leaveTypeRepository).save(any(LeaveType.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteLeaveType_Success() throws Exception {
        mockMvc.perform(get("/admin/leave-types/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/system-config?tab=leave-config"));

        verify(leaveTypeRepository).deleteById(1L);
    }
}
