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
import com.revature.revworkforce.service.AuditService;
import com.revature.revworkforce.service.NotificationService;
import com.revature.revworkforce.enums.NotificationType;
import com.revature.revworkforce.enums.NotificationPriority;
import com.revature.revworkforce.util.Constants;
import com.revature.revworkforce.security.SecurityUtils;

@Service
@Transactional
public class PerformanceReviewServiceImpl implements PerformanceReviewService {

    private final PerformanceReviewRepository reviewRepository;
    private final EmployeeRepository employeeRepository;
    private final NotificationService notificationService;
    private final AuditService auditService;

    public PerformanceReviewServiceImpl(
            PerformanceReviewRepository reviewRepository,
            EmployeeRepository employeeRepository,
            NotificationService notificationService,
            AuditService auditService) {

        this.reviewRepository = reviewRepository;
        this.employeeRepository = employeeRepository;
        this.notificationService = notificationService;
        this.auditService = auditService;
    }

    // =========================================================
    // CREATE REVIEW
    // =========================================================

    // =========================================================
    // CREATE REVIEW
    // =========================================================

    @Override
    public PerformanceReview createReview(PerformanceReviewDTO dto, String managerId) {

        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "employeeId", dto.getEmployeeId()));

        if (reviewRepository.existsByEmployeeAndReviewYear(employee, dto.getReviewYear())) {
            throw new ValidationException("Review already exists for this employee and year");
        }

        PerformanceReview review = new PerformanceReview();
        review.setEmployee(employee);
        review.setReviewYear(dto.getReviewYear());
        review.setReviewPeriod(dto.getReviewYear() + " Annual Review");
        review.setStatus(ReviewStatus.PENDING_SELF_ASSESSMENT);
        review.setCreatedAt(LocalDateTime.now());
        review.setUpdatedAt(LocalDateTime.now());

        PerformanceReview savedReview = reviewRepository.save(review);

        String performedBy = SecurityUtils.getCurrentUsername() != null ? SecurityUtils.getCurrentUsername() : "SYSTEM";
        auditService.createAuditLog(
                performedBy,
                Constants.AUDIT_INSERT,
                "PERFORMANCE_REVIEWS",
                savedReview.getReviewId().toString(),
                null,
                "Created performance review for employee " + dto.getEmployeeId() + " for year " + dto.getReviewYear(),
                null,
                null);

        return savedReview;
    }

    // =========================================================
    // SELF ASSESSMENT
    // =========================================================

    @Override
    public PerformanceReview submitSelfAssessment(Long reviewId, PerformanceReviewDTO dto, String employeeId) {

        PerformanceReview review = getReviewById(reviewId);

        if (!review.getEmployee().getEmployeeId().equals(employeeId)) {
            throw new UnauthorizedException("You can only submit assessment for your own review");
        }

        if (review.getStatus() != ReviewStatus.PENDING_SELF_ASSESSMENT) {
            throw new ValidationException("Can only submit self-assessment in PENDING_SELF_ASSESSMENT status");
        }

        if (dto.getSelfAssessmentText() != null && dto.getSelfAssessmentText().length() < 100) {
            throw new ValidationException("Self-assessment must be at least 100 characters");
        }
        if (dto.getAchievements() != null && dto.getAchievements().length() < 50) {
            throw new ValidationException("Achievements must be at least 50 characters");
        }
        if (dto.getImprovementAreas() != null && dto.getImprovementAreas().length() < 50) {
            throw new ValidationException("Improvement areas must be at least 50 characters");
        }

        review.setAchievements(dto.getAchievements());
        review.setImprovementAreas(dto.getImprovementAreas());
        review.setSelfAssessmentRating(dto.getSelfAssessmentRating());
        review.setSelfAssessmentComments(dto.getSelfAssessmentComments());
        review.setSubmittedDate(LocalDate.now());
        review.setStatus(ReviewStatus.PENDING_MANAGER_REVIEW);
        review.setUpdatedAt(LocalDateTime.now());

        // 🔔 Notify Manager
        Employee manager = review.getEmployee().getManager();

        if (manager != null) {
            notificationService.createNotification(
                    manager,
                    NotificationType.PERFORMANCE,
                    "Performance Review Submitted",
                    review.getEmployee().getFullName() +
                            " has submitted self-assessment for year " + review.getReviewYear(),
                    NotificationPriority.HIGH);
        }

        PerformanceReview savedReview = reviewRepository.save(review);

        String performedBy = SecurityUtils.getCurrentUsername() != null ? SecurityUtils.getCurrentUsername() : "SYSTEM";
        auditService.createAuditLog(
                performedBy,
                Constants.AUDIT_UPDATE,
                "PERFORMANCE_REVIEWS",
                reviewId.toString(),
                ReviewStatus.PENDING_SELF_ASSESSMENT.name(),
                ReviewStatus.PENDING_MANAGER_REVIEW.name(),
                null,
                null);

        return savedReview;
    }

    // =========================================================
    // MANAGER REVIEW
    // =========================================================

    @Override
    @Transactional
    public PerformanceReview submitManagerReview(Long reviewId, PerformanceReviewDTO dto, String managerId) {

        PerformanceReview review = getReviewById(reviewId);

        Employee manager = employeeRepository.findById(managerId)
                .orElseThrow(() -> new ResourceNotFoundException("Manager", "employeeId", managerId));

        if (review.getEmployee().getManager() == null ||
                !review.getEmployee().getManager().getEmployeeId().equals(managerId)) {
            throw new UnauthorizedException("You can only review your direct reports");
        }

        if (review.getStatus() != ReviewStatus.PENDING_MANAGER_REVIEW) {
            throw new ValidationException("Can only review a review in PENDING_MANAGER_REVIEW status");
        }

        validateRatings(dto);

        if (dto.getManagerFeedback() != null && dto.getManagerFeedback().length() < 50) {
            throw new ValidationException("Manager feedback must be at least 50 characters");
        }

        review.setManagerFeedback(dto.getManagerFeedback());
        review.setTechnicalSkills(dto.getTechnicalSkills());
        review.setCommunication(dto.getCommunication());
        review.setTeamwork(dto.getTeamwork());
        review.setLeadership(dto.getLeadership());
        review.setPunctuality(dto.getPunctuality());
        review.setManagerRating(dto.getManagerRating());
        review.setManagerComments(dto.getManagerComments());
        review.setReviewedBy(manager);
        review.setReviewedDate(LocalDate.now());

        // Calculate Overall Rating (Average of technical, communication, teamwork,
        // leadership, punctuality)
        BigDecimal overallRating = dto.getTechnicalSkills()
                .add(dto.getCommunication())
                .add(dto.getTeamwork())
                .add(dto.getLeadership())
                .add(dto.getPunctuality())
                .divide(BigDecimal.valueOf(5), 1, java.math.RoundingMode.HALF_UP);

        review.setFinalRating(overallRating);
        review.setStatus(ReviewStatus.COMPLETED);
        review.setUpdatedAt(LocalDateTime.now());

        // 🔔 Notify Employee
        notificationService.createNotification(
                review.getEmployee(),
                NotificationType.PERFORMANCE,
                "Performance Review Completed",
                "Your performance review for year " + review.getReviewYear() +
                        " has been completed by your manager.",
                NotificationPriority.HIGH);

        PerformanceReview savedReview = reviewRepository.save(review);

        String performedBy = SecurityUtils.getCurrentUsername() != null ? SecurityUtils.getCurrentUsername() : "SYSTEM";
        auditService.createAuditLog(
                performedBy,
                Constants.AUDIT_UPDATE,
                "PERFORMANCE_REVIEWS",
                reviewId.toString(),
                ReviewStatus.PENDING_MANAGER_REVIEW.name(),
                ReviewStatus.COMPLETED.name(),
                null,
                null);

        return savedReview;
    }

    // =========================================================
    // COMPLETE REVIEW
    // =========================================================

    @Override
    public PerformanceReview completeReview(Long reviewId) {

        PerformanceReview review = getReviewById(reviewId);

        if (review.getStatus() == ReviewStatus.COMPLETED) {
            throw new ValidationException("Review is already completed");
        }

        review.setStatus(ReviewStatus.COMPLETED);
        review.setUpdatedAt(LocalDateTime.now());

        return reviewRepository.save(review);
    }

    // =========================================================
    // FETCH METHODS
    // =========================================================

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public PerformanceReview getReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("PerformanceReview", "id", reviewId));
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public PerformanceReviewDTO getReviewDTOById(Long reviewId) {
        PerformanceReview review = getReviewById(reviewId);
        return convertToDTO(review);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<PerformanceReviewDTO> getEmployeeReviews(String employeeId) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "employeeId", employeeId));

        return reviewRepository.findByEmployeeOrderByReviewYearDesc(employee).stream()
                .map(this::convertToDTO)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<PerformanceReviewDTO> getPendingReviewsForManager(String managerId) {
        return reviewRepository.findPendingReviewsByManagerId(managerId, ReviewStatus.PENDING_MANAGER_REVIEW).stream()
                .map(this::convertToDTO)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PerformanceReviewDTO> getTeamReviews(String managerId) {
        return reviewRepository.findTeamReviewsByManagerId(managerId).stream()
                .map(this::convertToDTO)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PerformanceReviewDTO> getReviewsByStatus(ReviewStatus status) {
        return reviewRepository.findByStatus(status).stream()
                .map(this::convertToDTO)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PerformanceReview getEmployeeReviewByYear(String employeeId, Integer year) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "employeeId", employeeId));

        return reviewRepository.findByEmployeeAndReviewYear(employee, year)
                .orElseThrow(() -> new ResourceNotFoundException("PerformanceReview",
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

        String performedBy = SecurityUtils.getCurrentUsername() != null ? SecurityUtils.getCurrentUsername() : "SYSTEM";
        auditService.createAuditLog(
                performedBy,
                Constants.AUDIT_DELETE,
                "PERFORMANCE_REVIEWS",
                reviewId.toString(),
                null,
                "Deleted performance review for " + review.getEmployee().getEmployeeId(),
                null,
                null);
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

        List<PerformanceReviewDTO> reviews = getEmployeeReviews(employeeId);

        BigDecimal sum = BigDecimal.ZERO;
        int count = 0;

        for (PerformanceReviewDTO review : reviews) {
            if (review.getOverallRating() != null) {
                sum = sum.add(review.getOverallRating());
                count++;
            }
        }

        if (count == 0)
            return null;

        return sum.divide(BigDecimal.valueOf(count), 1, java.math.RoundingMode.HALF_UP);
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

        // Populate Dept/Desig
        if (review.getEmployee().getDepartment() != null) {
            dto.setDepartmentName(review.getEmployee().getDepartment().getDepartmentName());
        }
        if (review.getEmployee().getDesignation() != null) {
            dto.setDesignationName(review.getEmployee().getDesignation().getDesignationName());
        }

        dto.setAchievements(review.getAchievements());
        dto.setImprovementAreas(review.getImprovementAreas());
        dto.setSelfAssessmentRating(review.getSelfAssessmentRating());
        dto.setSelfAssessmentComments(review.getSelfAssessmentComments());
        dto.setManagerFeedback(review.getManagerFeedback());
        dto.setTechnicalSkills(review.getTechnicalSkills());
        dto.setCommunication(review.getCommunication());
        dto.setTeamwork(review.getTeamwork());
        dto.setLeadership(review.getLeadership());
        dto.setPunctuality(review.getPunctuality());
        dto.setManagerRating(review.getManagerRating());
        dto.setManagerComments(review.getManagerComments());
        dto.setOverallRating(review.getFinalRating());
        dto.setSubmittedDate(review.getSubmittedDate());
        dto.setReviewedDate(review.getReviewedDate());

        if (review.getReviewedBy() != null) {
            dto.setReviewedByName(review.getReviewedBy().getFullName());
            dto.setManagerId(review.getReviewedBy().getEmployeeId());
            dto.setManagerName(review.getReviewedBy().getFullName());
        }

        return dto;
    }

    @Override
    public PerformanceReview updateDraft(Long reviewId, PerformanceReviewDTO dto, String employeeId) {

        PerformanceReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("PerformanceReview", "id", reviewId));

        if (!review.getEmployee().getEmployeeId().equals(employeeId)) {
            throw new UnauthorizedException("You can only edit your own review");
        }

        if (review.getStatus() != ReviewStatus.PENDING_SELF_ASSESSMENT) {
            throw new ValidationException("Can only edit review in PENDING_SELF_ASSESSMENT status");
        }

        review.setAchievements(dto.getAchievements());
        review.setImprovementAreas(dto.getImprovementAreas());
        review.setSelfAssessmentRating(dto.getSelfAssessmentRating());
        review.setSelfAssessmentComments(dto.getSelfAssessmentComments());
        review.setUpdatedAt(LocalDateTime.now());

        return reviewRepository.save(review);
    }

    @Override
    public void validateRatings(PerformanceReviewDTO dto) {
        validateRating(dto.getTechnicalSkills(), "Technical Skills");
        validateRating(dto.getCommunication(), "Communication");
        validateRating(dto.getTeamwork(), "Teamwork");
        validateRating(dto.getLeadership(), "Leadership");
        validateRating(dto.getPunctuality(), "Punctuality");
    }

    private void validateRating(BigDecimal rating, String fieldName) {
        if (rating == null || rating.compareTo(BigDecimal.valueOf(1.0)) < 0
                || rating.compareTo(BigDecimal.valueOf(5.0)) > 0) {
            throw new ValidationException(fieldName + " rating must be between 1.0 and 5.0");
        }
    }
}