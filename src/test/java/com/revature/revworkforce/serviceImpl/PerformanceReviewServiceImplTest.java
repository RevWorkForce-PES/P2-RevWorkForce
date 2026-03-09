package com.revature.revworkforce.serviceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

import com.revature.revworkforce.enums.ReviewStatus;
import com.revature.revworkforce.exception.ResourceNotFoundException;
import com.revature.revworkforce.exception.UnauthorizedException;
import com.revature.revworkforce.exception.ValidationException;
import com.revature.revworkforce.model.Employee;
import com.revature.revworkforce.model.PerformanceReview;
import com.revature.revworkforce.repository.EmployeeRepository;
import com.revature.revworkforce.repository.PerformanceReviewRepository;
import com.revature.revworkforce.service.AuditService;
import com.revature.revworkforce.service.NotificationService;
import com.revature.revworkforce.service.impl.PerformanceReviewServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PerformanceReviewServiceImplTest {

    @Mock
    private PerformanceReviewRepository reviewRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private NotificationService notificationService;

    @Mock
    private AuditService auditService;

    @InjectMocks
    private PerformanceReviewServiceImpl performanceReviewService;

    private Employee employee;
    private Employee manager;
    private PerformanceReview review;

    @BeforeEach
    void setup() {

        manager = new Employee();
        manager.setEmployeeId("MGR001");
        manager.setFirstName("Manager");

        employee = new Employee();
        employee.setEmployeeId("EMP001");
        employee.setFirstName("Employee");
        employee.setManager(manager);

        review = new PerformanceReview();
        review.setReviewId(1L);
        review.setEmployee(employee);
        review.setReviewYear(2025);
        review.setStatus(ReviewStatus.DRAFT);
    }

    // ======================================================
    // CREATE REVIEW
    // ======================================================

    @Test
    void createReview_Success() {

        when(employeeRepository.findById("EMP001"))
                .thenReturn(Optional.of(employee));

        when(reviewRepository.existsByEmployeeAndReviewYear(employee, 2025))
                .thenReturn(false);

        when(reviewRepository.save(any(PerformanceReview.class)))
                .thenReturn(review);

        PerformanceReview result =
                performanceReviewService.createReview("EMP001", 2025, "MGR001");

        assertNotNull(result);
        verify(reviewRepository).save(any(PerformanceReview.class));
    }

    @Test
    void createReview_DuplicateReview_ThrowsException() {

        when(employeeRepository.findById("EMP001"))
                .thenReturn(Optional.of(employee));

        when(reviewRepository.existsByEmployeeAndReviewYear(employee, 2025))
                .thenReturn(true);

        assertThrows(ValidationException.class, () ->
                performanceReviewService.createReview("EMP001", 2025, "MGR001"));
    }

    // ======================================================
    // GET REVIEW
    // ======================================================

    @Test
    void getReviewById_Success() {

        when(reviewRepository.findById(1L))
                .thenReturn(Optional.of(review));

        PerformanceReview result = performanceReviewService.getReviewById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getReviewId());
    }

    @Test
    void getReviewById_NotFound() {

        when(reviewRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> performanceReviewService.getReviewById(1L));
    }

    // ======================================================
    // SELF ASSESSMENT
    // ======================================================

    @Test
    void submitSelfAssessment_Success() {

        when(reviewRepository.findById(1L))
                .thenReturn(Optional.of(review));

        when(reviewRepository.save(any()))
                .thenReturn(review);

        PerformanceReview result =
                performanceReviewService.submitSelfAssessment(
                        1L,
                        "EMP001",
                        "Deliverables",
                        "Achievements",
                        "Improvements",
                        BigDecimal.valueOf(4),
                        "Good performance"
                );

        assertEquals(ReviewStatus.SUBMITTED, result.getStatus());
        verify(notificationService).createNotification(any(), any(), any(), any(), any());
    }

    @Test
    void submitSelfAssessment_Unauthorized() {

        when(reviewRepository.findById(1L))
                .thenReturn(Optional.of(review));

        assertThrows(UnauthorizedException.class, () ->
                performanceReviewService.submitSelfAssessment(
                        1L,
                        "EMP999",
                        "Deliverables",
                        "Achievements",
                        "Improvements",
                        BigDecimal.valueOf(4),
                        "Comments"));
    }

    // ======================================================
    // MANAGER REVIEW
    // ======================================================

    @Test
    void submitManagerReview_Success() {

        review.setStatus(ReviewStatus.SUBMITTED);

        when(reviewRepository.findById(1L))
                .thenReturn(Optional.of(review));

        when(employeeRepository.findById("MGR001"))
                .thenReturn(Optional.of(manager));

        when(reviewRepository.save(any()))
                .thenReturn(review);

        PerformanceReview result =
                performanceReviewService.submitManagerReview(
                        1L,
                        "MGR001",
                        "Good work",
                        BigDecimal.valueOf(4),
                        "Keep improving"
                );

        assertEquals(ReviewStatus.COMPLETED, result.getStatus());
        verify(notificationService).createNotification(any(), any(), any(), any(), any());
    }

    @Test
    void submitManagerReview_InvalidRating() {

        review.setStatus(ReviewStatus.SUBMITTED);

        when(reviewRepository.findById(1L))
                .thenReturn(Optional.of(review));

        when(employeeRepository.findById("MGR001"))
                .thenReturn(Optional.of(manager));

        assertThrows(ValidationException.class, () ->
                performanceReviewService.submitManagerReview(
                        1L,
                        "MGR001",
                        "Feedback",
                        BigDecimal.valueOf(10),
                        "Comment"));
    }

    // ======================================================
    // DELETE REVIEW
    // ======================================================

    @Test
    void deleteReview_Success() {

        review.setStatus(ReviewStatus.DRAFT);

        when(reviewRepository.findById(1L))
                .thenReturn(Optional.of(review));

        performanceReviewService.deleteReview(1L);

        verify(reviewRepository).delete(review);
    }

    @Test
    void deleteReview_CompletedReview_ThrowsException() {

        review.setStatus(ReviewStatus.COMPLETED);

        when(reviewRepository.findById(1L))
                .thenReturn(Optional.of(review));

        assertThrows(ValidationException.class, () ->
                performanceReviewService.deleteReview(1L));
    }

    // ======================================================
    // GET EMPLOYEE REVIEWS
    // ======================================================

    @Test
    void getEmployeeReviews_Success() {

        when(employeeRepository.findById("EMP001"))
                .thenReturn(Optional.of(employee));

        when(reviewRepository.findByEmployeeOrderByReviewYearDesc(employee))
                .thenReturn(List.of(review));

        var result = performanceReviewService.getEmployeeReviews("EMP001");

        assertEquals(1, result.size());
    }

    // ======================================================
    // AVERAGE RATING
    // ======================================================

    @Test
    void getAverageRating_Success() {

        review.setFinalRating(BigDecimal.valueOf(4));

        when(employeeRepository.findById("EMP001"))
                .thenReturn(Optional.of(employee));

        when(reviewRepository.findByEmployeeOrderByReviewYearDesc(employee))
                .thenReturn(List.of(review));

        BigDecimal avg = performanceReviewService.getAverageRating("EMP001");

        assertEquals(BigDecimal.valueOf(4.0).setScale(1), avg);
    }

}