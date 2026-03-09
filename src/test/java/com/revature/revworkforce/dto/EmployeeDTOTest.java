package com.revature.revworkforce.dto;

import com.revature.revworkforce.enums.EmployeeStatus;
import com.revature.revworkforce.enums.Gender;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeDTOTest {

    @Test
    void testGettersAndSetters() {
        EmployeeDTO dto = new EmployeeDTO();

        dto.setEmployeeId("EMP001");
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setEmail("john.doe@example.com");
        dto.setPhone("9876543210");
        dto.setDateOfBirth(LocalDate.of(1990, 5, 10));
       dto.setAddress("123 Main St");
        dto.setCity("Mumbai");
        dto.setState("Maharashtra");
        dto.setPostalCode("400001");
        dto.setCountry("India");
        dto.setEmergencyContactName("Jane Doe");
        dto.setEmergencyContactPhone("9876543211");
        dto.setDepartmentId(10L);
        dto.setDepartmentName("IT");
        dto.setDesignationId(5L);
        dto.setDesignationName("Software Engineer");
        dto.setManagerId("MGR001");
        dto.setManagerName("Manager One");
        dto.setJoiningDate(LocalDate.of(2023, 1, 1));
        dto.setLeavingDate(LocalDate.of(2025, 12, 31));
        dto.setSalary(new BigDecimal("75000.50"));
        dto.setStatus(EmployeeStatus.ACTIVE);
        dto.setPassword("secret123");
        dto.setRoleIds(Set.of(1L, 2L));
        dto.setRoleNames(Set.of("EMPLOYEE", "ADMIN"));

        // Assertions
        assertEquals("EMP001", dto.getEmployeeId());
        assertEquals("John", dto.getFirstName());
        assertEquals("Doe", dto.getLastName());
        assertEquals("John Doe", dto.getFullName());
        assertEquals("john.doe@example.com", dto.getEmail());
        assertEquals("9876543210", dto.getPhone());
        assertEquals(LocalDate.of(1990, 5, 10), dto.getDateOfBirth());
         assertEquals("123 Main St", dto.getAddress());
        assertEquals("Mumbai", dto.getCity());
        assertEquals("Maharashtra", dto.getState());
        assertEquals("400001", dto.getPostalCode());
        assertEquals("India", dto.getCountry());
        assertEquals("Jane Doe", dto.getEmergencyContactName());
        assertEquals("9876543211", dto.getEmergencyContactPhone());
        assertEquals(10L, dto.getDepartmentId());
        assertEquals("IT", dto.getDepartmentName());
        assertEquals(5L, dto.getDesignationId());
        assertEquals("Software Engineer", dto.getDesignationName());
        assertEquals("MGR001", dto.getManagerId());
        assertEquals("Manager One", dto.getManagerName());
        assertEquals(LocalDate.of(2023, 1, 1), dto.getJoiningDate());
        assertEquals(LocalDate.of(2025, 12, 31), dto.getLeavingDate());
        assertEquals(new BigDecimal("75000.50"), dto.getSalary());
        assertEquals(EmployeeStatus.ACTIVE, dto.getStatus());
        assertEquals("secret123", dto.getPassword());
        assertEquals(Set.of(1L, 2L), dto.getRoleIds());
        assertEquals(Set.of("EMPLOYEE", "ADMIN"), dto.getRoleNames());
    }
}