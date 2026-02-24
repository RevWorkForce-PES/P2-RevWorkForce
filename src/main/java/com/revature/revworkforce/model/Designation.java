package com.revature.revworkforce.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "DESIGNATIONS")
public class Designation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "desig_seq_gen")
    @SequenceGenerator(name = "desig_seq_gen", sequenceName = "desig_seq", allocationSize = 1)
    @Column(name = "designation_id")
    private Long designationId;

    @Column(name = "designation_name", nullable = false, unique = true)
    private String designationName;

    @Column(name = "designation_level", length = 20)
    private String designationLevel;

    @Column(name = "min_salary")
    private Double minSalary;

    @Column(name = "max_salary")
    private Double maxSalary;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "is_active", length = 1)
    private String isActive;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}