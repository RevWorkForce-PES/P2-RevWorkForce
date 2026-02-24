package com.revature.revworkforce.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity class representing an Audit Log entry.
 * 
 * Maps to database table: AUDIT_LOGS
 * 
 * @author RevWorkForce Team
 */
@Entity
@Table(name = "AUDIT_LOGS")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "audit_seq")
    @SequenceGenerator(name = "audit_seq", sequenceName = "audit_seq", allocationSize = 1)
    @Column(name = "audit_id")
    private Long auditId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(name = "action", nullable = false, length = 50)
    private String action;

    @Column(name = "table_name", nullable = false, length = 50)
    private String tableName;

    @Column(name = "record_id", length = 50)
    private String recordId;

    @Column(name = "field_name", length = 100)
    private String fieldName;

    @Lob
    @Column(name = "old_value")
    private String oldValue;

    @Lob
    @Column(name = "new_value")
    private String newValue;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "user_agent", length = 500)
    private String userAgent;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Constructors
    public AuditLog() {
        this.createdAt = LocalDateTime.now();
    }

    public AuditLog(Employee employee, String action, String tableName, String description) {
        this();
        this.employee = employee;
        this.action = action;
        this.tableName = tableName;
        this.description = description;
    }

    public AuditLog(Employee employee, String action, String tableName, String recordId, String description) {
        this();
        this.employee = employee;
        this.action = action;
        this.tableName = tableName;
        this.recordId = recordId;
        this.description = description;
    }

    // Getters and Setters
    public Long getAuditId() {
        return auditId;
    }

    public void setAuditId(Long auditId) {
        this.auditId = auditId;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Lifecycle callbacks
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "AuditLog{" +
                "auditId=" + auditId +
                ", employeeId=" + (employee != null ? employee.getEmployeeId() : null) +
                ", action='" + action + '\'' +
                ", tableName='" + tableName + '\'' +
                ", recordId='" + recordId + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}