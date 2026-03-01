package com.revature.revworkforce.dto;

import com.revature.revworkforce.enums.HolidayType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for Holiday.
 * Used for request/response between controller and service layer.
 */
public class HolidayDTO {

    private Long holidayId;

    @NotBlank(message = "Holiday name is required")
    @Size(max = 100, message = "Holiday name cannot exceed 100 characters")
    private String holidayName;

    @NotNull(message = "Holiday date is required")
    private LocalDate holidayDate;

    @NotNull(message = "Holiday type is required")
    private HolidayType type;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    private Boolean isActive;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ===============================
    // Custom Validations
    // ===============================

    public void validateDateRules() {
        if (holidayDate != null) {

            // No past date
            if (holidayDate.isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("Holiday date cannot be in the past");
            }

            // No weekend
            if (holidayDate.getDayOfWeek() == DayOfWeek.SATURDAY ||
                holidayDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                throw new IllegalArgumentException("Holiday cannot be on a weekend");
            }
        }
    }

    // ===============================
    // Getters & Setters
    // ===============================

    public Long getHolidayId() {
        return holidayId;
    }

    public void setHolidayId(Long holidayId) {
        this.holidayId = holidayId;
    }

    public String getHolidayName() {
        return holidayName;
    }

    public void setHolidayName(String holidayName) {
        this.holidayName = holidayName;
    }

    public LocalDate getHolidayDate() {
        return holidayDate;
    }

    public void setHolidayDate(LocalDate holidayDate) {
        this.holidayDate = holidayDate;
    }

    public HolidayType getType() {
        return type;
    }

    public void setType(HolidayType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
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

    // ===============================
    // Optional Builder
    // ===============================

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final HolidayDTO dto = new HolidayDTO();

        public Builder holidayId(Long id) {
            dto.setHolidayId(id);
            return this;
        }

        public Builder holidayName(String name) {
            dto.setHolidayName(name);
            return this;
        }

        public Builder holidayDate(LocalDate date) {
            dto.setHolidayDate(date);
            return this;
        }

        public Builder type(HolidayType type) {
            dto.setType(type);
            return this;
        }

        public Builder description(String description) {
            dto.setDescription(description);
            return this;
        }

        public Builder isActive(Boolean active) {
            dto.setIsActive(active);
            return this;
        }

        public HolidayDTO build() {
            dto.validateDateRules();
            return dto;
        }
    }
}