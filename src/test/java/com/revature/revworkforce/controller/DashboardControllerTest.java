package com.revature.revworkforce.controller;

import com.revature.revworkforce.repository.EmployeeRepository;
import com.revature.revworkforce.security.SecurityUtils;
import com.revature.revworkforce.service.AuthService;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DashboardController.class)
@AutoConfigureMockMvc(addFilters = false)
class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeRepository employeeRepository;

    @MockBean
    private AuthService authService;

    // ===============================
    // First Login Redirect
    // ===============================

    @Test
    void dashboard_FirstLogin_ShouldRedirectChangePassword() throws Exception {

        String employeeId = "EMP001";

        try (MockedStatic<SecurityUtils> mockedSecurity = mockStatic(SecurityUtils.class)) {

            mockedSecurity.when(SecurityUtils::getCurrentUsername).thenReturn(employeeId);
            mockedSecurity.when(() -> SecurityUtils.hasRole(anyString())).thenReturn(false);

            when(authService.isFirstLogin(employeeId)).thenReturn(true);

            mockMvc.perform(get("/dashboard"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/change-password"));
        }
    }

    // ===============================
    // Admin Redirect
    // ===============================

    @Test
    void dashboard_AdminRole_ShouldRedirectAdminDashboard() throws Exception {

        String employeeId = "EMP001";

        try (MockedStatic<SecurityUtils> mockedSecurity = mockStatic(SecurityUtils.class)) {

            mockedSecurity.when(SecurityUtils::getCurrentUsername).thenReturn(employeeId);
            mockedSecurity.when(() -> SecurityUtils.hasRole("ADMIN")).thenReturn(true);

            when(authService.isFirstLogin(employeeId)).thenReturn(false);

            mockMvc.perform(get("/dashboard"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/admin/dashboard"));
        }
    }

    // ===============================
    // Manager Redirect
    // ===============================

    @Test
    void dashboard_ManagerRole_ShouldRedirectManagerDashboard() throws Exception {

        String employeeId = "EMP002";

        try (MockedStatic<SecurityUtils> mockedSecurity = mockStatic(SecurityUtils.class)) {

            mockedSecurity.when(SecurityUtils::getCurrentUsername).thenReturn(employeeId);

            mockedSecurity.when(() -> SecurityUtils.hasRole("ADMIN")).thenReturn(false);
            mockedSecurity.when(() -> SecurityUtils.hasRole("MANAGER")).thenReturn(true);

            when(authService.isFirstLogin(employeeId)).thenReturn(false);

            mockMvc.perform(get("/dashboard"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/manager/dashboard"));
        }
    }

    // ===============================
    // Employee Redirect
    // ===============================

    @Test
    void dashboard_EmployeeRole_ShouldRedirectEmployeeDashboard() throws Exception {

        String employeeId = "EMP003";

        try (MockedStatic<SecurityUtils> mockedSecurity = mockStatic(SecurityUtils.class)) {

            mockedSecurity.when(SecurityUtils::getCurrentUsername).thenReturn(employeeId);

            mockedSecurity.when(() -> SecurityUtils.hasRole("ADMIN")).thenReturn(false);
            mockedSecurity.when(() -> SecurityUtils.hasRole("MANAGER")).thenReturn(false);

            when(authService.isFirstLogin(employeeId)).thenReturn(false);

            mockMvc.perform(get("/dashboard"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/employee/dashboard"));
        }
    }
}