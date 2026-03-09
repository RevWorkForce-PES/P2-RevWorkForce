package com.revature.revworkforce.controller;

import com.revature.revworkforce.model.Employee;
import com.revature.revworkforce.repository.EmployeeRepository;
import com.revature.revworkforce.security.SecurityUtils;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserApiController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeRepository employeeRepository;

    // ====================================
    // Authenticated User
    // ====================================

    @Test
    void getUserInfo_AuthenticatedUser_ShouldReturnUserInfo() throws Exception {

        String employeeId = "EMP001";

        Employee emp = new Employee();
        emp.setEmployeeId(employeeId);
        emp.setFirstName("John Doe");   // ✅ correct field

        try (MockedStatic<SecurityUtils> mockedSecurity = mockStatic(SecurityUtils.class)) {

            mockedSecurity.when(SecurityUtils::isAuthenticated).thenReturn(true);
            mockedSecurity.when(SecurityUtils::getCurrentUsername).thenReturn(employeeId);

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(
                            employeeId,
                            null,
                            List.of(new SimpleGrantedAuthority("ROLE_EMPLOYEE"))
                    );

            mockedSecurity.when(SecurityUtils::getCurrentAuthentication).thenReturn(auth);

            when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(emp));

            mockMvc.perform(get("/api/user/info"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.role").value("Employee"));
        }
    }

    // ====================================
    // Employee Not Found
    // ====================================

    @Test
    void getUserInfo_EmployeeNotFound_ShouldReturnSystemAdministrator() throws Exception {

        String employeeId = "EMP999";

        try (MockedStatic<SecurityUtils> mockedSecurity = mockStatic(SecurityUtils.class)) {

            mockedSecurity.when(SecurityUtils::isAuthenticated).thenReturn(true);
            mockedSecurity.when(SecurityUtils::getCurrentUsername).thenReturn(employeeId);

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(
                            employeeId,
                            null,
                            List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
                    );

            mockedSecurity.when(SecurityUtils::getCurrentAuthentication).thenReturn(auth);

            when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

            mockMvc.perform(get("/api/user/info"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.fullName").value("System Administrator"))
                    .andExpect(jsonPath("$.role").value("Admin"));
        }
    }

    // ====================================
    // Guest User
    // ====================================

    @Test
    void getUserInfo_NotAuthenticated_ShouldReturnGuest() throws Exception {

        try (MockedStatic<SecurityUtils> mockedSecurity = mockStatic(SecurityUtils.class)) {

            mockedSecurity.when(SecurityUtils::isAuthenticated).thenReturn(false);

            mockMvc.perform(get("/api/user/info"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.fullName").value("Guest"))
                    .andExpect(jsonPath("$.role").value("None"));
        }
    }
}