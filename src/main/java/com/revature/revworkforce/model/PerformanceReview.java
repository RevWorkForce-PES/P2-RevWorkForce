package com.revature.revworkforce.model;

import com.revature.revworkforce.enums.ReviewStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity representing a performance review of an employee.
 *
 * Database Table: PERFORMANCE_REVIEWS
 *
 * This entity stores employee performance evaluations
 * conducted annually or periodically.
 *
 * Relationships:
 * - Many-to-One with Employee (reviewed employee)
 * - Many-to-One with Employee (reviewed by manager)
 *
 * Status Lifecycle:
 * DRAFT → SUBMITTED → REVIEWED → COMPLETED
 *
 * Business Rules:
 * - Each employee can have only one review per year.
 * - Ratings must be between 1.0 and 5.0.
 * - Final rating may be calculated based on self and manager ratings.
 *
 * Oracle Sequence Used: review_seq
 *
 * @author RevWorkForce Team
 */
@Getter
@Setter
@Entity
@Table(name = "PERFORMANCE_REVIEWS",
       uniqueConstraints = @UniqueConstraint(
           name = "uk_emp_review_year",
           columnNames = {"employee_id", "review_year"}
       ))
public class PerformanceReview {

    /**
     * Primary key for performance review.
     * Auto-generated using Oracle sequence review_seq.
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
     * Stored as NUMBER(2,1) in Oracle.
     */
    @Column(name = "self_assessment_rating", precision = 2, scale = 1)
    private BigDecimal selfAssessmentRating;

    /**
     * Manager rating (1.0 to 5.0).
     * Stored as NUMBER(2,1) in Oracle.
     */
    @Column(name = "manager_rating", precision = 2, scale = 1)
    private BigDecimal managerRating;

    /**
     * Final rating after evaluation.
     * Stored as NUMBER(2,1) in Oracle.
     */
    @Column(name = "final_rating", precision = 2, scale = 1)
    private BigDecimal finalRating;

    /**
     * Current status of the review.
     * Default value: DRAFT.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
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
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * Last updated timestamp.
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Automatically sets timestamps before insert.
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    /**
     * Updates modified timestamp before update.
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}