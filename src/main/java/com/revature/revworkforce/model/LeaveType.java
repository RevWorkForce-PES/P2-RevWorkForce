package com.revature.revworkforce.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "LEAVE_TYPES")
public class LeaveType {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "leave_type_seq_gen")
    @SequenceGenerator(name = "leave_type_seq_gen", sequenceName = "leave_type_seq", allocationSize = 1)
    @Column(name = "leave_type_id")
    private Long leaveTypeId;

    @Column(name = "leave_code", nullable = false, unique = true, length = 10)
    private String leaveCode;

    @Column(name = "leave_name", nullable = false, length = 50)
    private String leaveName;

    @Column(name = "default_days")
    private Integer defaultDays;

    @Column(name = "max_carry_forward")
    private Integer maxCarryForward;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "requires_approval", length = 1)
    private String requiresApproval;

    @Column(name = "is_active", length = 1)
    private String isActive;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}