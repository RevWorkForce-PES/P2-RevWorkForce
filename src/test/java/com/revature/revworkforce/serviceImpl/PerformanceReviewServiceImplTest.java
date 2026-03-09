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
        review.setStatus(ReviewStatus.PENDING_SELF_ASSESSMENT);
    }

    @Test
    void createReview_Success() {
        PerformanceReviewDTO dto = new PerformanceReviewDTO();
        dto.setEmployeeId("EMP001");
        dto.setReviewYear(2024);

        when(employeeRepository.findById("EMP001")).thenReturn(Optional.of(employee));
        when(reviewRepository.existsByEmployeeAndReviewYear(employee, 2024)).thenReturn(false);
        when(reviewRepository.save(any(PerformanceReview.class))).thenReturn(review);

        PerformanceReview result = reviewService.createReview(dto, "MGR001");

        assertThat(result).isNotNull();
        verify(reviewRepository).save(any(PerformanceReview.class));
        verify(auditService).createAuditLog(anyString(), anyString(), anyString(), anyString(), any(), any(), any(),
                any());
    }

    @Test
    void createReview_AlreadyExists() {
        PerformanceReviewDTO dto = new PerformanceReviewDTO();
        dto.setEmployeeId("EMP001");
        dto.setReviewYear(2024);

        when(employeeRepository.findById("EMP001")).thenReturn(Optional.of(employee));
        when(reviewRepository.existsByEmployeeAndReviewYear(employee, 2024)).thenReturn(true);

        assertThrows(ValidationException.class, () -> reviewService.createReview(dto, "MGR001"));
    }

    @Test
    void submitSelfAssessment_Success() {
        PerformanceReviewDTO dto = new PerformanceReviewDTO();
        dto.setAchievements(
                "This is a long achievement text that meets the minimum length requirement for validation if handled in service.");
        dto.setImprovementAreas("This is a long improvement area text that meets the minimum length requirement.");
        dto.setSelfAssessmentText(
                "This is a very long self assessment text that definitely exceeds the 100 character minimum requirement for submission.");
        dto.setSelfAssessmentRating(BigDecimal.valueOf(4.0));

        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
        when(reviewRepository.save(any(PerformanceReview.class))).thenReturn(review);

        PerformanceReview result = reviewService.submitSelfAssessment(1L, dto, "EMP001");

        assertThat(result.getStatus()).isEqualTo(ReviewStatus.PENDING_MANAGER_REVIEW);
        verify(notificationService).createNotification(any(), any(), any(), any(), any());
    }

    @Test
    void submitManagerReview_Success() {
        review.setStatus(ReviewStatus.PENDING_MANAGER_REVIEW);

        PerformanceReviewDTO dto = new PerformanceReviewDTO();
        dto.setManagerFeedback("This is a long manager feedback text that meets the 50 character minimum requirement.");
        dto.setTechnicalSkills(BigDecimal.valueOf(4.0));
        dto.setCommunication(BigDecimal.valueOf(4.0));
        dto.setTeamwork(BigDecimal.valueOf(4.0));
        dto.setLeadership(BigDecimal.valueOf(4.0));
        dto.setPunctuality(BigDecimal.valueOf(4.0));
        dto.setManagerRating(BigDecimal.valueOf(4.0));

        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
        when(employeeRepository.findById("MGR001")).thenReturn(Optional.of(manager));
        when(reviewRepository.save(any(PerformanceReview.class))).thenReturn(review);

        PerformanceReview result = reviewService.submitManagerReview(1L, dto, "MGR001");

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
                () -> reviewService.submitSelfAssessment(1L, new PerformanceReviewDTO(), "EMP002"));
    }

    @Test
    void submitSelfAssessment_InvalidStatus_ThrowsException() {
        review.setStatus(ReviewStatus.PENDING_MANAGER_REVIEW);
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        assertThrows(ValidationException.class,
                () -> reviewService.submitSelfAssessment(1L, new PerformanceReviewDTO(), "EMP001"));
    }

    @Test
    void submitManagerReview_Unauthorized_ThrowsException() {
        review.setStatus(ReviewStatus.PENDING_MANAGER_REVIEW);
        Employee otherManager = new Employee();
        otherManager.setEmployeeId("MGR002");

        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
        when(employeeRepository.findById("MGR002")).thenReturn(Optional.of(otherManager));

        assertThrows(com.revature.revworkforce.exception.UnauthorizedException.class,
                () -> reviewService.submitManagerReview(1L, new PerformanceReviewDTO(), "MGR002"));
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
        when(reviewRepository.findPendingReviewsByManagerId("MGR001", ReviewStatus.PENDING_MANAGER_REVIEW))
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
        when(reviewRepository.findByStatus(ReviewStatus.PENDING_SELF_ASSESSMENT)).thenReturn(List.of(review));

        List<PerformanceReviewDTO> result = reviewService.getReviewsByStatus(ReviewStatus.PENDING_SELF_ASSESSMENT);

        assertThat(result).hasSize(1);
    }

    @Test
    void updateDraft_Unauthorized_ThrowsException() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        assertThrows(com.revature.revworkforce.exception.UnauthorizedException.class,
                () -> reviewService.updateDraft(1L, new PerformanceReviewDTO(), "EMP002"));
    }
}
