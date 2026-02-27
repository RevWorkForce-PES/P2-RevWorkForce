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
 * LeaveStatus enum is stored as STRING in the database.
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
     * Type of leave being applied.
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
     * Total number of leave days.
     */
    @Column(name = "total_days", nullable = false)
    private Integer totalDays;

    /**
     * Reason for leave.
     */
    @Column(name = "reason", nullable = false, length = 500)
    private String reason;

    /**
     * Current leave status.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private LeaveStatus status = LeaveStatus.PENDING;

    /**
     * Timestamp when leave was applied.
     */
    @Column(name = "applied_on", updatable = false, nullable = false)
    private LocalDateTime appliedOn;

    /**
     * Manager who approved/rejected leave.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private Employee approvedBy;

    /**
     * Timestamp when leave was approved/rejected.
     */
    @Column(name = "approved_on")
    private LocalDateTime approvedOn;

    /**
     * Rejection reason if leave is rejected.
     */
    @Column(name = "rejection_reason", length = 500)
    private String rejectionReason;

    /**
     * Additional manager comments.
     */
    @Column(name = "comments", length = 500)
    private String comments;

    /**
     * Default constructor required by JPA.
     * Initializes default status and timestamp.
     */
    public LeaveApplication() {
        this.status = LeaveStatus.PENDING;
        this.appliedOn = LocalDateTime.now();
    }

    /**
     * Constructor used when employee applies for leave.
     */
    public LeaveApplication(Employee employee,
                            LeaveType leaveType,
                            LocalDate startDate,
                            LocalDate endDate,
                            Integer totalDays,
                            String reason) {

        this();
        this.employee = employee;
        this.leaveType = leaveType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalDays = totalDays;
        this.reason = reason;
    }

    /**
     * Full constructor.
     */
    public LeaveApplication(Long applicationId,
                            Employee employee,
                            LeaveType leaveType,
                            LocalDate startDate,
                            LocalDate endDate,
                            Integer totalDays,
                            String reason,
                            LeaveStatus status,
                            LocalDateTime appliedOn,
                            Employee approvedBy,
                            LocalDateTime approvedOn,
                            String rejectionReason,
                            String comments) {

        this.applicationId = applicationId;
        this.employee = employee;
        this.leaveType = leaveType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalDays = totalDays;
        this.reason = reason;
        this.status = status;
        this.appliedOn = appliedOn;
        this.approvedBy = approvedBy;
        this.approvedOn = approvedOn;
        this.rejectionReason = rejectionReason;
        this.comments = comments;
    }

    /**
     * Automatically sets appliedOn before persisting.
     */
    @PrePersist
    protected void onCreate() {
        if (this.appliedOn == null) {
            this.appliedOn = LocalDateTime.now();
        }
        if (this.status == null) {
            this.status = LeaveStatus.PENDING;
        }
    }
}