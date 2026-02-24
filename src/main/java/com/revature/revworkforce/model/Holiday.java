package com.revature.revworkforce.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity representing company holidays.
 *
 * Database Table: HOLIDAYS
 *
 * Oracle Sequence Used: holiday_seq
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "HOLIDAYS")
public class Holiday {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "holiday_seq")
    @SequenceGenerator(name = "holiday_seq", sequenceName = "holiday_seq", allocationSize = 1)
    @Column(name = "holiday_id")
    private Long holidayId;

    @Column(name = "holiday_date", nullable = false)
    private LocalDate holidayDate;

    @Column(name = "holiday_name", nullable = false, length = 100)
    private String holidayName;

    @Column(name = "holiday_type", length = 50)
    private String holidayType = "National";

    @Column(name = "is_optional", length = 1)
    private Character isOptional = 'N';

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by", length = 20)
    private String createdBy;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}