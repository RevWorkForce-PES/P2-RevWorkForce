package com.revature.revworkforce.controller;

import com.revature.revworkforce.dto.PerformanceReviewDTO;
import com.revature.revworkforce.security.SecurityUtils;
import com.revature.revworkforce.service.EmployeeService;
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

class PerformanceReviewControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PerformanceReviewService reviewService;

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private PerformanceReviewController controller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    // =========================================
    // Employee Reviews
    // =========================================

    @Test
    void viewMyReviews_ShouldReturnEmployeePage() throws Exception {

        try (MockedStatic<SecurityUtils> security = mockStatic(SecurityUtils.class)) {

            security.when(SecurityUtils::getCurrentUsername).thenReturn("EMP001");

            when(reviewService.getEmployeeReviews("EMP001"))
                    .thenReturn(List.of());

            when(reviewService.getAverageRating("EMP001"))
                    .thenReturn(BigDecimal.valueOf(4.0));

            mockMvc.perform(get("/employee/reviews"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("pages/employee/performance-goals"))
                    .andExpect(model().attributeExists("reviews"));
        }
    }

    // =========================================
    // Manager Team Reviews
    // =========================================

    @Test
    void viewTeamReviews_ShouldReturnManagerPage() throws Exception {

        try (MockedStatic<SecurityUtils> security = mockStatic(SecurityUtils.class)) {

            security.when(SecurityUtils::getCurrentUsername).thenReturn("MGR001");

            when(reviewService.getTeamReviews("MGR001"))
                    .thenReturn(List.of());

            mockMvc.perform(get("/manager/reviews"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("pages/manager/performance-review"))
                    .andExpect(model().attributeExists("reviews"));
        }
    }

    // =========================================
    // Pending Reviews
    // =========================================

    @Test
    void viewPendingReviews_ShouldReturnPendingPage() throws Exception {

        try (MockedStatic<SecurityUtils> security = mockStatic(SecurityUtils.class)) {

            security.when(SecurityUtils::getCurrentUsername).thenReturn("MGR001");

            when(reviewService.getPendingReviewsForManager("MGR001"))
                    .thenReturn(List.of());

            mockMvc.perform(get("/manager/reviews/pending"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("pages/manager/pending-reviews"))
                    .andExpect(model().attributeExists("reviews"));
        }
    }

    // =========================================
    // Create Review Form
    // =========================================

    @Test
    void showCreateReviewForm_ShouldReturnForm() throws Exception {

        try (MockedStatic<SecurityUtils> security = mockStatic(SecurityUtils.class)) {

            security.when(SecurityUtils::getCurrentUsername).thenReturn("MGR001");

            when(employeeService.getTeamMembers("MGR001"))
                    .thenReturn(List.of());

            mockMvc.perform(get("/manager/reviews/create"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("pages/manager/review-create"))
                    .andExpect(model().attributeExists("reviewDTO"));
        }
    }

    // =========================================
    // Create Review
    // =========================================

    @Test
    void createReview_ShouldRedirect() throws Exception {

        try (MockedStatic<SecurityUtils> security = mockStatic(SecurityUtils.class)) {

            security.when(SecurityUtils::getCurrentUsername).thenReturn("MGR001");

            mockMvc.perform(post("/manager/reviews/create")
                    .param("employeeId", "EMP001")
                    .param("reviewYear", "2025"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/manager/reviews"));

            verify(reviewService).createReview(any(PerformanceReviewDTO.class), eq("MGR001"));
        }
    }

    // =========================================
    // Delete Review
    // =========================================

    @Test
    void deleteReview_ShouldRedirect() throws Exception {

        mockMvc.perform(post("/admin/reviews/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/reviews"));

        verify(reviewService).deleteReview(1L);
    }
    
}