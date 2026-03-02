package com.revature.revworkforce.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.revature.revworkforce.dto.PerformanceReviewDTO;
import com.revature.revworkforce.enums.ReviewStatus;
import com.revature.revworkforce.exception.ResourceNotFoundException;
import com.revature.revworkforce.exception.UnauthorizedException;
import com.revature.revworkforce.exception.ValidationException;
import com.revature.revworkforce.model.Employee;
import com.revature.revworkforce.model.PerformanceReview;
import com.revature.revworkforce.repository.EmployeeRepository;
import com.revature.revworkforce.repository.PerformanceReviewRepository;
import com.revature.revworkforce.service.PerformanceReviewService;
import com.revature.revworkforce.service.NotificationService;
import com.revature.revworkforce.enums.NotificationType;
import com.revature.revworkforce.enums.NotificationPriority;
@Service
@Transactional
public class PerformanceReviewServiceImpl implements PerformanceReviewService {

    private final PerformanceReviewRepository reviewRepository;
    private final EmployeeRepository employeeRepository;
    private final NotificationService notificationService;
    public PerformanceReviewServiceImpl(
            PerformanceReviewRepository reviewRepository,
            EmployeeRepository employeeRepository,
            NotificationService notificationService) {

        this.reviewRepository = reviewRepository;
        this.employeeRepository = employeeRepository;
        this.notificationService = notificationService;
    }

    // =========================================================
    // CREATE REVIEW
    // =========================================================

    @Override
    public PerformanceReview createReview(String employeeId, Integer reviewYear, String createdByManagerId) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee", "employeeId", employeeId));

        if (reviewRepository.existsByEmployeeAndReviewYear(employee, reviewYear)) {
            throw new ValidationException("Review already exists for this employee and year");
        }

        PerformanceReview review = new PerformanceReview();
        review.setEmployee(employee);
        review.setReviewYear(reviewYear);
        review.setReviewPeriod(reviewYear + " Annual Review");
        review.setStatus(ReviewStatus.DRAFT);
        review.setCreatedAt(LocalDateTime.now());
        review.setUpdatedAt(LocalDateTime.now());

