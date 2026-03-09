package com.revature.revworkforce.controller;

import com.revature.revworkforce.dto.GoalStatistics;
import com.revature.revworkforce.repository.EmployeeRepository;
import com.revature.revworkforce.service.GoalService;
import com.revature.revworkforce.service.PerformanceReviewService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GoalController.class)
class GoalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GoalService goalService;

    @MockBean
    private PerformanceReviewService reviewService;

    @MockBean
    private EmployeeRepository employeeRepository;

    @Test
    @WithMockUser(username = "user", roles = "EMPLOYEE")
    void viewPerformanceDashboard_Success() throws Exception {
        when(goalService.getEmployeeGoals("user")).thenReturn(new ArrayList<>());
        when(goalService.getEmployeeStatistics("user")).thenReturn(new GoalStatistics(0, 0, 0, 0, 0));
        when(reviewService.getEmployeeReviews("user")).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/employee/performance"))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/employee/performance-goals"))
                .andExpect(model().attributeExists("goals", "statistics", "reviews"));
    }

    @Test
    @WithMockUser(username = "user", roles = "EMPLOYEE")
    void createGoal_Success() throws Exception {
        mockMvc.perform(post("/employee/goals/create")
                .param("goalTitle", "New Goal")
                .param("goalDescription", "Description")
                .param("category", "TECHNICAL")
                .param("deadline", LocalDate.now().plusDays(30).toString())
                .param("priority", "HIGH")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/employee/performance"))
                .andExpect(flash().attribute("success", "Goal created successfully!"));

        verify(goalService).createGoal(eq("user"), anyString(), anyString(), anyString(), any(), any());
    }

    @Test
    @WithMockUser(username = "user", roles = "EMPLOYEE")
    void updateProgress_Success() throws Exception {
        mockMvc.perform(post("/employee/goals/progress/1")
                .param("progress", "50")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/employee/performance"))
                .andExpect(flash().attribute("success", "Progress updated successfully!"));

        verify(goalService).updateProgress(eq(1L), eq("user"), eq(50));
    }

    @Test
    @WithMockUser(username = "user", roles = "EMPLOYEE")
    void deleteGoal_Success() throws Exception {
        mockMvc.perform(post("/employee/goals/delete/1")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/employee/performance"))
                .andExpect(flash().attribute("success", "Goal deleted successfully!"));

        verify(goalService).deleteGoal(1L, "user");
    }

    @Test
    @WithMockUser(username = "manager", roles = "MANAGER")
    void viewTeamPerformance_Success() throws Exception {
        when(goalService.getTeamGoals("manager")).thenReturn(new ArrayList<>());
        when(reviewService.getTeamReviews("manager")).thenReturn(new ArrayList<>());
        when(reviewService.getPendingReviewsForManager("manager")).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/manager/performance"))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/manager/performance-review"))
                .andExpect(model().attributeExists("goals", "reviews", "pendingReviews"));
    }

    @Test
    @WithMockUser(username = "manager", roles = "MANAGER")
    void submitComments_Success() throws Exception {
        mockMvc.perform(post("/manager/goals/comment/1")
                .param("managerComments", "Good progress")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/manager/performance"))
                .andExpect(flash().attribute("success", "Comments added successfully!"));

        verify(goalService).addManagerComments(eq(1L), eq("manager"), eq("Good progress"));
    }
}
