package com.revature.revworkforce.controller;

import com.revature.revworkforce.model.Employee;
import com.revature.revworkforce.repository.EmployeeRepository;
import com.revature.revworkforce.security.SecurityUtils;
import com.revature.revworkforce.service.LeaveService;
import com.revature.revworkforce.service.NotificationService;

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
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ManagerDashboardController.class)
class ManagerDashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeRepository employeeRepository;

    @MockBean
    private LeaveService leaveService;

    @MockBean
    private NotificationService notificationService;

    // ==============================
    // Manager Dashboard Test
    // ==============================

    @Test
    @WithMockUser(roles = "MANAGER")
    void managerDashboard_ShouldReturnDashboardView() throws Exception {

        String managerId = "EMP001";

        Employee manager = new Employee();
        manager.setEmployeeId(managerId);
        manager.setFirstName("Manager User");

        Employee emp1 = new Employee();
        emp1.setEmployeeId("EMP002");
        emp1.setFirstName("Employee One");

        try (MockedStatic<SecurityUtils> mockedSecurity = mockStatic(SecurityUtils.class)) {

            mockedSecurity.when(SecurityUtils::getCurrentUsername).thenReturn(managerId);

            when(employeeRepository.findById(managerId))
                    .thenReturn(Optional.of(manager));

            when(employeeRepository.findByManager_EmployeeId(managerId))
                    .thenReturn(List.of(emp1));

            when(leaveService.getPendingLeavesForManager(managerId))
                    .thenReturn(List.of());

            when(notificationService.getUnreadCount(managerId))
                    .thenReturn((long) 2);

            mockMvc.perform(get("/manager/dashboard"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("pages/manager/dashboard"))
                    .andExpect(model().attributeExists("teamSize"))
                    .andExpect(model().attributeExists("pendingCount"))
                    .andExpect(model().attributeExists("unreadCount"));
        }
    }

    // ==============================
    // Team Management Page
    // ==============================

    @Test
    @WithMockUser(roles = "MANAGER")
    void teamManagement_ShouldReturnTeamMembers() throws Exception {

        String managerId = "EMP001";

        Employee emp1 = new Employee();
        emp1.setEmployeeId("EMP002");
        emp1.setFirstName("Alice");
        emp1.setEmail("alice@test.com");

        try (MockedStatic<SecurityUtils> mockedSecurity = mockStatic(SecurityUtils.class)) {

            mockedSecurity.when(SecurityUtils::getCurrentUsername).thenReturn(managerId);

            when(employeeRepository.findByManager_EmployeeId(managerId))
                    .thenReturn(List.of(emp1));

            mockMvc.perform(get("/manager/team-management"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("pages/manager/team-management"))
                    .andExpect(model().attributeExists("teamMembers"));
        }
    }

    // ==============================
    // Team Management Search
    // ==============================

    @Test
    @WithMockUser(roles = "MANAGER")
    void teamManagement_WithSearch_ShouldFilterResults() throws Exception {

        String managerId = "EMP001";

        Employee emp1 = new Employee();
        emp1.setEmployeeId("EMP002");
        emp1.setFirstName("Alice");
        emp1.setEmail("alice@test.com");

        Employee emp2 = new Employee();
        emp2.setEmployeeId("EMP003");
        emp2.setFirstName("Bob");
        emp2.setEmail("bob@test.com");

        try (MockedStatic<SecurityUtils> mockedSecurity = mockStatic(SecurityUtils.class)) {

            mockedSecurity.when(SecurityUtils::getCurrentUsername).thenReturn(managerId);

            when(employeeRepository.findByManager_EmployeeId(managerId))
                    .thenReturn(List.of(emp1, emp2));

            mockMvc.perform(get("/manager/team-management")
                    .param("search", "alice"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("pages/manager/team-management"))
                    .andExpect(model().attributeExists("teamMembers"));
        }
    }
}