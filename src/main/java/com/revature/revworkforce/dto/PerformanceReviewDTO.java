package com.revature.revworkforce.dto;

import com.revature.revworkforce.enums.ReviewStatus;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for Performance Review - P2 VERSION
 * Matches existing schema with BigDecimal
 * 
 * @author RevWorkForce Team
 */
public class PerformanceReviewDTO {
    
    private Long reviewId;
    
    private String employeeId;
    
    private String employeeName;
    
    private String departmentName;
    
    private String designationName;
    
    @NotNull(message = "Review year is required")
    @Min(value = 2020, message = "Review year must be 2020 or later")
    @Max(value = 2030, message = "Review year must be 2030 or earlier")
    private Integer reviewYear;
    
    private String reviewPeriod;
    
    private ReviewStatus status;
    
    // ========================================
    // EMPLOYEE SELF-ASSESSMENT FIELDS
    // ========================================
    
    @Size(max = 2000, message = "Key deliverables cannot exceed 2000 characters")
    private String keyDeliverables;
    
    @Size(max = 2000, message = "Major accomplishments cannot exceed 2000 characters")
    private String majorAccomplishments;
    
    @Size(max = 2000, message = "Areas of improvement cannot exceed 2000 characters")
    private String areasOfImprovement;
    
    @DecimalMin(value = "1.0", message = "Rating must be at least 1.0")
    @DecimalMax(value = "5.0", message = "Rating cannot exceed 5.0")
    private BigDecimal selfAssessmentRating;
    
    @Size(max = 1000, message = "Comments cannot exceed 1000 characters")
    private String selfAssessmentComments;
    
    private LocalDate submittedDate;
    
    // ========================================
    // MANAGER REVIEW FIELDS
    // ========================================
    
    @Size(max = 2000, message = "Manager feedback cannot exceed 2000 characters")
    private String managerFeedback;
    
    @DecimalMin(value = "1.0", message = "Rating must be at least 1.0")
    @DecimalMax(value = "5.0", message = "Rating cannot exceed 5.0")
    private BigDecimal managerRating;
    
    @Size(max = 1000, message = "Manager comments cannot exceed 1000 characters")
    private String managerComments;
    
    private BigDecimal finalRating;
    
    private String reviewedByName;
    
    private LocalDate reviewedDate;
    
    // Manager Info
    private String managerId;
    
    private String managerName;
    
    // Constructors
    public PerformanceReviewDTO() {
    }
    
    // Getters and Setters
    public Long getReviewId() {
        return reviewId;
    }
    
    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }
    
    public String getEmployeeId() {
        return employeeId;
    }
    
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }
    
    public String getEmployeeName() {
        return employeeName;
    }
    
    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }
    
    public String getDepartmentName() {
        return departmentName;
    }
    
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
    
    public String getDesignationName() {
        return designationName;
    }
    
    public void setDesignationName(String designationName) {
        this.designationName = designationName;
    }
    
    public Integer getReviewYear() {
        return reviewYear;
    }
    
    public void setReviewYear(Integer reviewYear) {
        this.reviewYear = reviewYear;
    }
    
    public String getReviewPeriod() {
        return reviewPeriod;
    }
    
    public void setReviewPeriod(String reviewPeriod) {
        this.reviewPeriod = reviewPeriod;
    }
    
    public ReviewStatus getStatus() {
        return status;
    }
    
    public void setStatus(ReviewStatus status) {
        this.status = status;
    }
    
    public String getKeyDeliverables() {
        return keyDeliverables;
    }
    
    public void setKeyDeliverables(String keyDeliverables) {
        this.keyDeliverables = keyDeliverables;
    }
    
    public String getMajorAccomplishments() {
        return majorAccomplishments;
    }
    
    public void setMajorAccomplishments(String majorAccomplishments) {
        this.majorAccomplishments = majorAccomplishments;
    }
    
    public String getAreasOfImprovement() {
        return areasOfImprovement;
    }
    
    public void setAreasOfImprovement(String areasOfImprovement) {
        this.areasOfImprovement = areasOfImprovement;
    }
    
    public BigDecimal getSelfAssessmentRating() {
        return selfAssessmentRating;
    }
    
    public void setSelfAssessmentRating(BigDecimal selfAssessmentRating) {
        this.selfAssessmentRating = selfAssessmentRating;
    }
    
    public String getSelfAssessmentComments() {
        return selfAssessmentComments;
    }
    
    public void setSelfAssessmentComments(String selfAssessmentComments) {
        this.selfAssessmentComments = selfAssessmentComments;
    }
    
    public LocalDate getSubmittedDate() {
        return submittedDate;
    }
    
    public void setSubmittedDate(LocalDate submittedDate) {
        this.submittedDate = submittedDate;
    }
    
    public String getManagerFeedback() {
        return managerFeedback;
    }
    
    public void setManagerFeedback(String managerFeedback) {
        this.managerFeedback = managerFeedback;
    }
    
    public BigDecimal getManagerRating() {
        return managerRating;
    }
    
    public void setManagerRating(BigDecimal managerRating) {
        this.managerRating = managerRating;
    }
    
    public String getManagerComments() {
        return managerComments;
    }
    
    public void setManagerComments(String managerComments) {
        this.managerComments = managerComments;
    }
    
    public BigDecimal getFinalRating() {
        return finalRating;
    }
    
    public void setFinalRating(BigDecimal finalRating) {
        this.finalRating = finalRating;
    }
    
    public String getReviewedByName() {
        return reviewedByName;
    }
    
    public void setReviewedByName(String reviewedByName) {
        this.reviewedByName = reviewedByName;
    }
    
    public LocalDate getReviewedDate() {
        return reviewedDate;
    }
    
    public void setReviewedDate(LocalDate reviewedDate) {
        this.reviewedDate = reviewedDate;
    }
    
    public String getManagerId() {
        return managerId;
    }
    
    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }
    
    public String getManagerName() {
        return managerName;
    }
    
    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }
    
    /**
     * Get rating label based on final rating.
     * 
     * @return rating label
     */
    public String getRatingLabel() {
        if (finalRating == null) {
            return "Not Rated";
        }
        
        if (finalRating.compareTo(BigDecimal.valueOf(4.5)) >= 0) {
            return "Exceptional";
        } else if (finalRating.compareTo(BigDecimal.valueOf(3.5)) >= 0) {
            return "Exceeds Expectations";
        } else if (finalRating.compareTo(BigDecimal.valueOf(2.5)) >= 0) {
            return "Meets Expectations";
        } else if (finalRating.compareTo(BigDecimal.valueOf(1.5)) >= 0) {
            return "Needs Improvement";
        } else {
            return "Unsatisfactory";
        }
    }
    
    /**
     * Check if employee can edit this review.
     * 
     * @return true if editable
     */
    public boolean isEditable() {
        return status == ReviewStatus.DRAFT;
    }
    
    /**
     * Check if employee can submit this review.
     * 
     * @return true if can submit
     */
    public boolean canSubmit() {
        return status == ReviewStatus.DRAFT;
    }
    
    /**
     * Check if manager can review this.
     * 
     * @return true if manager can review
     */
    public boolean canManagerReview() {
        return status == ReviewStatus.SUBMITTED;
    }
    
    /**
     * Check if employee can view manager feedback.
     * 
     * @return true if can view feedback
     */
    public boolean canViewManagerFeedback() {
        return status == ReviewStatus.REVIEWED || status == ReviewStatus.COMPLETED;
    }
}
