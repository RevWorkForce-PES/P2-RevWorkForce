package com.revature.revworkforce.dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class DesignationDTOTest {

    @Test
    void testGettersAndSetters() {
        DesignationDTO dto = new DesignationDTO();

        dto.setDesignationId(101L);
        dto.setDesignationName("Software Engineer");
        dto.setDesignationLevel("L2");
        dto.setMinSalary(new BigDecimal("50000"));
        dto.setMaxSalary(new BigDecimal("120000"));
        dto.setDescription("Develops software");
        dto.setIsActive('N');
        dto.setEmployeeCount(10L);

        assertEquals(101L, dto.getDesignationId());
        assertEquals("Software Engineer", dto.getDesignationName());
        assertEquals("L2", dto.getDesignationLevel());
        assertEquals(new BigDecimal("50000"), dto.getMinSalary());
        assertEquals(new BigDecimal("120000"), dto.getMaxSalary());
        assertEquals("Develops software", dto.getDescription());
        assertEquals('N', dto.getIsActive());
        assertEquals(10L, dto.getEmployeeCount());
    }

    @Test
    void testConstructorWithName() {
        DesignationDTO dto = new DesignationDTO("QA Engineer");

        assertEquals("QA Engineer", dto.getDesignationName());
        assertNull(dto.getDesignationId());
        assertNull(dto.getDesignationLevel());
        assertNull(dto.getMinSalary());
        assertNull(dto.getMaxSalary());
        assertNull(dto.getDescription());
        assertEquals('Y', dto.getIsActive());
        assertEquals(0L, dto.getEmployeeCount());
    }
}