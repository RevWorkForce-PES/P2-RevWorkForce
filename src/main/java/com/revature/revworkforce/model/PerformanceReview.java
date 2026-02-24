package com.revature.revworkforce.model;

import com.revature.revworkforce.enums.ReviewStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity representing a performance review of an employee.
 *
 * Database Table: PERFORMANCE_REVIEWS
 *
 * Relationships:
 * - Many-to-One with Employee (reviewed employee)
 * - Many-to-One with Employee (reviewed by manager)
 *
 * Unique Constraint:
 * Each employee can have only one review per year.
 *
 * Oracle Sequence Used: review_seq
 */
@Getter
@Setter
@ToString(exclude = {"employee", "reviewedBy"})
@Entity
@Table(name = "PERFORMANCE_REVIEWS",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_emp_review_year",
                columnNames = {"employee_id", "review_year"}
        ))
public class PerformanceReview {

    /**
     * Primary key for performance review.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "review_seq")
    @SequenceGenerator(name = "review_seq", sequenceName = "review_seq", allocationSize = 1)
    @Column(name = "review_id")
    private Long reviewId;

    /**
     * Employee whose performance is being reviewed.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    /**
     * Review year (e.g., 2026).
     */
    @Column(name = "review_year", nullable = false)
    private Integer reviewYear;

    /**
     * Review period (e.g., Q1-2026, Annual-2026).
     */
    @Column(name = "review_period", length = 50)
    private String reviewPeriod;

    /**
     * Employee self-assessment rating (1.0 to 5.0).
     */
    @Column(name = "self_assessment_rating", precision = 2, scale = 1)
    private BigDecimal selfAssessmentRating;

    /**
     * Manager rating (1.0 to 5.0).
     */
    @Column(name = "manager_rating", precision = 2, scale = 1)
    private BigDecimal managerRating;

    /**
     * Final rating after evaluation.
     */
    @Column(name = "final_rating", precision = 2, scale = 1)
    private BigDecimal finalRating;

    /**
     * Current status of the review.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ReviewStatus status = ReviewStatus.DRAFT;

    /**
     * Manager who reviewed the performance.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_by")
    private Employee reviewedBy;

    /**
     * Date when review was submitted.
     */
    @Column(name = "submitted_date")
    private LocalDate submittedDate;

    /**
     * Date when review was finalized.
     */
    @Column(name = "reviewed_date")
    private LocalDate reviewedDate;

    /**
     * Record creation timestamp.
     */
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    /**
     * Last updated timestamp.
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Default constructor required by JPA.
     * Initializes timestamps and default status.
     */
    public PerformanceReview() {
        this.status = ReviewStatus.DRAFT;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Constructor used when creating a new review.
     */
    public PerformanceReview(Employee employee,
                             Integer reviewYear,
                             String reviewPeriod) {

        this();
        this.employee = employee;
        this.reviewYear = reviewYear;
        this.reviewPeriod = reviewPeriod;
    }

    /**
     * Full constructor.
     */
    public PerformanceReview(Long reviewId,
                             Employee employee,
                             Integer reviewYear,
                             String reviewPeriod,
                             BigDecimal selfAssessmentRating,
                             BigDecimal managerRating,
                             BigDecimal finalRating,
                             ReviewStatus status,
                             Employee reviewedBy,
                             LocalDate submittedDate,
                             LocalDate reviewedDate,
                             LocalDateTime createdAt,
                             LocalDateTime updatedAt) {

        this.reviewId = reviewId;
        this.employee = employee;
        this.reviewYear = reviewYear;
        this.reviewPeriod = reviewPeriod;
        this.selfAssessmentRating = selfAssessmentRating;
        this.managerRating = managerRating;
        this.finalRating = finalRating;
        this.status = status;
        this.reviewedBy = reviewedBy;
        this.submittedDate = submittedDate;
        this.reviewedDate = reviewedDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * Automatically sets timestamps before insert.
     */
    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.updatedAt == null) {
            this.updatedAt = LocalDateTime.now();
        }
    }

    /**
     * Updates modified timestamp before update.
     */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}