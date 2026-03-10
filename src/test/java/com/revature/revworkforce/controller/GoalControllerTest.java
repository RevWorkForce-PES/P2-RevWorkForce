package com.revature.revworkforce.controller;

import com.revature.revworkforce.dto.GoalDTO;
import com.revature.revworkforce.dto.GoalStatistics;
import com.revature.revworkforce.dto.PerformanceReviewDTO;
import com.revature.revworkforce.security.SecurityUtils;
import com.revature.revworkforce.service.EmployeeService;
import com.revature.revworkforce.service.GoalService;
import com.revature.revworkforce.service.PerformanceReviewService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class GoalControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GoalService goalService;

    @Mock
    private PerformanceReviewService reviewService;

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private GoalController goalController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(goalController)
                .build();
    }

    // ===============================
    // Employee Dashboard
    // ===============================

    @Test
    void viewPerformanceDashboard_ShouldReturnDashboard() throws Exception {

        GoalStatistics stats = mock(GoalStatistics.class);

        try (MockedStatic<SecurityUtils> security = mockStatic(SecurityUtils.class)) {

            security.when(SecurityUtils::getCurrentUsername).thenReturn("EMP001");
            security.when(() -> SecurityUtils.hasRole(anyString())).thenReturn(false);

            when(goalService.getEmployeeGoals("EMP001")).thenReturn(List.of(new GoalDTO()));
            when(goalService.getEmployeeStatistics("EMP001")).thenReturn(stats);
            when(reviewService.getEmployeeReviews("EMP001")).thenReturn(List.of(new PerformanceReviewDTO()));
            when(reviewService.getAverageRating("EMP001")).thenReturn(BigDecimal.valueOf(4.0));

            mockMvc.perform(get("/employee/performance"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("pages/employee/performance-goals"))
                    .andExpect(model().attributeExists("goals"))
                    .andExpect(model().attributeExists("statistics"))
                    .andExpect(model().attributeExists("reviews"));
        }
    }

    // ===============================
    // Active Goals
    // ===============================

    @Test
    void viewActiveGoals_ShouldReturnActiveGoalsPage() throws Exception {

        try (MockedStatic<SecurityUtils> security = mockStatic(SecurityUtils.class)) {

            security.when(SecurityUtils::getCurrentUsername).thenReturn("EMP001");

            when(goalService.getActiveGoals("EMP001")).thenReturn(List.of(new GoalDTO()));

            mockMvc.perform(get("/employee/goals/active"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("pages/employee/goals-active"))
                    .andExpect(model().attributeExists("goals"));
        }
    }

    // ===============================
    // Create Goal Form
    // ===============================

    @Test
    void showCreateForm_ShouldReturnCreatePage() throws Exception {

        try (MockedStatic<SecurityUtils> security = mockStatic(SecurityUtils.class)) {

            security.when(SecurityUtils::getCurrentUsername).thenReturn("EMP001");

            mockMvc.perform(get("/employee/goals/create"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("pages/employee/goal-create"))
                    .andExpect(model().attributeExists("goalDTO"));
        }
    }

    // ===============================
    // Create Goal
    // ===============================

    @Test
    void createGoal_ShouldRedirectToDashboard() throws Exception {

        try (MockedStatic<SecurityUtils> security = mockStatic(SecurityUtils.class)) {

            security.when(SecurityUtils::getCurrentUsername).thenReturn("EMP001");

            mockMvc.perform(post("/employee/goals/create")
                    .param("goalTitle", "Learn Spring Boot")
                    .param("goalDescription", "Complete Boot course")
                    .param("category", "Technical")
                    .param("deadline", "2026-12-31")
                    .param("priority", "HIGH"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/employee/performance"));

            verify(goalService).createGoal(
                    eq("EMP001"),
                    anyString(),
                    anyString(),
                    anyString(),
                    any(),
                    any());
        }
    }

    // ===============================
    // Cancel Goal
    // ===============================

    @Test
    void cancelGoal_ShouldRedirect() throws Exception {

        try (MockedStatic<SecurityUtils> security = mockStatic(SecurityUtils.class)) {

            security.when(SecurityUtils::getCurrentUsername).thenReturn("EMP001");

            mockMvc.perform(post("/employee/goals/cancel/1"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/employee/performance"));

            verify(goalService).cancelGoal(1L, "EMP001");
        }
    }

    // ===============================
    // Upcoming Goals
    // ===============================

    @Test
    void viewUpcomingDeadlines_ShouldReturnUpcomingPage() throws Exception {

        try (MockedStatic<SecurityUtils> security = mockStatic(SecurityUtils.class)) {

            security.when(SecurityUtils::getCurrentUsername).thenReturn("EMP001");

            when(goalService.getUpcomingDeadlines("EMP001", 30))
                    .thenReturn(List.of(new GoalDTO()));

            mockMvc.perform(get("/employee/goals/upcoming"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("pages/employee/goals-upcoming"))
                    .andExpect(model().attributeExists("goals"));
        }
    }

    // ===============================
    // Overdue Goals
    // ===============================

    @Test
    void viewOverdueGoals_ShouldReturnOverduePage() throws Exception {

        try (MockedStatic<SecurityUtils> security = mockStatic(SecurityUtils.class)) {

            security.when(SecurityUtils::getCurrentUsername).thenReturn("EMP001");

            when(goalService.getOverdueGoals("EMP001"))
                    .thenReturn(List.of(new GoalDTO()));

            mockMvc.perform(get("/employee/goals/overdue"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("pages/employee/goals-overdue"))
                    .andExpect(model().attributeExists("goals"));
        }
    }

    // ===============================
    // Manager Performance
    // ===============================

    @Test
    void viewTeamPerformance_ShouldReturnManagerPage() throws Exception {

        try (MockedStatic<SecurityUtils> security = mockStatic(SecurityUtils.class)) {

            security.when(SecurityUtils::getCurrentUsername).thenReturn("MGR001");

            when(goalService.getTeamGoals("MGR001")).thenReturn(List.of(new GoalDTO()));
            when(reviewService.getTeamReviews("MGR001")).thenReturn(List.of(new PerformanceReviewDTO()));
            when(reviewService.getPendingReviewsForManager("MGR001")).thenReturn(List.of(new PerformanceReviewDTO()));

            mockMvc.perform(get("/manager/performance"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("pages/manager/performance-review"))
                    .andExpect(model().attributeExists("goals"))
                    .andExpect(model().attributeExists("reviews"));
        }
    }

}
