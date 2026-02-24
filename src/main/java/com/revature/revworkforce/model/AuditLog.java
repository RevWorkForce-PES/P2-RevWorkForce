package com.revature.revworkforce.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * Entity representing audit trail logs.
 *
 * Database Table: AUDIT_LOG
 *
 * Used for:
 * - Tracking user activity
 * - Recording data changes
 * - Security auditing
 *
 * Oracle Sequence Used: audit_seq
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "AUDIT_LOG")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "audit_seq")
    @SequenceGenerator(name = "audit_seq", sequenceName = "audit_seq", allocationSize = 1)
    @Column(name = "audit_id")
    private Long auditId;

    /**
     * Employee performing the action.
     */
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

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}