package com.revature.revworkforce.model;

import com.revature.revworkforce.enums.LeaveStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity representing a leave request raised by an employee.
 *
 * Database Table: LEAVE_APPLICATIONS
 *
 * This entity manages the complete leave workflow:
 * - Employee submits a leave request
 * - Manager reviews the request
 * - Leave is approved, rejected, or cancelled
 *
 * Relationships:
 * - Many-to-One with Employee (applicant)
 * - Many-to-One with LeaveType
 * - Many-to-One with Employee (approver/manager)
 *
 * Status Lifecycle:
 * PENDING → APPROVED
 * PENDING → REJECTED
 * PENDING → CANCELLED
 *
 * LeaveStatus enum is stored as STRING in the database
 * to avoid issues if enum order changes.
 *
 * Oracle Sequence Used: leave_app_seq
 *
 * @author RevWorkForce Team
 */
@Getter
@Setter
@ToString(exclude = {"employee", "leaveType", "approvedBy"})
@Entity
@Table(name = "LEAVE_APPLICATIONS")
public class LeaveApplication {

    /**
     * Primary key for leave application.
     * Auto-generated using Oracle sequence leave_app_seq.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "leave_app_seq")
    @SequenceGenerator(name = "leave_app_seq", sequenceName = "leave_app_seq", allocationSize = 1)
    @Column(name = "application_id")
    private Long applicationId;

    /**
     * Employee who submitted the leave request.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    /**
     * Type of leave being applied (CL, SL, PL, etc.).
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "leave_type_id", nullable = false)
    private LeaveType leaveType;

    /**
     * Leave start date (inclusive).
     */
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    /**
     * Leave end date (inclusive).
     */
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    /**
     * Total number of working days.
     */
    @Column(name = "total_days", nullable = false)
    private Integer totalDays;

    /**
     * Reason provided by employee.
     */
    @Column(name = "reason", nullable = false, length = 500)
    private String reason;

    /**
     * Current leave status.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private LeaveStatus status = LeaveStatus.PENDING;

    /**
     * Timestamp when leave was applied.
     */
    @Column(name = "applied_on")
    private LocalDateTime appliedOn;

    /**
     * Manager who approved or rejected the leave.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private Employee approvedBy;

    /**
     * Timestamp when leave was approved or rejected.
     */
    @Column(name = "approved_on")
    private LocalDateTime approvedOn;

    /**
     * Reason provided if leave is rejected.
     */
    @Column(name = "rejection_reason", length = 500)
    private String rejectionReason;

    /**
     * Additional comments from manager.
     */
    @Column(name = "comments", length = 500)
    private String comments;

    /**
     * Ensures appliedOn is set before persisting.
     */
    @PrePersist
    protected void onCreate() {
        if (appliedOn == null) {
            appliedOn = LocalDateTime.now();
        }
    }
}