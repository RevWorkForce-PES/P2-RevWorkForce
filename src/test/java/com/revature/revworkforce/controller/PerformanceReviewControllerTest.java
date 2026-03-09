package com.revature.revworkforce.controller;

import com.revature.revworkforce.dto.PerformanceReviewDTO;
import com.revature.revworkforce.enums.ReviewStatus;
import com.revature.revworkforce.repository.EmployeeRepository;
import com.revature.revworkforce.service.EmployeeService;
import com.revature.revworkforce.service.PerformanceReviewService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PerformanceReviewController.class)
class PerformanceReviewControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private PerformanceReviewService reviewService;

        @MockBean
        private EmployeeService employeeService;

        @MockBean
        private EmployeeRepository employeeRepository;

        @Test
        @WithMockUser(username = "user", roles = "EMPLOYEE")
        void viewMyReviews_Success() throws Exception {
                when(reviewService.getEmployeeReviews("user")).thenReturn(new ArrayList<>());
                when(reviewService.getAverageRating("user")).thenReturn(BigDecimal.valueOf(4.0));

                mockMvc.perform(get("/employee/reviews"))
                                .andExpect(status().isOk())
                                .andExpect(view().name("pages/employee/performance-goals"))
                                .andExpect(model().attributeExists("reviews", "averageRating"));
        }

        @Test
        @WithMockUser(username = "user", roles = "EMPLOYEE")
        void showSelfAssessmentForm_Success() throws Exception {
                PerformanceReviewDTO dto = new PerformanceReviewDTO();
                dto.setEmployeeId("user");
                dto.setStatus(ReviewStatus.PENDING_SELF_ASSESSMENT);

                when(reviewService.getReviewDTOById(1L)).thenReturn(dto);

                mockMvc.perform(get("/employee/reviews/self-assessment/1"))
                                .andExpect(status().isOk())
                                .andExpect(view().name("pages/employee/self-assessment"))
                                .andExpect(model().attributeExists("reviewDTO", "pageTitle"));
        }

        @Test
        @WithMockUser(username = "user", roles = "EMPLOYEE")
        void submitSelfAssessment_Success() throws Exception {
                PerformanceReviewDTO dto = new PerformanceReviewDTO();
                dto.setSelfAssessmentText(
                                "This is a very long self assessment text that definitely exceeds the 100 character minimum requirement for submission.");
                dto.setAchievements("Achievements");
                dto.setImprovementAreas("ImprovementAreas");
                dto.setSelfAssessmentRating(BigDecimal.valueOf(4.0));

                mockMvc.perform(post("/employee/reviews/self-assessment/1")
                                .flashAttr("reviewDTO", dto)
                                .with(csrf()))
                                .andExpect(status().is3xxRedirection())
                                .andExpect(redirectedUrl("/employee/reviews"))
                                .andExpect(flash().attribute("success", "Self-assessment submitted successfully!"));

                verify(reviewService).submitSelfAssessment(eq(1L), any(PerformanceReviewDTO.class), eq("user"));
        }

        @Test
        @WithMockUser(username = "manager", roles = "MANAGER")
        void viewPendingReviews_Success() throws Exception {
                when(reviewService.getPendingReviewsForManager("manager")).thenReturn(new ArrayList<>());

                mockMvc.perform(get("/manager/reviews/pending"))
                                .andExpect(status().isOk())
                                .andExpect(view().name("pages/manager/performance-review"))
                                .andExpect(model().attributeExists("reviews"));
        }

        @Test
        @WithMockUser(username = "manager", roles = "MANAGER")
        void showCreateReviewForm_Success() throws Exception {
                mockMvc.perform(get("/manager/reviews/create"))
                                .andExpect(status().isOk())
                                .andExpect(view().name("pages/manager/review-create"))
                                .andExpect(model().attributeExists("reviewDTO", "pageTitle"));
        }

        @Test
        @WithMockUser(username = "manager", roles = "MANAGER")
        void createReview_Success() throws Exception {
                PerformanceReviewDTO dto = new PerformanceReviewDTO();
                dto.setEmployeeId("user");
                dto.setReviewYear(2024);

                mockMvc.perform(post("/manager/reviews/create")
                                .flashAttr("reviewDTO", dto)
                                .with(csrf()))
                                .andExpect(status().is3xxRedirection())
                                .andExpect(redirectedUrl("/manager/reviews"))
                                .andExpect(flash().attribute("success", "Performance review created successfully!"));

                verify(reviewService).createReview(any(PerformanceReviewDTO.class), eq("manager"));
        }

        @Test
        @WithMockUser(username = "manager", roles = "MANAGER")
        void submitManagerReview_Success() throws Exception {
                PerformanceReviewDTO dto = new PerformanceReviewDTO();
                dto.setManagerFeedback(
                                "This is a long manager feedback text that meets the 50 character minimum requirement.");
                dto.setTechnicalSkills(BigDecimal.valueOf(4.0));
                dto.setCommunication(BigDecimal.valueOf(4.0));
                dto.setTeamwork(BigDecimal.valueOf(4.0));
                dto.setLeadership(BigDecimal.valueOf(4.0));
                dto.setPunctuality(BigDecimal.valueOf(4.0));
                dto.setManagerRating(BigDecimal.valueOf(4.0));

                mockMvc.perform(post("/manager/reviews/review/1")
                                .flashAttr("reviewDTO", dto)
                                .with(csrf()))
                                .andExpect(status().is3xxRedirection())
                                .andExpect(redirectedUrl("/manager/reviews"))
                                .andExpect(flash().attribute("success", "Manager evaluation submitted successfully!"));

                verify(reviewService).submitManagerReview(eq(1L), any(PerformanceReviewDTO.class), eq("manager"));
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        void viewDetailedReview_Success() throws Exception {
                when(reviewService.getReviewDTOById(1L)).thenReturn(new PerformanceReviewDTO());

                mockMvc.perform(get("/manager/reviews/view/1"))
                                .andExpect(status().isOk())
                                .andExpect(view().name("pages/manager/review-view"))
                                .andExpect(model().attributeExists("review", "pageTitle"));
        }
}
