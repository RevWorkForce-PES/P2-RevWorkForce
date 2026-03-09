package com.revature.revworkforce.service;

import java.math.BigDecimal;
import java.util.List;

import com.revature.revworkforce.dto.PerformanceReviewDTO;
import com.revature.revworkforce.enums.ReviewStatus;
import com.revature.revworkforce.model.PerformanceReview;

public interface PerformanceReviewService {

        PerformanceReview createReview(String employeeId, Integer reviewYear, String createdByManagerId);

        PerformanceReview submitSelfAssessment(
                        Long reviewId,
                        String employeeId,
                        String keyDeliverables,
                        String majorAccomplishments,
                        String areasOfImprovement,
                        BigDecimal selfAssessmentRating,
                        String selfAssessmentComments);

        PerformanceReview submitManagerReview(
                        Long reviewId,
                        String managerId,
                        String managerFeedback,
                        BigDecimal managerRating,
                        String managerComments);

        PerformanceReview completeReview(Long reviewId);

        PerformanceReview updateDraft(
                        Long reviewId,
                        String employeeId,
                        String keyDeliverables,
                        String majorAccomplishments,
                        String areasOfImprovement,
                        BigDecimal selfAssessmentRating,
                        String selfAssessmentComments);

        PerformanceReview getReviewById(Long reviewId);

        PerformanceReviewDTO getReviewDTOById(Long reviewId);

        List<PerformanceReviewDTO> getEmployeeReviews(String employeeId);

        List<PerformanceReviewDTO> getPendingReviewsForManager(String managerId);

        List<PerformanceReviewDTO> getTeamReviews(String managerId);

        List<PerformanceReviewDTO> getReviewsByStatus(ReviewStatus status);

        PerformanceReview getEmployeeReviewByYear(String employeeId, Integer year);

        void deleteReview(Long reviewId);

        boolean reviewExists(String employeeId, Integer year);

        BigDecimal getAverageRating(String employeeId);

        PerformanceReviewDTO convertToDTO(PerformanceReview review);
}