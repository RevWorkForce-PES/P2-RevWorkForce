package com.revature.revworkforce.controller;

import com.revature.revworkforce.dto.PerformanceReviewDTO;
import com.revature.revworkforce.model.PerformanceReview;
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
        void createReview_Success() throws Exception {
                PerformanceReview review = new PerformanceReview();
                review.setReviewId(1L);
                when(reviewService.reviewExists("user", 2024)).thenReturn(false);
                when(reviewService.createReview(eq("user"), eq(2024), eq("user"))).thenReturn(review);

                mockMvc.perform(post("/employee/reviews/create")
                                .param("reviewYear", "2024")
                                .with(csrf()))
                                .andExpect(status().is3xxRedirection())
                                .andExpect(redirectedUrl("/employee/reviews/edit/1"))
                                .andExpect(flash().attribute("success",
                                                "Performance review created successfully! Please fill in the details."));
        }

        @Test
        @WithMockUser(username = "user", roles = "EMPLOYEE")
        void submitReview_Success() throws Exception {
                mockMvc.perform(post("/employee/reviews/submit/1")
                                .param("keyDeliverables", "D")
                                .param("majorAccomplishments", "A")
                                .param("areasOfImprovement", "I")
                                .param("selfAssessmentRating", "4.0")
                                .with(csrf()))
                                .andExpect(status().is3xxRedirection())
                                .andExpect(redirectedUrl("/employee/performance"))
                                .andExpect(flash().attribute("success",
                                                "Performance review submitted to manager successfully!"));

                verify(reviewService).submitSelfAssessment(eq(1L), eq("user"), anyString(), anyString(), anyString(),
                                any(),
                                any());
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
        void submitManagerReview_Success() throws Exception {
                PerformanceReviewDTO dto = new PerformanceReviewDTO();
                dto.setReviewYear(2024);
                dto.setManagerRating(BigDecimal.valueOf(4.0));
                dto.setManagerFeedback("Feedback");

                mockMvc.perform(post("/manager/reviews/review/1")
                                .flashAttr("reviewDTO", dto)
                                .with(csrf()))
                                .andExpect(status().is3xxRedirection())
                                .andExpect(redirectedUrl("/manager/performance"))
                                .andExpect(flash().attribute("success", "Review feedback submitted successfully!"));

                verify(reviewService).submitManagerReview(eq(1L), eq("manager"), any(), any(), any());
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        void deleteReview_Success() throws Exception {
                mockMvc.perform(post("/admin/reviews/delete/1")
                                .with(csrf()))
                                .andExpect(status().is3xxRedirection())
                                .andExpect(redirectedUrl("/admin/reviews"))
                                .andExpect(flash().attribute("success", "Review deleted successfully!"));

                verify(reviewService).deleteReview(1L);
        }
}
