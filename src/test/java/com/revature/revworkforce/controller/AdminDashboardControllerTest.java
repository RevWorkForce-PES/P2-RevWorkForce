package com.revature.revworkforce.controller;

import com.revature.revworkforce.enums.EmployeeStatus;
import com.revature.revworkforce.model.Employee;
import com.revature.revworkforce.repository.DepartmentRepository;
import com.revature.revworkforce.repository.EmployeeRepository;
import com.revature.revworkforce.repository.LeaveTypeRepository;
import com.revature.revworkforce.security.SecurityUtils;
import com.revature.revworkforce.service.DepartmentService;
import com.revature.revworkforce.service.DesignationService;
import com.revature.revworkforce.service.HolidayService;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminDashboardController.class)
class AdminDashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeRepository employeeRepository;

    @MockBean
    private DepartmentRepository departmentRepository;

    @MockBean
    private LeaveTypeRepository leaveTypeRepository;

    @MockBean
    private DepartmentService departmentService;

    @MockBean
    private DesignationService designationService;

    @MockBean
    private HolidayService holidayService;

    // ===============================
    // Admin Dashboard
    // ===============================

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminDashboard_ShouldReturnDashboardView() throws Exception {

        String adminId = "ADM001";

        Employee admin = new Employee();
        admin.setEmployeeId(adminId);
        admin.setFirstName("Admin User");

        try (MockedStatic<SecurityUtils> mockedSecurity = mockStatic(SecurityUtils.class)) {

            mockedSecurity.when(SecurityUtils::getCurrentUsername)
                    .thenReturn(adminId);

            when(employeeRepository.findById(adminId))
                    .thenReturn(Optional.of(admin));

            when(employeeRepository.count())
                    .thenReturn(10L);

            when(employeeRepository.countByStatus(EmployeeStatus.ACTIVE))
                    .thenReturn(8L);

            when(departmentRepository.count())
                    .thenReturn(3L);

            mockMvc.perform(get("/admin/dashboard"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("pages/admin/dashboard"))
                    .andExpect(model().attributeExists("totalEmployees"))
                    .andExpect(model().attributeExists("activeEmployees"))
                    .andExpect(model().attributeExists("totalDepartments"))
                    .andExpect(model().attributeExists("pageTitle"));
        }
    }

    // ===============================
    // System Config Page
    // ===============================

    @Test
    @WithMockUser(roles = "ADMIN")
    void systemConfig_ShouldReturnSystemConfigPage() throws Exception {

        when(departmentService.getAllDepartments()).thenReturn(List.of());
        when(designationService.getAllDesignations()).thenReturn(List.of());
        when(holidayService.getAllHolidays()).thenReturn(List.of());
        when(employeeRepository.findAll()).thenReturn(List.of());
        when(leaveTypeRepository.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/admin/system-config"))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/admin/system-config"))
                .andExpect(model().attributeExists("departments"))
                .andExpect(model().attributeExists("designations"))
                .andExpect(model().attributeExists("holidays"))
                .andExpect(model().attributeExists("employees"))
                .andExpect(model().attributeExists("leaveTypes"));
    }
}