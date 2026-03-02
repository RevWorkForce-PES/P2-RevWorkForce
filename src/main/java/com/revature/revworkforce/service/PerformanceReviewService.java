package com.revature.revworkforce.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

/**
 * Performance Review Service - P2 VERSION
 * Works with existing BigDecimal schema and matches P2 requirements
 * 
 * @author RevWorkForce Team
 */
@Service
@Transactional
public class PerformanceReviewService {

    @Autowired
    private PerformanceReviewRepository reviewRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * Create new review (Employee or Manager creates)
     * P2 Requirement: Create performance review document
     */
    public PerformanceReview createReview(String employeeId, Integer reviewYear, String createdByManagerId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "employeeId", employeeId));

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

    /**
     * Employee fills self-assessment
     * P2 Requirement: key deliverables, accomplishments, areas of improvement, self-assessment rating
     */
    public PerformanceReview submitSelfAssessment(
            Long reviewId, 
            String employeeId,
            String keyDeliverables,
            String majorAccomplishments,
            String areasOfImprovement,
            BigDecimal selfAssessmentRating,
            String selfAssessmentComments) {
        
        PerformanceReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("PerformanceReview", "id", reviewId));

        if (!review.getEmployee().getEmployeeId().equals(employeeId)) {
            throw new UnauthorizedException("You can only submit assessment for your own review");
        }

        if (review.getStatus() != ReviewStatus.DRAFT) {
            throw new ValidationException("Can only edit review in DRAFT status");
        }

        // Validate self-assessment rating (1-5)
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
        review.setStatus(ReviewStatus.SUBMITTED);
        review.setUpdatedAt(LocalDateTime.now());

        return reviewRepository.save(review);
    }

    /**
     * Manager provides feedback and rating
     * P2 Requirement: Detailed feedback, rating (1-5), submit feedback
     */
    public PerformanceReview submitManagerReview(
            Long reviewId, 
            String managerId,
            String managerFeedback,
            BigDecimal managerRating,
            String managerComments) {
        
        PerformanceReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("PerformanceReview", "id", reviewId));

        Employee manager = employeeRepository.findById(managerId)
                .orElseThrow(() -> new ResourceNotFoundException("Manager", "employeeId", managerId));

        // Verify manager is the employee's manager
        if (review.getEmployee().getManager() == null || 
            !review.getEmployee().getManager().getEmployeeId().equals(managerId)) {
            throw new UnauthorizedException("You can only review your direct reports");
        }

        if (review.getStatus() != ReviewStatus.SUBMITTED) {
            throw new ValidationException("Can only review a SUBMITTED review");
        }

        // Validate manager rating (1-5)
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
        
        // Calculate final rating as average of self and manager ratings
        if (review.getSelfAssessmentRating() != null) {
            BigDecimal finalRating = review.getSelfAssessmentRating()
                .add(managerRating)
                .divide(BigDecimal.valueOf(2), 1, BigDecimal.ROUND_HALF_UP);
            review.setFinalRating(finalRating);
        } else {
            review.setFinalRating(managerRating);
        }
        
        review.setStatus(ReviewStatus.REVIEWED);
        review.setUpdatedAt(LocalDateTime.now());

        return reviewRepository.save(review);
    }

    /**
     * Complete/finalize review
     */
    public PerformanceReview completeReview(Long reviewId) {
        PerformanceReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("PerformanceReview", "id", reviewId));

        if (review.getStatus() != ReviewStatus.REVIEWED) {
            throw new ValidationException("Can only complete a REVIEWED review");
        }

        review.setStatus(ReviewStatus.COMPLETED);
        review.setUpdatedAt(LocalDateTime.now());

        return reviewRepository.save(review);
    }

    /**
     * Update review in DRAFT status (employee can edit)
     */
    public PerformanceReview updateDraft(
            Long reviewId,
            String employeeId,
            String keyDeliverables,
            String majorAccomplishments,
            String areasOfImprovement,
            BigDecimal selfAssessmentRating,
            String selfAssessmentComments) {
        
        PerformanceReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("PerformanceReview", "id", reviewId));

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

    /**
     * Get review by ID
     * P2 Requirement: View submitted reviews, View manager feedback
     */
    @Transactional(readOnly = true)
    public PerformanceReview getReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("PerformanceReview", "id", reviewId));
    }

    /**
     * Get all reviews for an employee
     * P2 Requirement: View submitted performance reviews with status
     */
    @Transactional(readOnly = true)
    public List<PerformanceReview> getEmployeeReviews(String employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "employeeId", employeeId));

        return reviewRepository.findByEmployeeOrderByReviewYearDesc(employee);
    }

    /**
     * Get pending reviews for manager (submitted by team)
     * P2 Requirement: View performance reviews submitted by direct reportees
     */
    @Transactional(readOnly = true)
    public List<PerformanceReview> getPendingReviewsForManager(String managerId) {
        return reviewRepository.findPendingReviewsByManagerId(managerId, ReviewStatus.SUBMITTED);
    }

    /**
     * Get all team reviews (manager view)
     * P2 Requirement: View team member reviews
     */
    @Transactional(readOnly = true)
    public List<PerformanceReview> getTeamReviews(String managerId) {
        return reviewRepository.findTeamReviewsByManagerId(managerId);
    }

    /**
     * Get reviews by status
     */
    @Transactional(readOnly = true)
    public List<PerformanceReview> getReviewsByStatus(ReviewStatus status) {
        return reviewRepository.findByStatus(status);
    }

    /**
     * Get review by employee and year
     */
    @Transactional(readOnly = true)
    public PerformanceReview getEmployeeReviewByYear(String employeeId, Integer year) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "employeeId", employeeId));

        return reviewRepository.findByEmployeeAndReviewYear(employee, year)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "PerformanceReview", "employeeId and year", employeeId + " - " + year));
    }

    /**
     * Delete review (admin only)
     */
    public void deleteReview(Long reviewId) {
        PerformanceReview review = getReviewById(reviewId);
        
        if (review.getStatus() == ReviewStatus.COMPLETED) {
            throw new ValidationException("Cannot delete completed review");
        }
        
        reviewRepository.delete(review);
    }

    /**
     * Check if review exists
     */
    @Transactional(readOnly = true)
    public boolean reviewExists(String employeeId, Integer year) {
        Employee employee = employeeRepository.findById(employeeId).orElse(null);
        return employee != null && reviewRepository.existsByEmployeeAndReviewYear(employee, year);
    }

    /**
     * Get average rating for employee
     */
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

        if (count == 0) {
            return null;
        }

        return sum.divide(BigDecimal.valueOf(count), 1, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Convert PerformanceReview entity to DTO
     */
    @Transactional(readOnly = true)
    public PerformanceReviewDTO convertToDTO(PerformanceReview review) {
        PerformanceReviewDTO dto = new PerformanceReviewDTO();
        
        dto.setReviewId(review.getReviewId());
        dto.setEmployeeId(review.getEmployee().getEmployeeId());
        dto.setEmployeeName(review.getEmployee().getFullName());
        
        if (review.getEmployee().getDepartment() != null) {
            dto.setDepartmentName(review.getEmployee().getDepartment().getDepartmentName());
        }
        
        if (review.getEmployee().getDesignation() != null) {
            dto.setDesignationName(review.getEmployee().getDesignation().getDesignationName());
        }
        
        dto.setReviewYear(review.getReviewYear());
        dto.setReviewPeriod(review.getReviewPeriod());
        dto.setStatus(review.getStatus());
        
        // Self-assessment fields
        dto.setKeyDeliverables(review.getKeyDeliverables());
        dto.setMajorAccomplishments(review.getMajorAccomplishments());
        dto.setAreasOfImprovement(review.getAreasOfImprovement());
        dto.setSelfAssessmentRating(review.getSelfAssessmentRating());
        dto.setSelfAssessmentComments(review.getSelfAssessmentComments());
        dto.setSubmittedDate(review.getSubmittedDate());
        
        // Manager review fields
        dto.setManagerFeedback(review.getManagerFeedback());
        dto.setManagerRating(review.getManagerRating());
        dto.setManagerComments(review.getManagerComments());
        dto.setFinalRating(review.getFinalRating());
        dto.setReviewedDate(review.getReviewedDate());
        
        if (review.getReviewedBy() != null) {
            dto.setReviewedByName(review.getReviewedBy().getFullName());
        }
        
        if (review.getEmployee().getManager() != null) {
            dto.setManagerId(review.getEmployee().getManager().getEmployeeId());
            dto.setManagerName(review.getEmployee().getManager().getFullName());
        }
        
        return dto;
    }
}
