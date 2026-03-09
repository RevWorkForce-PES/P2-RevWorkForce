package com.revature.revworkforce.dto;

import com.revature.revworkforce.enums.LeaveStatus;
import jakarta.validation.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class LeaveApplicationDTOTest {

    private LeaveApplicationDTO dto;
    private Validator validator;

    @BeforeEach
    void setUp() {
        dto = new LeaveApplicationDTO();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testSettersAndGetters() {
        dto.setLeaveType("CL");
        dto.setStartDate(LocalDate.of(2026, 3, 10));
        dto.setEndDate(LocalDate.of(2026, 3, 12));
        dto.setReason("Personal work");
        dto.setTotalDays(3);
        dto.setStatus(LeaveStatus.PENDING);
        dto.setManagerComments("Approved soon");
        dto.setRejectionReason("Not enough leaves");

        assertEquals("CL", dto.getLeaveType());
        assertEquals(LocalDate.of(2026, 3, 10), dto.getStartDate());
        assertEquals(LocalDate.of(2026, 3, 12), dto.getEndDate());
        assertEquals("Personal work", dto.getReason());
        assertEquals(3, dto.getTotalDays());
        assertEquals(LeaveStatus.PENDING, dto.getStatus());
        assertEquals("Approved soon", dto.getManagerComments());
        assertEquals("Not enough leaves", dto.getRejectionReason());
    }

    @Test
    void testValidationConstraints() {
        // Empty DTO should fail @NotBlank and @NotNull
        LeaveApplicationDTO emptyDto = new LeaveApplicationDTO();
        Set<ConstraintViolation<LeaveApplicationDTO>> violations = validator.validate(emptyDto);

        assertFalse(violations.isEmpty(), "Empty DTO should have validation errors");

        // Check specific violation messages
        boolean hasLeaveTypeError = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("leaveType"));
        boolean hasStartDateError = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("startDate"));
        boolean hasEndDateError = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("endDate"));
        boolean hasReasonError = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("reason"));

        assertTrue(hasLeaveTypeError);
        assertTrue(hasStartDateError);
        assertTrue(hasEndDateError);
        assertTrue(hasReasonError);
    }

    @Test
    void testValidDTOPassesValidation() {
        dto.setLeaveType("CL");
        dto.setStartDate(LocalDate.of(2026, 3, 10));
        dto.setEndDate(LocalDate.of(2026, 3, 12));
        dto.setReason("Personal work");
        dto.setTotalDays(3);

        Set<ConstraintViolation<LeaveApplicationDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "Valid DTO should have no validation errors");
    }
}