        return reviewRepository.save(review);
    }

    // =========================================================
    // SELF ASSESSMENT
    // =========================================================

    @Override
    public PerformanceReview submitSelfAssessment(
            Long reviewId,
            String employeeId,
            String keyDeliverables,
            String majorAccomplishments,
            String areasOfImprovement,
            BigDecimal selfAssessmentRating,
            String selfAssessmentComments) {

        PerformanceReview review = getReviewById(reviewId);

        if (!review.getEmployee().getEmployeeId().equals(employeeId)) {
            throw new UnauthorizedException("You can only submit assessment for your own review");
        }

        if (review.getStatus() != ReviewStatus.DRAFT) {
            throw new ValidationException("Can only edit review in DRAFT status");
        }

        if (selfAssessmentRating != null &&
                (selfAssessmentRating.compareTo(BigDecimal.ONE) < 0 ||
                 selfAssessmentRating.compareTo(BigDecimal.valueOf(5)) > 0)) {
            throw new ValidationException("Self-assessment rating must be between 1 and 5");
        }

        review.setKeyDeliverables(keyDeliverables);
        review.setMajorAccomplishments(majorAccomplishments);
        review.setAreasOfImprovement(areasOfImprovement);
        review.setSelfAssessmentRating(selfAssessmentRating);
        review.setSelfAssessmentComments(selfAssessmentComments);
        review.setSubmittedDate(LocalDate.now());
     // 🔔 Notify Manager
        Employee manager = review.getEmployee().getManager();

        if (manager != null) {
            notificationService.createNotification(
                    manager,
                    NotificationType.PERFORMANCE,
                    "Performance Review Submitted",
                    review.getEmployee().getFullName() + 
                    " has submitted performance review for year " + review.getReviewYear(),
                    NotificationPriority.HIGH
            );
        }

        return reviewRepository.save(review);
    }

    // =========================================================
    // MANAGER REVIEW
    // =========================================================

    @Override
    public PerformanceReview submitManagerReview(
            Long reviewId,
            String managerId,
            String managerFeedback,
            BigDecimal managerRating,
            String managerComments) {

        PerformanceReview review = getReviewById(reviewId);

        Employee manager = employeeRepository.findById(managerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Manager", "employeeId", managerId));

        if (review.getEmployee().getManager() == null ||
            !review.getEmployee().getManager().getEmployeeId().equals(managerId)) {
            throw new UnauthorizedException("You can only review your direct reports");
        }

        if (review.getStatus() != ReviewStatus.SUBMITTED) {
            throw new ValidationException("Can only review a SUBMITTED review");
        }

        if (managerRating == null ||
            managerRating.compareTo(BigDecimal.ONE) < 0 ||
            managerRating.compareTo(BigDecimal.valueOf(5)) > 0) {
            throw new ValidationException("Manager rating must be between 1 and 5");
        }

        review.setManagerFeedback(managerFeedback);
        review.setManagerRating(managerRating);
        review.setManagerComments(managerComments);
        review.setReviewedBy(manager);
        review.setReviewedDate(LocalDate.now());

        if (review.getSelfAssessmentRating() != null) {
            BigDecimal finalRating = review.getSelfAssessmentRating()
                    .add(managerRating)
                    .divide(BigDecimal.valueOf(2), 1, BigDecimal.ROUND_HALF_UP);
            review.setFinalRating(finalRating);
        } else {
            review.setFinalRating(managerRating);
        }

     // 🔔 Notify Employee
        notificationService.createNotification(
                review.getEmployee(),
                NotificationType.PERFORMANCE,
                "Performance Review Reviewed",
                "Your performance review for year " + review.getReviewYear() +
                " has been reviewed by your manager.",
                NotificationPriority.HIGH
        );

        return reviewRepository.save(review);
    }

    // =========================================================
    // COMPLETE REVIEW
    // =========================================================

    @Override
    public PerformanceReview completeReview(Long reviewId) {

        PerformanceReview review = getReviewById(reviewId);

        if (review.getStatus() != ReviewStatus.REVIEWED) {
            throw new ValidationException("Can only complete a REVIEWED review");
        }

     // 🔔 Notify Employee
        notificationService.createNotification(
                review.getEmployee(),
                NotificationType.PERFORMANCE,
                "Performance Review Completed",
                "Your performance review for year " + review.getReviewYear() +
                " has been marked as completed.",
                NotificationPriority.NORMAL
        );

        return reviewRepository.save(review);
    }

    // =========================================================
    // FETCH METHODS
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public PerformanceReview getReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("PerformanceReview", "id", reviewId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PerformanceReview> getEmployeeReviews(String employeeId) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee", "employeeId", employeeId));

        return reviewRepository.findByEmployeeOrderByReviewYearDesc(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PerformanceReview> getPendingReviewsForManager(String managerId) {
        return reviewRepository.findPendingReviewsByManagerId(managerId, ReviewStatus.SUBMITTED);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PerformanceReview> getTeamReviews(String managerId) {
        return reviewRepository.findTeamReviewsByManagerId(managerId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PerformanceReview> getReviewsByStatus(ReviewStatus status) {
        return reviewRepository.findByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public PerformanceReview getEmployeeReviewByYear(String employeeId, Integer year) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee", "employeeId", employeeId));

        return reviewRepository.findByEmployeeAndReviewYear(employee, year)
                .orElseThrow(() ->
                        new ResourceNotFoundException("PerformanceReview",
                                "employeeId and year", employeeId + " - " + year));
    }

    // =========================================================
    // DELETE
    // =========================================================

    @Override
    public void deleteReview(Long reviewId) {

        PerformanceReview review = getReviewById(reviewId);

        if (review.getStatus() == ReviewStatus.COMPLETED) {
            throw new ValidationException("Cannot delete completed review");
        }

        reviewRepository.delete(review);
    }

    // =========================================================
    // UTIL METHODS
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public boolean reviewExists(String employeeId, Integer year) {

        Employee employee = employeeRepository.findById(employeeId).orElse(null);
        return employee != null && reviewRepository.existsByEmployeeAndReviewYear(employee, year);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getAverageRating(String employeeId) {

        List<PerformanceReview> reviews = getEmployeeReviews(employeeId);

        BigDecimal sum = BigDecimal.ZERO;
        int count = 0;

        for (PerformanceReview review : reviews) {
            if (review.getFinalRating() != null) {
                sum = sum.add(review.getFinalRating());
                count++;
            }
        }

        if (count == 0) return null;

        return sum.divide(BigDecimal.valueOf(count), 1, BigDecimal.ROUND_HALF_UP);
    }

    @Override
    @Transactional(readOnly = true)
    public PerformanceReviewDTO convertToDTO(PerformanceReview review) {

        PerformanceReviewDTO dto = new PerformanceReviewDTO();

        dto.setReviewId(review.getReviewId());
        dto.setEmployeeId(review.getEmployee().getEmployeeId());
        dto.setEmployeeName(review.getEmployee().getFullName());
        dto.setReviewYear(review.getReviewYear());
        dto.setReviewPeriod(review.getReviewPeriod());
        dto.setStatus(review.getStatus());
        dto.setKeyDeliverables(review.getKeyDeliverables());
        dto.setMajorAccomplishments(review.getMajorAccomplishments());
        dto.setAreasOfImprovement(review.getAreasOfImprovement());
        dto.setSelfAssessmentRating(review.getSelfAssessmentRating());
        dto.setSelfAssessmentComments(review.getSelfAssessmentComments());
        dto.setManagerFeedback(review.getManagerFeedback());
        dto.setManagerRating(review.getManagerRating());
        dto.setManagerComments(review.getManagerComments());
        dto.setFinalRating(review.getFinalRating());
        dto.setSubmittedDate(review.getSubmittedDate());
        dto.setReviewedDate(review.getReviewedDate());

        return dto;
    }
    @Override
    public PerformanceReview updateDraft(
            Long reviewId,
            String employeeId,
            String keyDeliverables,
            String majorAccomplishments,
            String areasOfImprovement,
            BigDecimal selfAssessmentRating,
            String selfAssessmentComments) {

        PerformanceReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("PerformanceReview", "id", reviewId));

        if (!review.getEmployee().getEmployeeId().equals(employeeId)) {
            throw new UnauthorizedException("You can only edit your own review");
        }

        if (review.getStatus() != ReviewStatus.DRAFT) {
            throw new ValidationException("Can only edit review in DRAFT status");
        }

        review.setKeyDeliverables(keyDeliverables);
        review.setMajorAccomplishments(majorAccomplishments);
        review.setAreasOfImprovement(areasOfImprovement);
        review.setSelfAssessmentRating(selfAssessmentRating);
        review.setSelfAssessmentComments(selfAssessmentComments);
        review.setUpdatedAt(LocalDateTime.now());

        return reviewRepository.save(review);
    }
}