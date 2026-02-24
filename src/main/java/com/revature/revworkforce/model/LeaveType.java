package com.revature.revworkforce.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity class representing a Leave Type (CL, SL, PL, etc.).
 * 
 * Maps to database table: LEAVE_TYPES
 * 
 * @author RevWorkForce Team
 */
@Entity
@Table(name = "LEAVE_TYPES")
public class LeaveType {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "leave_type_seq")
    @SequenceGenerator(name = "leave_type_seq", sequenceName = "leave_type_seq", allocationSize = 1)
    @Column(name = "leave_type_id")
    private Long leaveTypeId;

    @Column(name = "leave_code", nullable = false, unique = true, length = 10)
    private String leaveCode;

    @Column(name = "leave_name", nullable = false, length = 50)
    private String leaveName;

    @Column(name = "default_days", nullable = false)
    private Integer defaultDays;

    @Column(name = "max_carry_forward")
    private Integer maxCarryForward = 0;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "requires_approval", length = 1)
    private Character requiresApproval = 'Y';

    @Column(name = "is_active", length = 1)
    private Character isActive = 'Y';

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructors
    public LeaveType() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public LeaveType(String leaveCode, String leaveName, Integer defaultDays) {
        this();
        this.leaveCode = leaveCode;
        this.leaveName = leaveName;
        this.defaultDays = defaultDays;
    }

    // Getters and Setters
    public Long getLeaveTypeId() {
        return leaveTypeId;
    }

    public void setLeaveTypeId(Long leaveTypeId) {
        this.leaveTypeId = leaveTypeId;
    }

    public String getLeaveCode() {
        return leaveCode;
    }

    public void setLeaveCode(String leaveCode) {
        this.leaveCode = leaveCode;
    }

    public String getLeaveName() {
        return leaveName;
    }

    public void setLeaveName(String leaveName) {
        this.leaveName = leaveName;
    }

    public Integer getDefaultDays() {
        return defaultDays;
    }

    public void setDefaultDays(Integer defaultDays) {
        this.defaultDays = defaultDays;
    }

    public Integer getMaxCarryForward() {
        return maxCarryForward;
    }

    public void setMaxCarryForward(Integer maxCarryForward) {
        this.maxCarryForward = maxCarryForward;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Character getRequiresApproval() {
        return requiresApproval;
    }

    public void setRequiresApproval(Character requiresApproval) {
        this.requiresApproval = requiresApproval;
    }

    public Character getIsActive() {
        return isActive;
    }

    public void setIsActive(Character isActive) {
        this.isActive = isActive;
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
        return "LeaveType{" +
                "leaveTypeId=" + leaveTypeId +
                ", leaveCode='" + leaveCode + '\'' +
                ", leaveName='" + leaveName + '\'' +
                ", defaultDays=" + defaultDays +
                ", isActive=" + isActive +
                '}';
    }
}