package com.revature.revworkforce.dto;

import com.revature.revworkforce.enums.ReviewStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PerformanceReviewDTOTest {

    private PerformanceReviewDTO dto;

    @BeforeEach
    void setUp() {
        dto = new PerformanceReviewDTO();
    }

    @Test
    void testSettersAndGetters() {
        LocalDate today = LocalDate.now();

        dto.setReviewId(1L);
        dto.setEmployeeId("E1001");
        dto.setEmployeeName("John Doe");
        dto.setDepartmentName("IT");
        dto.setDesignationName("Developer");
        dto.setReviewYear(2024);
        dto.setReviewPeriod("Q1");
        dto.setStatus(ReviewStatus.DRAFT);
        dto.setKeyDeliverables("Deliverable 1");
        dto.setMajorAccomplishments("Achievement 1");
        dto.setAreasOfImprovement("Improve communication");
        dto.setSelfAssessmentRating(BigDecimal.valueOf(4.2));
        dto.setSelfAssessmentComments("Did well");
        dto.setSubmittedDate(today);
        dto.setManagerFeedback("Good work");
        dto.setManagerRating(BigDecimal.valueOf(4.5));
        dto.setManagerComments("Excellent");
        dto.setFinalRating(BigDecimal.valueOf(4.6));
        dto.setReviewedByName("Manager X");
        dto.setReviewedDate(today);
        dto.setManagerId("M100");
        dto.setManagerName("Manager X");

        assertEquals(1L, dto.getReviewId());
        assertEquals("E1001", dto.getEmployeeId());
        assertEquals("John Doe", dto.getEmployeeName());
        assertEquals("IT", dto.getDepartmentName());
        assertEquals("Developer", dto.getDesignationName());
        assertEquals(2024, dto.getReviewYear());
        assertEquals("Q1", dto.getReviewPeriod());
        assertEquals(ReviewStatus.DRAFT, dto.getStatus());
        assertEquals("Deliverable 1", dto.getKeyDeliverables());
        assertEquals("Achievement 1", dto.getMajorAccomplishments());
        assertEquals("Improve communication", dto.getAreasOfImprovement());
        assertEquals(BigDecimal.valueOf(4.2), dto.getSelfAssessmentRating());
        assertEquals("Did well", dto.getSelfAssessmentComments());
        assertEquals(today, dto.getSubmittedDate());
        assertEquals("Good work", dto.getManagerFeedback());
        assertEquals(BigDecimal.valueOf(4.5), dto.getManagerRating());
        assertEquals("Excellent", dto.getManagerComments());
        assertEquals(BigDecimal.valueOf(4.6), dto.getFinalRating());
        assertEquals("Manager X", dto.getReviewedByName());
        assertEquals(today, dto.getReviewedDate());
        assertEquals("M100", dto.getManagerId());
        assertEquals("Manager X", dto.getManagerName());
    }

    @Test
    void testGetRatingLabel() {
        dto.setFinalRating(BigDecimal.valueOf(4.7));
        assertEquals("Exceptional", dto.getRatingLabel());

        dto.setFinalRating(BigDecimal.valueOf(4.0));
        assertEquals("Exceeds Expectations", dto.getRatingLabel());

        dto.setFinalRating(BigDecimal.valueOf(3.0));
        assertEquals("Meets Expectations", dto.getRatingLabel());

        dto.setFinalRating(BigDecimal.valueOf(2.0));
        assertEquals("Needs Improvement", dto.getRatingLabel());

        dto.setFinalRating(BigDecimal.valueOf(1.0));
        assertEquals("Unsatisfactory", dto.getRatingLabel());

        dto.setFinalRating(null);
        assertEquals("Not Rated", dto.getRatingLabel());
    }

    @Test
    void testStatusMethods() {
        dto.setStatus(ReviewStatus.DRAFT);
        assertTrue(dto.isEditable());
        assertTrue(dto.canSubmit());
        assertFalse(dto.canManagerReview());
        assertFalse(dto.canViewManagerFeedback());

        dto.setStatus(ReviewStatus.SUBMITTED);
        assertFalse(dto.isEditable());
        assertFalse(dto.canSubmit());
        assertTrue(dto.canManagerReview());
        assertFalse(dto.canViewManagerFeedback());

        dto.setStatus(ReviewStatus.REVIEWED);
        assertFalse(dto.isEditable());
        assertFalse(dto.canSubmit());
        assertFalse(dto.canManagerReview());
        assertTrue(dto.canViewManagerFeedback());

        dto.setStatus(ReviewStatus.COMPLETED);
        assertFalse(dto.isEditable());
        assertFalse(dto.canSubmit());
        assertFalse(dto.canManagerReview());
        assertTrue(dto.canViewManagerFeedback());
    }
}