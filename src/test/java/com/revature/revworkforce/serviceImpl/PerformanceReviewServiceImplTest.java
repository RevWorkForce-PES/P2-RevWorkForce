package com.revature.revworkforce.serviceImpl;

import com.revature.revworkforce.dto.PerformanceReviewDTO;
import com.revature.revworkforce.enums.ReviewStatus;
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

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
    private PerformanceReviewServiceImpl reviewService;

    private Employee employee;
    private Employee manager;
    private PerformanceReview review;

    @BeforeEach
    void setUp() {
        manager = new Employee();
        manager.setEmployeeId("MGR001");
        manager.setFirstName("Jane");
        manager.setLastName("Manager");

        employee = new Employee();
        employee.setEmployeeId("EMP001");
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setManager(manager);

        review = new PerformanceReview();
        review.setReviewId(1L);
        review.setEmployee(employee);
        review.setReviewYear(2024);
        review.setStatus(ReviewStatus.DRAFT);
    }

    @Test
    void createReview_Success() {
        when(employeeRepository.findById("EMP001")).thenReturn(Optional.of(employee));
        when(reviewRepository.existsByEmployeeAndReviewYear(employee, 2024)).thenReturn(false);
        when(reviewRepository.save(any(PerformanceReview.class))).thenReturn(review);

        PerformanceReview result = reviewService.createReview("EMP001", 2024, "MGR001");

        assertThat(result).isNotNull();
        verify(reviewRepository).save(any(PerformanceReview.class));
        verify(auditService).createAuditLog(anyString(), anyString(), anyString(), anyString(), any(), any(), any(),
                any());
    }

    @Test
    void createReview_AlreadyExists() {
        when(employeeRepository.findById("EMP001")).thenReturn(Optional.of(employee));
        when(reviewRepository.existsByEmployeeAndReviewYear(employee, 2024)).thenReturn(true);

        assertThrows(ValidationException.class, () -> reviewService.createReview("EMP001", 2024, "MGR001"));
    }

    @Test
    void submitSelfAssessment_Success() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
        when(reviewRepository.save(any(PerformanceReview.class))).thenReturn(review);

        PerformanceReview result = reviewService.submitSelfAssessment(1L, "EMP001", "Deliv", "Accomp", "Improv",
                BigDecimal.valueOf(4.0), "Comments");

        assertThat(result.getStatus()).isEqualTo(ReviewStatus.SUBMITTED);
        verify(notificationService).createNotification(any(), any(), any(), any(), any());
    }

    @Test
    void submitManagerReview_Success() {
        review.setStatus(ReviewStatus.SUBMITTED);
        review.setSelfAssessmentRating(BigDecimal.valueOf(4.0));
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
        when(employeeRepository.findById("MGR001")).thenReturn(Optional.of(manager));
        when(reviewRepository.save(any(PerformanceReview.class))).thenReturn(review);

        PerformanceReview result = reviewService.submitManagerReview(1L, "MGR001", "Feedback", BigDecimal.valueOf(4.0),
                "Comments");

        assertThat(result.getStatus()).isEqualTo(ReviewStatus.COMPLETED);
        assertThat(result.getFinalRating()).isEqualTo(new BigDecimal("4.0"));
        verify(notificationService).createNotification(any(), any(), any(), any(), any());
    }

    @Test
    void getEmployeeReviews_Success() {
        when(employeeRepository.findById("EMP001")).thenReturn(Optional.of(employee));
        when(reviewRepository.findByEmployeeOrderByReviewYearDesc(employee)).thenReturn(Arrays.asList(review));

        List<PerformanceReviewDTO> result = reviewService.getEmployeeReviews("EMP001");

        assertThat(result).hasSize(1);
    }

    @Test
    void deleteReview_Success() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        reviewService.deleteReview(1L);

        verify(reviewRepository).delete(review);
    }

    @Test
    void deleteReview_Completed_ThrowsException() {
        review.setStatus(ReviewStatus.COMPLETED);
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        assertThrows(ValidationException.class, () -> reviewService.deleteReview(1L));
    }

    @Test
    void getAverageRating_Success() {
        review.setFinalRating(BigDecimal.valueOf(4.0));
        when(employeeRepository.findById("EMP001")).thenReturn(Optional.of(employee));
        when(reviewRepository.findByEmployeeOrderByReviewYearDesc(employee)).thenReturn(Arrays.asList(review));

        BigDecimal avg = reviewService.getAverageRating("EMP001");

        assertThat(avg).isEqualTo(new BigDecimal("4.0"));
    }

    @Test
    void submitSelfAssessment_Unauthorized_ThrowsException() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        assertThrows(com.revature.revworkforce.exception.UnauthorizedException.class,
                () -> reviewService.submitSelfAssessment(1L, "EMP002", "D", "A", "I", BigDecimal.valueOf(4.0), "C"));
    }

    @Test
    void submitSelfAssessment_InvalidStatus_ThrowsException() {
        review.setStatus(ReviewStatus.SUBMITTED);
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        assertThrows(ValidationException.class,
                () -> reviewService.submitSelfAssessment(1L, "EMP001", "D", "A", "I", BigDecimal.valueOf(4.0), "C"));
    }

    @Test
    void submitManagerReview_Unauthorized_ThrowsException() {
        review.setStatus(ReviewStatus.SUBMITTED);
        Employee otherManager = new Employee();
        otherManager.setEmployeeId("MGR002");

        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
        when(employeeRepository.findById("MGR002")).thenReturn(Optional.of(otherManager));

        assertThrows(com.revature.revworkforce.exception.UnauthorizedException.class,
                () -> reviewService.submitManagerReview(1L, "MGR002", "F", BigDecimal.valueOf(4.0), "C"));
    }

    @Test
    void getReviewDTOById_Success() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        PerformanceReviewDTO result = reviewService.getReviewDTOById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getReviewYear()).isEqualTo(2024);
    }

    @Test
    void getPendingReviewsForManager_Success() {
        when(reviewRepository.findPendingReviewsByManagerId("MGR001", ReviewStatus.SUBMITTED))
                .thenReturn(List.of(review));

        List<PerformanceReviewDTO> result = reviewService.getPendingReviewsForManager("MGR001");

        assertThat(result).hasSize(1);
    }

    @Test
    void getTeamReviews_Success() {
        when(reviewRepository.findTeamReviewsByManagerId("MGR001")).thenReturn(List.of(review));

        List<PerformanceReviewDTO> result = reviewService.getTeamReviews("MGR001");

        assertThat(result).hasSize(1);
    }

    @Test
    void getReviewsByStatus_Success() {
        when(reviewRepository.findByStatus(ReviewStatus.DRAFT)).thenReturn(List.of(review));

        List<PerformanceReviewDTO> result = reviewService.getReviewsByStatus(ReviewStatus.DRAFT);

        assertThat(result).hasSize(1);
    }

    @Test
    void updateDraft_Unauthorized_ThrowsException() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        assertThrows(com.revature.revworkforce.exception.UnauthorizedException.class,
                () -> reviewService.updateDraft(1L, "EMP002", "D", "A", "I", BigDecimal.valueOf(4.0), "C"));
    }
}
