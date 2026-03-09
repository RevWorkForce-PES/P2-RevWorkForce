package com.revature.revworkforce.dto;

import jakarta.validation.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class LeaveBalanceDTOTest {

    private LeaveBalanceDTO dto;
    private Validator validator;

    @BeforeEach
    void setUp() {
        dto = new LeaveBalanceDTO();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testSettersAndGetters() {
        dto.setLeaveType("CL");
        dto.setLeaveTypeName("Casual Leave");
        dto.setTotalAllocated(12);
        dto.setUsedLeaves(5);
        dto.setRemainingBalance(7);
        dto.setYear(2026);

        assertEquals("CL", dto.getLeaveType());
        assertEquals("Casual Leave", dto.getLeaveTypeName());
        assertEquals(12, dto.getTotalAllocated());
        assertEquals(5, dto.getUsedLeaves());
        assertEquals(7, dto.getRemainingBalance());
        assertEquals(2026, dto.getYear());
    }

    @Test
    void testValidationConstraints() {
        // Empty DTO should fail @NotBlank and @NotNull
        LeaveBalanceDTO emptyDto = new LeaveBalanceDTO();
        Set<ConstraintViolation<LeaveBalanceDTO>> violations = validator.validate(emptyDto);

        assertFalse(violations.isEmpty(), "Empty DTO should have validation errors");

        // Check for specific fields
        boolean hasLeaveTypeError = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("leaveType"));
        boolean hasTotalAllocatedError = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("totalAllocated"));
        boolean hasUsedLeavesError = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("usedLeaves"));
        boolean hasRemainingBalanceError = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("remainingBalance"));
        boolean hasYearError = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("year"));

        assertTrue(hasLeaveTypeError);
        assertTrue(hasTotalAllocatedError);
        assertTrue(hasUsedLeavesError);
        assertTrue(hasRemainingBalanceError);
        assertTrue(hasYearError);
    }

    @Test
    void testValidDTOPassesValidation() {
        dto.setLeaveType("SL");
        dto.setLeaveTypeName("Sick Leave");
        dto.setTotalAllocated(10);
        dto.setUsedLeaves(2);
        dto.setRemainingBalance(8);
        dto.setYear(2026);

        Set<ConstraintViolation<LeaveBalanceDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "Valid DTO should have no validation errors");
    }
}