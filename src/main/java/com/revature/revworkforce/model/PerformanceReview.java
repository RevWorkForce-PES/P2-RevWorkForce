package com.revature.revworkforce.model;

import com.revature.revworkforce.enums.ReviewStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity class representing a Performance Review.
 * 
 * Maps to database table: PERFORMANCE_REVIEWS
 * 
 * @author RevWorkForce Team
 */
@Entity
@Table(name = "PERFORMANCE_REVIEWS",
       uniqueConstraints = @UniqueConstraint(columnNames = {"employee_id", "review_year"}))
public class PerformanceReview {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "review_seq")
    @SequenceGenerator(name = "review_seq", sequenceName = "review_seq", allocationSize = 1)
    @Column(name = "review_id")
    private Long reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "review_year", nullable = false)
    private Integer reviewYear;

    @Column(name = "review_period", length = 50)
    private String reviewPeriod;

    @Lob
    @Column(name = "key_deliverables")
    private String keyDeliverables;

    @Lob
    @Column(name = "major_accomplishments")
    private String majorAccomplishments;

    @Lob
    @Column(name = "areas_of_improvement")
    private String areasOfImprovement;

    @Column(name = "self_assessment_rating", precision = 2, scale = 1)
    private BigDecimal selfAssessmentRating;

    @Column(name = "self_assessment_comments", length = 1000)
    private String selfAssessmentComments;

    @Lob
    @Column(name = "manager_feedback")
    private String managerFeedback;

    @Column(name = "manager_rating", precision = 2, scale = 1)
    private BigDecimal managerRating;

    @Column(name = "manager_comments", length = 1000)
    private String managerComments;

    @Column(name = "final_rating", precision = 2, scale = 1)
    private BigDecimal finalRating;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private ReviewStatus status = ReviewStatus.DRAFT;

    @Column(name = "submitted_date")
    private LocalDate submittedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_by")
    private Employee reviewedBy;

    @Column(name = "reviewed_date")
    private LocalDate reviewedDate;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructors
    public PerformanceReview() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public PerformanceReview(Employee employee, Integer reviewYear) {
        this();
        this.employee = employee;
        this.reviewYear = reviewYear;
    }

    // Getters and Setters
    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
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

    public ReviewStatus getStatus() {
        return status;
    }

    public void setStatus(ReviewStatus status) {
        this.status = status;
    }

    public LocalDate getSubmittedDate() {
        return submittedDate;
    }

    public void setSubmittedDate(LocalDate submittedDate) {
        this.submittedDate = submittedDate;
    }

    public Employee getReviewedBy() {
        return reviewedBy;
    }

    public void setReviewedBy(Employee reviewedBy) {
        this.reviewedBy = reviewedBy;
    }

    public LocalDate getReviewedDate() {
        return reviewedDate;
    }

    public void setReviewedDate(LocalDate reviewedDate) {
        this.reviewedDate = reviewedDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Lifecycle callbacks
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "PerformanceReview{" +
                "reviewId=" + reviewId +
                ", employeeId=" + (employee != null ? employee.getEmployeeId() : null) +
                ", reviewYear=" + reviewYear +
                ", status=" + status +
                ", finalRating=" + finalRating +
                '}';
    }
}