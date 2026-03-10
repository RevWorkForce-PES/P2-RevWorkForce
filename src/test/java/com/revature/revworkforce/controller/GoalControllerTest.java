package com.revature.revworkforce.controller;

import com.revature.revworkforce.dto.GoalDTO;
import com.revature.revworkforce.dto.GoalStatistics;
import com.revature.revworkforce.dto.PerformanceReviewDTO;
import com.revature.revworkforce.enums.GoalStatus;
import com.revature.revworkforce.model.Employee;
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
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.containsString;

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
    // EMPLOYEE ENDPOINTS
    // ===============================

    @Test
    void viewPerformanceDashboard_Success() throws Exception {
        try (MockedStatic<SecurityUtils> security = mockStatic(SecurityUtils.class)) {
            security.when(SecurityUtils::getCurrentUsername).thenReturn("EMP001");
            security.when(() -> SecurityUtils.hasRole(anyString())).thenReturn(false);

            when(goalService.getEmployeeGoals("EMP001")).thenReturn(Collections.emptyList());
            when(goalService.getEmployeeStatistics("EMP001")).thenReturn(new GoalStatistics(0, 0, 0, 0, 0));

            Employee emp = new Employee();
            emp.setFirstName("John");
            emp.setLastName("Doe");
            when(employeeService.getEmployeeById("EMP001")).thenReturn(emp);

            mockMvc.perform(get("/employee/performance"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("pages/employee/performance-goals"))
                    .andExpect(model().attribute("fullName", "John Doe"));
        }
    }

    @Test
    void viewPerformanceDashboard_SidebarException() throws Exception {
        try (MockedStatic<SecurityUtils> security = mockStatic(SecurityUtils.class)) {
            security.when(SecurityUtils::getCurrentUsername).thenReturn("EMP001");
            when(employeeService.getEmployeeById("EMP001")).thenThrow(new RuntimeException("Error"));

            mockMvc.perform(get("/employee/performance"))
                    .andExpect(status().isOk())
                    .andExpect(model().attribute("fullName", "User"));
        }
    }

    @Test
    void viewActiveGoals_Success() throws Exception {
        try (MockedStatic<SecurityUtils> security = mockStatic(SecurityUtils.class)) {
            security.when(SecurityUtils::getCurrentUsername).thenReturn("EMP001");
            when(goalService.getActiveGoals("EMP001")).thenReturn(Collections.emptyList());

            mockMvc.perform(get("/employee/goals/active"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("pages/employee/goals-active"));
        }
    }

    @Test
    void showCreateForm_Success() throws Exception {
        try (MockedStatic<SecurityUtils> security = mockStatic(SecurityUtils.class)) {
            security.when(SecurityUtils::getCurrentUsername).thenReturn("EMP001");

            mockMvc.perform(get("/employee/goals/create"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("pages/employee/goal-create"))
                    .andExpect(model().attributeExists("goalDTO"));
        }
    }

    @Test
    void createGoal_ValidationError() throws Exception {
        mockMvc.perform(post("/employee/goals/create")
        // No params to trigger validation errors
        )
                .andExpect(status().isOk())
                .andExpect(view().name("pages/employee/goal-create"));
    }

    @Test
    void createGoal_Success() throws Exception {
        try (MockedStatic<SecurityUtils> security = mockStatic(SecurityUtils.class)) {
            security.when(SecurityUtils::getCurrentUsername).thenReturn("EMP001");

            mockMvc.perform(post("/employee/goals/create")
                    .param("goalTitle", "Valid Title")
                    .param("goalDescription", "This is a valid goal description with enough length.")
                    .param("category", "Technical")
                    .param("deadline", "2027-12-31")
                    .param("priority", "HIGH"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/employee/performance"));

            verify(goalService).createGoal(eq("EMP001"), anyString(), anyString(), anyString(), any(), any());
        }
    }

    @Test
    void createGoal_ServiceException() throws Exception {
        try (MockedStatic<SecurityUtils> security = mockStatic(SecurityUtils.class)) {
            security.when(SecurityUtils::getCurrentUsername).thenReturn("EMP001");
            doThrow(new RuntimeException("fail")).when(goalService).createGoal(any(), any(), any(), any(), any(),
                    any());

            mockMvc.perform(post("/employee/goals/create")
                    .param("goalTitle", "Valid Title")
                    .param("goalDescription", "This is a valid goal description.")
                    .param("category", "Technical")
                    .param("deadline", "2027-12-31")
                    .param("priority", "HIGH"))
                    .andExpect(status().isOk())
                    .andExpect(model().attribute("error", "fail"));
        }
    }

    @Test
    void showEditForm_Success() throws Exception {
        GoalDTO dto = new GoalDTO();
        dto.setEmployeeId("EMP001");
        dto.setStatus(GoalStatus.IN_PROGRESS); // Derived isEditable() will be true

        try (MockedStatic<SecurityUtils> security = mockStatic(SecurityUtils.class)) {
            security.when(SecurityUtils::getCurrentUsername).thenReturn("EMP001");
            when(goalService.getGoalDTOById(1L)).thenReturn(dto);

            mockMvc.perform(get("/employee/goals/edit/1"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("pages/employee/goal-edit"));
        }
    }

    @Test
    void showEditForm_NotOwner() throws Exception {
        GoalDTO dto = new GoalDTO();
        dto.setEmployeeId("EMP002");

        try (MockedStatic<SecurityUtils> security = mockStatic(SecurityUtils.class)) {
            security.when(SecurityUtils::getCurrentUsername).thenReturn("EMP001");
            when(goalService.getGoalDTOById(1L)).thenReturn(dto);

            mockMvc.perform(get("/employee/goals/edit/1"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(flash().attribute("error", "You can only edit your own goals"));
        }
    }

    @Test
    void showEditForm_NotEditable() throws Exception {
        GoalDTO dto = new GoalDTO();
        dto.setEmployeeId("EMP001");
        dto.setStatus(GoalStatus.COMPLETED); // Derived isEditable() will be false

        try (MockedStatic<SecurityUtils> security = mockStatic(SecurityUtils.class)) {
            security.when(SecurityUtils::getCurrentUsername).thenReturn("EMP001");
            when(goalService.getGoalDTOById(1L)).thenReturn(dto);

            mockMvc.perform(get("/employee/goals/edit/1"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(flash().attribute("error", containsString("Cannot edit completed")));
        }
    }

    @Test
    void updateGoal_ValidationError() throws Exception {
        mockMvc.perform(post("/employee/goals/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/employee/goal-edit"));
    }

    @Test
    void updateGoal_Success() throws Exception {
        try (MockedStatic<SecurityUtils> security = mockStatic(SecurityUtils.class)) {
            security.when(SecurityUtils::getCurrentUsername).thenReturn("EMP001");

            mockMvc.perform(post("/employee/goals/edit/1")
                    .param("goalTitle", "New Title")
                    .param("goalDescription", "New valid description for the goal.")
                    .param("category", "Technical")
                    .param("deadline", "2027-12-31")
                    .param("priority", "MEDIUM"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/employee/performance"));

            verify(goalService).updateGoal(eq(1L), eq("EMP001"), anyString(), anyString(), anyString(), any(), any());
        }
    }

    @Test
    void showProgressForm_Success() throws Exception {
        GoalDTO dto = new GoalDTO();
        dto.setEmployeeId("EMP001");

        try (MockedStatic<SecurityUtils> security = mockStatic(SecurityUtils.class)) {
            security.when(SecurityUtils::getCurrentUsername).thenReturn("EMP001");
            when(goalService.getGoalDTOById(1L)).thenReturn(dto);

            mockMvc.perform(get("/employee/goals/progress/1"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("pages/employee/goal-progress"));
        }
    }

    @Test
    void updateProgress_Success() throws Exception {
        try (MockedStatic<SecurityUtils> security = mockStatic(SecurityUtils.class)) {
            security.when(SecurityUtils::getCurrentUsername).thenReturn("EMP001");

            mockMvc.perform(post("/employee/goals/progress/1")
                    .param("progress", "50"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/employee/performance"));

            verify(goalService).updateProgress(eq(1L), eq("EMP001"), eq(50));
        }
    }

    @Test
    void cancelGoal_Success() throws Exception {
        try (MockedStatic<SecurityUtils> security = mockStatic(SecurityUtils.class)) {
            security.when(SecurityUtils::getCurrentUsername).thenReturn("EMP001");

            mockMvc.perform(post("/employee/goals/cancel/1"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/employee/performance"));

            verify(goalService).cancelGoal(1L, "EMP001");
        }
    }

    @Test
    void viewGoalDetails_Success() throws Exception {
        when(goalService.getGoalDTOById(1L)).thenReturn(new GoalDTO());

        mockMvc.perform(get("/employee/goals/view/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/employee/goal-view"));
    }

    @Test
    void deleteGoal_Success() throws Exception {
        try (MockedStatic<SecurityUtils> security = mockStatic(SecurityUtils.class)) {
            security.when(SecurityUtils::getCurrentUsername).thenReturn("EMP001");

            mockMvc.perform(post("/employee/goals/delete/1"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/employee/performance"));

            verify(goalService).deleteGoal(1L, "EMP001");
        }
    }

    @Test
    void viewUpcomingDeadlines_Success() throws Exception {
        try (MockedStatic<SecurityUtils> security = mockStatic(SecurityUtils.class)) {
            security.when(SecurityUtils::getCurrentUsername).thenReturn("EMP001");
            when(goalService.getUpcomingDeadlines("EMP001", 30)).thenReturn(Collections.emptyList());

            mockMvc.perform(get("/employee/goals/upcoming"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("pages/employee/goals-upcoming"));
        }
    }

    @Test
    void viewOverdueGoals_Success() throws Exception {
        try (MockedStatic<SecurityUtils> security = mockStatic(SecurityUtils.class)) {
            security.when(SecurityUtils::getCurrentUsername).thenReturn("EMP001");
            when(goalService.getOverdueGoals("EMP001")).thenReturn(Collections.emptyList());

            mockMvc.perform(get("/employee/goals/overdue"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("pages/employee/goals-overdue"));
        }
    }

    // ===============================
    // MANAGER ENDPOINTS
    // ===============================

    @Test
    void viewTeamPerformance_Success() throws Exception {
        try (MockedStatic<SecurityUtils> security = mockStatic(SecurityUtils.class)) {
            security.when(SecurityUtils::getCurrentUsername).thenReturn("MGR001");

            mockMvc.perform(get("/manager/performance"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("pages/manager/performance-review"));
        }
    }

    @Test
    void viewTeamActiveGoals_Success() throws Exception {
        try (MockedStatic<SecurityUtils> security = mockStatic(SecurityUtils.class)) {
            security.when(SecurityUtils::getCurrentUsername).thenReturn("MGR001");
            when(goalService.getTeamActiveGoals("MGR001")).thenReturn(Collections.emptyList());

            mockMvc.perform(get("/manager/goals/active"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("pages/manager/goals-active"));
        }
    }

    @Test
    void showCommentForm_Success() throws Exception {
        when(goalService.getGoalDTOById(1L)).thenReturn(new GoalDTO());

        mockMvc.perform(get("/manager/goals/comment/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/manager/goal-comment"));
    }

    @Test
    void submitComments_Success() throws Exception {
        try (MockedStatic<SecurityUtils> security = mockStatic(SecurityUtils.class)) {
            security.when(SecurityUtils::getCurrentUsername).thenReturn("MGR001");

            mockMvc.perform(post("/manager/goals/comment/1")
                    .param("managerComments", "Good progress."))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/manager/performance"));

            verify(goalService).addManagerComments(eq(1L), eq("MGR001"), eq("Good progress."));
        }
    }

    @Test
    void viewTeamGoalDetails_Success() throws Exception {
        when(goalService.getGoalDTOById(1L)).thenReturn(new GoalDTO());

        mockMvc.perform(get("/manager/goals/view/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/manager/goal-view"));
    }
}
