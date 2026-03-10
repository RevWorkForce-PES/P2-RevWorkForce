package com.revature.revworkforce.serviceImpl;

import com.revature.revworkforce.dto.PerformanceReviewDTO;
import com.revature.revworkforce.dto.TeamPerformanceStatsDTO;
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

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
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
                employee.setLastName("Employee");
                employee.setManager(manager);

                review = new PerformanceReview();
                review.setReviewId(1L);
                review.setEmployee(employee);
                review.setReviewYear(2024);
                review.setStatus(ReviewStatus.PENDING_SELF_ASSESSMENT);
        }

        // =========================================================
        // CREATE REVIEW
        // =========================================================

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
                verify(reviewRepository).save(any());
                verify(auditService).createAuditLog(any(), any(), any(), any(), any(), any(), any(), any());
        }

        @Test
        void createReview_EmployeeNotFound() {
                PerformanceReviewDTO dto = new PerformanceReviewDTO();
                dto.setEmployeeId("UNKNOWN");
                when(employeeRepository.findById("UNKNOWN")).thenReturn(Optional.empty());

                assertThrows(ResourceNotFoundException.class, () -> reviewService.createReview(dto, "MGR001"));
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

        // =========================================================
        // SELF ASSESSMENT
        // =========================================================

        @Test
        void submitSelfAssessment_Success() {
                PerformanceReviewDTO dto = new PerformanceReviewDTO();
                dto.setAchievements("Achieved all major goals.");
                dto.setImprovementAreas("Documentation skills.");
                dto.setSelfAssessmentText("Consistent performance.");
                dto.setSelfAssessmentRating(BigDecimal.valueOf(4.0));

                when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
                when(reviewRepository.save(any(PerformanceReview.class))).thenReturn(review);

                PerformanceReview result = reviewService.submitSelfAssessment(1L, dto, "EMP001");

                assertThat(result.getStatus()).isEqualTo(ReviewStatus.PENDING_MANAGER_REVIEW);
                verify(notificationService).createNotification(any(), any(), any(), any(), any());
        }

        @Test
        void submitSelfAssessment_ValidationFailures() {
                when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

                PerformanceReviewDTO dto = new PerformanceReviewDTO();
                dto.setSelfAssessmentText("short");
                assertThrows(ValidationException.class, () -> reviewService.submitSelfAssessment(1L, dto, "EMP001"));

                dto.setSelfAssessmentText("valid length text");
                dto.setAchievements("short");
                assertThrows(ValidationException.class, () -> reviewService.submitSelfAssessment(1L, dto, "EMP001"));

                dto.setAchievements("valid length achievements");
                dto.setImprovementAreas("short");
                assertThrows(ValidationException.class, () -> reviewService.submitSelfAssessment(1L, dto, "EMP001"));
        }

        @Test
        void submitSelfAssessment_Unauthorized() {
                when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
                assertThrows(UnauthorizedException.class,
                                () -> reviewService.submitSelfAssessment(1L, new PerformanceReviewDTO(), "EMP002"));
        }

        @Test
        void submitSelfAssessment_WrongStatus() {
                review.setStatus(ReviewStatus.COMPLETED);
                when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
                assertThrows(ValidationException.class,
                                () -> reviewService.submitSelfAssessment(1L, new PerformanceReviewDTO(), "EMP001"));
        }

        // =========================================================
        // MANAGER REVIEW
        // =========================================================

        @Test
        void submitManagerReview_Success() {
                review.setStatus(ReviewStatus.PENDING_MANAGER_REVIEW);

                PerformanceReviewDTO dto = new PerformanceReviewDTO();
                dto.setManagerFeedback(
                                "Excellent performance throughout the year. Very impressed with technical skills.");
                dto.setTechnicalSkills(BigDecimal.valueOf(5.0));
                dto.setCommunication(BigDecimal.valueOf(4.0));
                dto.setTeamwork(BigDecimal.valueOf(4.0));
                dto.setLeadership(BigDecimal.valueOf(4.0));
                dto.setPunctuality(BigDecimal.valueOf(5.0));

                when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
                when(employeeRepository.findById("MGR001")).thenReturn(Optional.of(manager));
                when(reviewRepository.save(any(PerformanceReview.class))).thenReturn(review);

                PerformanceReview result = reviewService.submitManagerReview(1L, dto, "MGR001");

                assertThat(result.getStatus()).isEqualTo(ReviewStatus.COMPLETED);
                assertThat(result.getFinalRating()).isEqualTo(new BigDecimal("4.4"));
        }

        @Test
        void submitManagerReview_ValidationFailures() {
                review.setStatus(ReviewStatus.PENDING_MANAGER_REVIEW);
                when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
                when(employeeRepository.findById("MGR001")).thenReturn(Optional.of(manager));

                PerformanceReviewDTO dto = new PerformanceReviewDTO();
                dto.setTechnicalSkills(BigDecimal.valueOf(6.0)); // Out of range
                assertThrows(ValidationException.class, () -> reviewService.submitManagerReview(1L, dto, "MGR001"));

                dto.setTechnicalSkills(BigDecimal.valueOf(4.0));
                dto.setCommunication(BigDecimal.valueOf(4.0));
                dto.setTeamwork(BigDecimal.valueOf(4.0));
                dto.setLeadership(BigDecimal.valueOf(4.0));
                dto.setPunctuality(BigDecimal.valueOf(4.0));
                dto.setManagerFeedback(""); // Empty
                assertThrows(ValidationException.class, () -> reviewService.submitManagerReview(1L, dto, "MGR001"));
        }

        // =========================================================
        // STATS AND FETCH
        // =========================================================

        @Test
        void getTeamPerformanceStats_Success() {
                PerformanceReview r1 = new PerformanceReview();
                r1.setStatus(ReviewStatus.COMPLETED);
                r1.setFinalRating(BigDecimal.valueOf(4.8));

                PerformanceReview r2 = new PerformanceReview();
                r2.setStatus(ReviewStatus.COMPLETED);
                r2.setFinalRating(BigDecimal.valueOf(3.8));

                PerformanceReview r3 = new PerformanceReview();
                r3.setStatus(ReviewStatus.PENDING_SELF_ASSESSMENT);

                when(reviewRepository.findTeamReviewsByManagerId("MGR001")).thenReturn(Arrays.asList(r1, r2, r3));

                TeamPerformanceStatsDTO stats = reviewService.getTeamPerformanceStats("MGR001");

                assertThat(stats.getTotalReviews()).isEqualTo(3);
                assertThat(stats.getCompletedReviews()).isEqualTo(2);
                assertThat(stats.getOverallRating()).isEqualTo(new BigDecimal("4.3"));
                assertThat(stats.getRatingDistribution().get("5.0 (Outstanding)")).isEqualTo(1L);
                assertThat(stats.getRatingDistribution().get("4.0 (Exceeds)")).isEqualTo(1L);
        }

        @Test
        void getTeamPerformanceStats_Empty() {
                when(reviewRepository.findTeamReviewsByManagerId("MGR001")).thenReturn(Collections.emptyList());
                TeamPerformanceStatsDTO stats = reviewService.getTeamPerformanceStats("MGR001");
                assertThat(stats.getOverallRating()).isEqualTo(BigDecimal.ZERO);
        }

        @Test
        void getAverageRating_NoReviews() {
                when(employeeRepository.findById("EMP001")).thenReturn(Optional.of(employee));
                when(reviewRepository.findByEmployeeOrderByReviewYearDesc(employee))
                                .thenReturn(Collections.emptyList());
                assertThat(reviewService.getAverageRating("EMP001")).isNull();
        }

        @Test
        void completeReview_AlreadyCompleted() {
                review.setStatus(ReviewStatus.COMPLETED);
                when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
                assertThrows(ValidationException.class, () -> reviewService.completeReview(1L));
        }

        @Test
        void deleteReview_CompletedError() {
                review.setStatus(ReviewStatus.COMPLETED);
                when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
                assertThrows(ValidationException.class, () -> reviewService.deleteReview(1L));
        }

        @Test
        void updateDraft_Success() {
                PerformanceReviewDTO dto = new PerformanceReviewDTO();
                dto.setAchievements("Updated achiev.");

                when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
                when(reviewRepository.save(any())).thenReturn(review);

                PerformanceReview result = reviewService.updateDraft(1L, dto, "EMP001");
                assertThat(result.getAchievements()).isEqualTo("Updated achiev.");
        }
}
