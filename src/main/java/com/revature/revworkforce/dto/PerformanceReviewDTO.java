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

    @NotBlank(message = "Self-assessment is required")
    @Size(min = 10, max = 2000, message = "Self-assessment must be at least 10 characters")
    private String selfAssessmentText;

    @NotBlank(message = "Achievements are required")
    @Size(min = 10, max = 2000, message = "Achievements must be at least 10 characters")
    private String achievements;

    @NotBlank(message = "Improvement areas are required")
    @Size(min = 10, max = 2000, message = "Improvement areas must be at least 10 characters")
    private String improvementAreas;

    @DecimalMin(value = "1.0", message = "Rating must be at least 1.0")
    @DecimalMax(value = "5.0", message = "Rating cannot exceed 5.0")
    private BigDecimal selfAssessmentRating;

    @Size(max = 1000, message = "Comments cannot exceed 1000 characters")
    private String selfAssessmentComments;

    private LocalDate submittedDate;

    // ========================================
    // MANAGER REVIEW FIELDS
    // ========================================

    @Size(min = 50, max = 2000, message = "Manager feedback must be at least 50 characters")
    private String managerFeedback;

    @DecimalMin(value = "1.0")
    @DecimalMax(value = "5.0")
    private BigDecimal technicalSkills;

    @DecimalMin(value = "1.0")
    @DecimalMax(value = "5.0")
    private BigDecimal communication;

    @DecimalMin(value = "1.0")
    @DecimalMax(value = "5.0")
    private BigDecimal teamwork;

    @DecimalMin(value = "1.0")
    @DecimalMax(value = "5.0")
    private BigDecimal leadership;

    @DecimalMin(value = "1.0")
    @DecimalMax(value = "5.0")
    private BigDecimal punctuality;

    @DecimalMin(value = "1.0", message = "Rating must be at least 1.0")
    @DecimalMax(value = "5.0", message = "Rating cannot exceed 5.0")
    private BigDecimal managerRating;

    @Size(max = 1000, message = "Manager comments cannot exceed 1000 characters")
    private String managerComments;

    private BigDecimal overallRating; // Renamed from finalRating to match requirements

    private String reviewedByName;

    private LocalDate reviewedDate;

    // Manager Info
    private String managerId;

    private String managerName;

    // Constructors
    public PerformanceReviewDTO() {
    }

    // Helper: returns 0-100 integer for progress bar widths
    private int toPercent(BigDecimal rating) {
        if (rating == null)
            return 0;
        return rating.multiply(BigDecimal.valueOf(20)).intValue();
    }

    public int getTechnicalSkillsPercent() {
        return toPercent(technicalSkills);
    }

    public int getCommunicationPercent() {
        return toPercent(communication);
    }

    public int getTeamworkPercent() {
        return toPercent(teamwork);
    }

    public int getLeadershipPercent() {
        return toPercent(leadership);
    }

    public int getPunctualityPercent() {
        return toPercent(punctuality);
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

    public String getSelfAssessmentText() {
        return selfAssessmentText;
    }

    public void setSelfAssessmentText(String selfAssessmentText) {
        this.selfAssessmentText = selfAssessmentText;
    }

    public String getAchievements() {
        return achievements;
    }

    public void setAchievements(String achievements) {
        this.achievements = achievements;
    }

    public String getImprovementAreas() {
        return improvementAreas;
    }

    public void setImprovementAreas(String improvementAreas) {
        this.improvementAreas = improvementAreas;
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

    public BigDecimal getTechnicalSkills() {
        return technicalSkills;
    }

    public void setTechnicalSkills(BigDecimal technicalSkills) {
        this.technicalSkills = technicalSkills;
    }

    public BigDecimal getCommunication() {
        return communication;
    }

    public void setCommunication(BigDecimal communication) {
        this.communication = communication;
    }

    public BigDecimal getTeamwork() {
        return teamwork;
    }

    public void setTeamwork(BigDecimal teamwork) {
        this.teamwork = teamwork;
    }

    public BigDecimal getLeadership() {
        return leadership;
    }

    public void setLeadership(BigDecimal leadership) {
        this.leadership = leadership;
    }

    public BigDecimal getPunctuality() {
        return punctuality;
    }

    public void setPunctuality(BigDecimal punctuality) {
        this.punctuality = punctuality;
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

    public BigDecimal getOverallRating() {
        return overallRating;
    }

    public void setOverallRating(BigDecimal overallRating) {
        this.overallRating = overallRating;
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
     * Get rating label based on overall rating.
     * 
     * @return rating label
     */
    public String getRatingLabel() {
        if (overallRating == null) {
            return "Not Rated";
        }

        if (overallRating.compareTo(BigDecimal.valueOf(4.5)) >= 0) {
            return "Exceptional";
        } else if (overallRating.compareTo(BigDecimal.valueOf(3.5)) >= 0) {
            return "Exceeds Expectations";
        } else if (overallRating.compareTo(BigDecimal.valueOf(2.5)) >= 0) {
            return "Meets Expectations";
        } else if (overallRating.compareTo(BigDecimal.valueOf(1.5)) >= 0) {
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
        return status == ReviewStatus.PENDING_SELF_ASSESSMENT;
    }

    /**
     * Check if employee can submit this review.
     * 
     * @return true if can submit
     */
    public boolean canSubmit() {
        return status == ReviewStatus.PENDING_SELF_ASSESSMENT;
    }

    /**
     * Check if manager can review this.
     * 
     * @return true if manager can review
     */
    public boolean canManagerReview() {
        return status == ReviewStatus.PENDING_MANAGER_REVIEW;
    }

    /**
     * Check if employee can view manager feedback.
     * 
     * @return true if can view feedback
     */
    public boolean canViewManagerFeedback() {
        return status == ReviewStatus.COMPLETED;
    }
}
