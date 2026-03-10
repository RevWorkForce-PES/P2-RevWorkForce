package com.revature.revworkforce.controller;

import com.revature.revworkforce.dto.PerformanceReviewDTO;
import com.revature.revworkforce.enums.ReviewStatus;
import com.revature.revworkforce.model.Employee;
import com.revature.revworkforce.security.SecurityUtils;
import com.revature.revworkforce.service.EmployeeService;
import com.revature.revworkforce.service.PerformanceReviewService;
import com.revature.revworkforce.service.GoalService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.containsString;

class PerformanceReviewControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PerformanceReviewService reviewService;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private GoalService goalService;

    @InjectMocks
    private PerformanceReviewController controller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    private PerformanceReviewDTO createValidSelfAssessmentDTO() {
        PerformanceReviewDTO dto = new PerformanceReviewDTO();
        dto.setReviewYear(2024);
        dto.setSelfAssessmentText("This is a valid self assessment with enough characters.");
        dto.setAchievements("Achieved all major goals set for the previous quarter.");
        dto.setImprovementAreas("I need to focus more on documentation and unit testing.");
        dto.setSelfAssessmentRating(BigDecimal.valueOf(4.0));
        return dto;
    }

    private PerformanceReviewDTO createValidManagerReviewDTO() {
        PerformanceReviewDTO dto = createValidSelfAssessmentDTO();
        dto.setManagerFeedback("Great job this year. You have consistently met and exceeded expectations in most areas. Keep it up!");
        dto.setManagerRating(BigDecimal.valueOf(4.5));
        return dto;
    }

    // =========================================
    // EMPLOYEE ENDPOINTS
    // =========================================

    @Test
    void viewMyReviews_Success() throws Exception {
        try (MockedStatic<SecurityUtils> security = mockStatic(SecurityUtils.class)) {
            security.when(SecurityUtils::getCurrentUsername).thenReturn("EMP001");
            when(reviewService.getEmployeeReviews("EMP001")).thenReturn(Collections.emptyList());
            when(reviewService.getAverageRating("EMP001")).thenReturn(BigDecimal.valueOf(4.5));
            
            Employee emp = new Employee();
            emp.setFirstName("John");
            emp.setLastName("Doe");
            when(employeeService.getEmployeeById("EMP001")).thenReturn(emp);

            mockMvc.perform(get("/employee/reviews"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("pages/employee/performance-goals"))
                    .andExpect(model().attribute("fullName", "John Doe"));
        }
    }

    @Test
    void viewMyReviews_SidebarException() throws Exception {
        try (MockedStatic<SecurityUtils> security = mockStatic(SecurityUtils.class)) {
            security.when(SecurityUtils::getCurrentUsername).thenReturn("EMP001");
            when(employeeService.getEmployeeById("EMP001")).thenThrow(new RuntimeException("Error"));

            mockMvc.perform(get("/employee/reviews"))
                    .andExpect(status().isOk())
                    .andExpect(model().attribute("fullName", "User"));
        }
    }

    @Test
    void showSelfAssessmentForm_Success() throws Exception {
        PerformanceReviewDTO dto = new PerformanceReviewDTO();
        dto.setEmployeeId("EMP001");
        dto.setStatus(ReviewStatus.PENDING_SELF_ASSESSMENT);

        try (MockedStatic<SecurityUtils> security = mockStatic(SecurityUtils.class)) {
            security.when(SecurityUtils::getCurrentUsername).thenReturn("EMP001");
            when(reviewService.getReviewDTOById(1L)).thenReturn(dto);
            when(employeeService.getEmployeeById("EMP001")).thenReturn(new Employee());

            mockMvc.perform(get("/employee/reviews/self-assessment/1"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("pages/employee/self-assessment"));
        }
    }

    @Test
    void showSelfAssessmentForm_NotOwner() throws Exception {
        PerformanceReviewDTO dto = new PerformanceReviewDTO();
        dto.setEmployeeId("EMP002");

        try (MockedStatic<SecurityUtils> security = mockStatic(SecurityUtils.class)) {
            security.when(SecurityUtils::getCurrentUsername).thenReturn("EMP001");
            when(reviewService.getReviewDTOById(1L)).thenReturn(dto);

            mockMvc.perform(get("/employee/reviews/self-assessment/1"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(flash().attributeExists("error"));
        }
    }

    @Test
    void showSelfAssessmentForm_WrongStatus() throws Exception {
        PerformanceReviewDTO dto = new PerformanceReviewDTO();
        dto.setEmployeeId("EMP001");
        dto.setStatus(ReviewStatus.COMPLETED);

        try (MockedStatic<SecurityUtils> security = mockStatic(SecurityUtils.class)) {
            security.when(SecurityUtils::getCurrentUsername).thenReturn("EMP001");
            when(reviewService.getReviewDTOById(1L)).thenReturn(dto);

            mockMvc.perform(get("/employee/reviews/self-assessment/1"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(flash().attribute("error", containsString("Self-assessment can only be submitted")));
        }
    }

    @Test
    void submitSelfAssessment_ValidationError() throws Exception {
        mockMvc.perform(post("/employee/reviews/self-assessment/1")
                .flashAttr("reviewDTO", new PerformanceReviewDTO()))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/employee/self-assessment"))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    void submitSelfAssessment_Success() throws Exception {
        try (MockedStatic<SecurityUtils> security = mockStatic(SecurityUtils.class)) {
            security.when(SecurityUtils::getCurrentUsername).thenReturn("EMP001");
            
            mockMvc.perform(post("/employee/reviews/self-assessment/1")
                    .flashAttr("reviewDTO", createValidSelfAssessmentDTO()))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/employee/reviews"));
            
            verify(reviewService).submitSelfAssessment(eq(1L), any(), eq("EMP001"));
        }
    }

    @Test
    void submitSelfAssessment_Exception() throws Exception {
        try (MockedStatic<SecurityUtils> security = mockStatic(SecurityUtils.class)) {
            security.when(SecurityUtils::getCurrentUsername).thenReturn("EMP001");
            doThrow(new RuntimeException("fail")).when(reviewService).submitSelfAssessment(any(), any(), any());

            mockMvc.perform(post("/employee/reviews/self-assessment/1")
                    .flashAttr("reviewDTO", createValidSelfAssessmentDTO()))
                    .andExpect(status().isOk())
                    .andExpect(model().attribute("error", "fail"));
        }
    }

    // =========================================
    // MANAGER ENDPOINTS
    // =========================================

    @Test
    void viewTeamReviews_Success() throws Exception {
        try (MockedStatic<SecurityUtils> security = mockStatic(SecurityUtils.class)) {
            security.when(SecurityUtils::getCurrentUsername).thenReturn("MGR001");
            
            mockMvc.perform(get("/manager/reviews"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("pages/manager/performance-review"));
        }
    }

    @Test
    void viewPendingReviews_Success() throws Exception {
        try (MockedStatic<SecurityUtils> security = mockStatic(SecurityUtils.class)) {
            security.when(SecurityUtils::getCurrentUsername).thenReturn("MGR001");
            
            mockMvc.perform(get("/manager/reviews/pending"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("pages/manager/pending-reviews"));
        }
    }

    @Test
    void showCreateReviewForm_Success() throws Exception {
        try (MockedStatic<SecurityUtils> security = mockStatic(SecurityUtils.class)) {
            security.when(SecurityUtils::getCurrentUsername).thenReturn("MGR001");
            
            mockMvc.perform(get("/manager/reviews/create"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("pages/manager/review-create"));
        }
    }

    @Test
    void createReview_BindingError() throws Exception {
        try (MockedStatic<SecurityUtils> security = mockStatic(SecurityUtils.class)) {
            security.when(SecurityUtils::getCurrentUsername).thenReturn("MGR001");
            
            mockMvc.perform(post("/manager/reviews/create")
                    )
                    .andExpect(status().isOk())
                    .andExpect(view().name("pages/manager/review-create"));
        }
    }

    @Test
    void createReview_Success() throws Exception {
        try (MockedStatic<SecurityUtils> security = mockStatic(SecurityUtils.class)) {
            security.when(SecurityUtils::getCurrentUsername).thenReturn("MGR001");
            
            mockMvc.perform(post("/manager/reviews/create")
                    .param("employeeId", "EMP001")
                    .param("reviewYear", "2024"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/manager/reviews"));
        }
    }

    @Test
    void showManagerReviewForm_Success() throws Exception {
        PerformanceReviewDTO dto = new PerformanceReviewDTO();
        dto.setStatus(ReviewStatus.PENDING_MANAGER_REVIEW);

        try (MockedStatic<SecurityUtils> security = mockStatic(SecurityUtils.class)) {
            security.when(SecurityUtils::getCurrentUsername).thenReturn("MGR001");
            when(reviewService.getReviewDTOById(1L)).thenReturn(dto);

            mockMvc.perform(get("/manager/reviews/review/1"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("pages/manager/review-form"));
        }
    }

    @Test
    void showManagerReviewForm_WrongStatus() throws Exception {
        PerformanceReviewDTO dto = new PerformanceReviewDTO();
        dto.setStatus(ReviewStatus.COMPLETED);

        when(reviewService.getReviewDTOById(1L)).thenReturn(dto);

        mockMvc.perform(get("/manager/reviews/review/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("error"));
    }

    @Test
    void submitManagerReview_Success() throws Exception {
        try (MockedStatic<SecurityUtils> security = mockStatic(SecurityUtils.class)) {
            security.when(SecurityUtils::getCurrentUsername).thenReturn("MGR001");
            
            mockMvc.perform(post("/manager/reviews/review/1")
                    .flashAttr("reviewDTO", createValidManagerReviewDTO()))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/manager/reviews"));
        }
    }

    @Test
    void viewDetailedReview_Owner() throws Exception {
        PerformanceReviewDTO dto = new PerformanceReviewDTO();
        dto.setEmployeeId("EMP001");

        try (MockedStatic<SecurityUtils> security = mockStatic(SecurityUtils.class)) {
            security.when(SecurityUtils::getCurrentUsername).thenReturn("EMP001");
            security.when(() -> SecurityUtils.hasRole(anyString())).thenReturn(false);
            security.when(() -> SecurityUtils.hasRole("EMPLOYEE")).thenReturn(true);
            
            when(reviewService.getReviewDTOById(1L)).thenReturn(dto);
            when(employeeService.getEmployeeById("EMP001")).thenReturn(new Employee());

            mockMvc.perform(get("/manager/reviews/view/1"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("pages/manager/review-view"));
        }
    }

    @Test
    void viewDetailedReview_Forbidden() throws Exception {
        PerformanceReviewDTO dto = new PerformanceReviewDTO();
        dto.setEmployeeId("EMP002");

        try (MockedStatic<SecurityUtils> security = mockStatic(SecurityUtils.class)) {
            security.when(SecurityUtils::getCurrentUsername).thenReturn("EMP001");
            security.when(() -> SecurityUtils.hasRole("MANAGER")).thenReturn(false);
            security.when(() -> SecurityUtils.hasRole("ADMIN")).thenReturn(false);
            
            when(reviewService.getReviewDTOById(1L)).thenReturn(dto);

            mockMvc.perform(get("/manager/reviews/view/1"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/employee/performance"));
        }
    }

    @Test
    void deleteReview_Success() throws Exception {
        mockMvc.perform(post("/admin/reviews/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/reviews"));
        
        verify(reviewService).deleteReview(1L);
    }
    
}