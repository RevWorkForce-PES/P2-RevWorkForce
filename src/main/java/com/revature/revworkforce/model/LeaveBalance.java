package com.revature.revworkforce.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * Entity representing yearly leave balance for an employee.
 *
 * Database Table: LEAVE_BALANCES
 *
 * This entity tracks leave allocation and usage per employee per year.
 * Each employee can have only one balance record per leave type per year.
 *
 * Relationships:
 * - Many-to-One with Employee
 * - Many-to-One with LeaveType
 *
 * Unique Constraint:
 * Combination of employee_id, leave_type_id, and year must be unique.
 *
 * Business Logic:
 * balance = totalAllocated + carriedForward - used
 *
 * Oracle Sequence Used: leave_bal_seq
 *
 * @author RevWorkForce Team
 */
@Getter
@Setter
@ToString(exclude = {"employee", "leaveType"})
@Entity
@Table(
    name = "LEAVE_BALANCES",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_emp_leave_year",
        columnNames = {"employee_id", "leave_type_id", "year"}
    )
)
public class LeaveBalance {

    /**
     * Primary key for leave balance.
     * Auto-generated using Oracle sequence leave_bal_seq.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "leave_bal_seq")
    @SequenceGenerator(name = "leave_bal_seq", sequenceName = "leave_bal_seq", allocationSize = 1)
    @Column(name = "balance_id")
    private Long balanceId;

    /**
     * Employee to whom this leave balance belongs.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    /**
     * Leave type (CL, SL, PL, etc.).
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "leave_type_id", nullable = false)
    private LeaveType leaveType;

    /**
     * Calendar year for which this balance applies.
     */
    @Column(name = "year", nullable = false)
    private Integer year;

    /**
     * Total leave days allocated for the year.
     */
    @Column(name = "total_allocated")
    private Integer totalAllocated = 0;

    /**
     * Leave days already used.
     */
    @Column(name = "used")
    private Integer used = 0;

    /**
     * Remaining leave balance.
     */
    @Column(name = "balance")
    private Integer balance = 0;

    /**
     * Leave days carried forward from previous year.
     */
    @Column(name = "carried_forward")
    private Integer carriedForward = 0;

    /**
     * Record creation timestamp.
     */
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * Last update timestamp.
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