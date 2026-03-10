package com.revature.revworkforce.controller;

import com.revature.revworkforce.dto.LeaveBalanceDTO;
import com.revature.revworkforce.model.Employee;
import com.revature.revworkforce.repository.EmployeeRepository;
import com.revature.revworkforce.service.*;

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

import com.revature.revworkforce.security.SecurityUtils;

@WebMvcTest(EmployeeDashboardController.class)
class EmployeeDashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LeaveService leaveService;

    @MockBean
    private EmployeeRepository employeeRepository;

    @MockBean
    private HolidayService holidayService;

    @MockBean
    private NotificationService notificationService;

    @MockBean
    private GoalService goalService;

    @MockBean
    private PerformanceReviewService reviewService;

    @MockBean
    private AnnouncementService announcementService;

    @MockBean
    private EmployeeService employeeService;

    // ==========================
    // Dashboard Test
    // ==========================

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void employeeDashboard_ShouldReturnDashboardView() throws Exception {

        String employeeId = "EMP001";

        Employee employee = new Employee();
        employee.setEmployeeId(employeeId);
        employee.setFirstName("Test User");

        LeaveBalanceDTO balance = new LeaveBalanceDTO();
        balance.setRemainingBalance(10);

        try (MockedStatic<SecurityUtils> mockedSecurity = mockStatic(SecurityUtils.class)) {

            mockedSecurity.when(SecurityUtils::getCurrentUsername).thenReturn(employeeId);

            when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
            when(leaveService.getLeaveBalances(any(), anyInt())).thenReturn(List.of(balance));
            when(notificationService.getUnreadCount(employeeId)).thenReturn((long) 3);
            when(holidayService.getUpcomingHolidays()).thenReturn(List.of());
            when(leaveService.getEmployeeLeaveHistory(employeeId)).thenReturn(List.of());
            when(goalService.getActiveGoals(employeeId)).thenReturn(List.of());
            when(goalService.getEmployeeGoals(employeeId)).thenReturn(List.of());
            when(reviewService.getEmployeeReviews(employeeId)).thenReturn(List.of());
            when(announcementService.getActiveAnnouncements()).thenReturn(List.of());

            mockMvc.perform(get("/employee/dashboard"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("pages/employee/dashboard"))
                    .andExpect(model().attributeExists("pageTitle"))
                    .andExpect(model().attributeExists("leaveBalance"));
        }
    }

}
   