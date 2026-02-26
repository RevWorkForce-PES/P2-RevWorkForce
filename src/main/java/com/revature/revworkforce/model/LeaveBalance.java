package com.revature.revworkforce.model;

import jakarta.persistence.*;
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
@ToString(exclude = { "employee", "leaveType" })
@Entity
@Table(name = "LEAVE_BALANCES", uniqueConstraints = @UniqueConstraint(name = "uk_emp_leave_year", columnNames = {
        "employee_id", "leave_type_id", "year" }))
public class LeaveBalance {

    /**
     * Primary key for leave balance.
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
    @Column(name = "total_allocated", nullable = false)
    private Integer totalAllocated = 0;

    /**
     * Leave days already used.
     */
    @Column(name = "used", nullable = false)
    private Integer used = 0;

    /**
     * Remaining leave balance.
     */
    @Column(name = "balance", nullable = false)
    private Integer balance = 0;

    /**
     * Leave days carried forward from previous year.
     */
    @Column(name = "carried_forward", nullable = false)
    private Integer carriedForward = 0;

    /**
     * Record creation timestamp.
     */
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    /**
     * Last update timestamp.
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Default constructor required by JPA.
     * Initializes timestamps and default numeric values.
     */
    public LeaveBalance() {
        this.totalAllocated = 0;
        this.used = 0;
        this.carriedForward = 0;
        this.balance = 0;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Constructor used when allocating yearly leave.
     */
    public LeaveBalance(Employee employee,
            LeaveType leaveType,
            Integer year,
            Integer totalAllocated,
            Integer carriedForward) {

        this();
        this.employee = employee;
        this.leaveType = leaveType;
        this.year = year;
        this.totalAllocated = totalAllocated != null ? totalAllocated : 0;
        this.carriedForward = carriedForward != null ? carriedForward : 0;
        this.used = 0;
        this.balance = this.totalAllocated + this.carriedForward;
    }

    /**
     * Full constructor.
     */
    public LeaveBalance(Long balanceId,
            Employee employee,
            LeaveType leaveType,
            Integer year,
            Integer totalAllocated,
            Integer used,
            Integer balance,
            Integer carriedForward,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {

        this.balanceId = balanceId;
        this.employee = employee;
        this.leaveType = leaveType;
        this.year = year;
        this.totalAllocated = totalAllocated;
        this.used = used;
        this.balance = balance;
        this.carriedForward = carriedForward;
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

    // Getters and Setters
    public Long getBalanceId() {
        return balanceId;
    }

    public void setBalanceId(Long balanceId) {
        this.balanceId = balanceId;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public LeaveType getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(LeaveType leaveType) {
        this.leaveType = leaveType;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getTotalAllocated() {
        return totalAllocated;
    }

    public void setTotalAllocated(Integer totalAllocated) {
        this.totalAllocated = totalAllocated;
    }

    public Integer getUsed() {
        return used;
    }

    public void setUsed(Integer used) {
        this.used = used;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public Integer getCarriedForward() {
        return carriedForward;
    }

    public void setCarriedForward(Integer carriedForward) {
        this.carriedForward = carriedForward;
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
}