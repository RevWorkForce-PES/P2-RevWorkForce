package com.revature.revworkforce.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DepartmentDTOTest {

    @Test
    void testGettersAndSetters() {
        DepartmentDTO dto = new DepartmentDTO();

        dto.setDepartmentId(123L);
        dto.setDepartmentName("Engineering");
        dto.setDepartmentHead("Alice");
        dto.setDescription("Handles product development");
        dto.setIsActive('N');
        dto.setEmployeeCount(50L);

        assertEquals(123L, dto.getDepartmentId());
        assertEquals("Engineering", dto.getDepartmentName());
        assertEquals("Alice", dto.getDepartmentHead());
        assertEquals("Handles product development", dto.getDescription());
        assertEquals('N', dto.getIsActive());
        assertEquals(50L, dto.getEmployeeCount());
    }

    @Test
    void testConstructorWithName() {
        DepartmentDTO dto = new DepartmentDTO("HR");

        assertEquals("HR", dto.getDepartmentName());
        assertNull(dto.getDepartmentId());
        assertNull(dto.getDepartmentHead());
        assertNull(dto.getDescription());
        assertEquals('Y', dto.getIsActive());
        assertEquals(0L, dto.getEmployeeCount());
    }
}