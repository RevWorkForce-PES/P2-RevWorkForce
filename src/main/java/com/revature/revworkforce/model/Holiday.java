package com.revature.revworkforce.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity class representing a Company Holiday.
 * 
 * Maps to database table: HOLIDAYS
 * 
 * @author RevWorkForce Team
 */
@Entity
@Table(name = "HOLIDAYS")
public class Holiday {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "holiday_seq")
    @SequenceGenerator(name = "holiday_seq", sequenceName = "holiday_seq", allocationSize = 1)
    @Column(name = "holiday_id")
    private Long holidayId;

    @Column(name = "holiday_date", nullable = false, unique = true)
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

    // Constructors
    public Holiday() {
        this.createdAt = LocalDateTime.now();
    }

    public Holiday(LocalDate holidayDate, String holidayName) {
        this();
        this.holidayDate = holidayDate;
        this.holidayName = holidayName;
    }

    public Holiday(LocalDate holidayDate, String holidayName, String holidayType) {
        this();
        this.holidayDate = holidayDate;
        this.holidayName = holidayName;
        this.holidayType = holidayType;
    }

    // Getters and Setters
    public Long getHolidayId() {
        return holidayId;
    }

    public void setHolidayId(Long holidayId) {
        this.holidayId = holidayId;
    }

    public LocalDate getHolidayDate() {
        return holidayDate;
    }

    public void setHolidayDate(LocalDate holidayDate) {
        this.holidayDate = holidayDate;
    }

    public String getHolidayName() {
        return holidayName;
    }

    public void setHolidayName(String holidayName) {
        this.holidayName = holidayName;
    }

    public String getHolidayType() {
        return holidayType;
    }

    public void setHolidayType(String holidayType) {
        this.holidayType = holidayType;
    }

    public Character getIsOptional() {
        return isOptional;
    }

    public void setIsOptional(Character isOptional) {
        this.isOptional = isOptional;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    // Lifecycle callbacks
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Holiday{" +
                "holidayId=" + holidayId +
                ", holidayDate=" + holidayDate +
                ", holidayName='" + holidayName + '\'' +
                ", holidayType='" + holidayType + '\'' +
                '}';
    }
}