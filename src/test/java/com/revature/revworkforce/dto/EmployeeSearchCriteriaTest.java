package com.revature.revworkforce.dto;

import com.revature.revworkforce.enums.EmployeeStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeSearchCriteriaTest {

    @Test
    void testGettersAndSetters() {
        EmployeeSearchCriteria criteria = new EmployeeSearchCriteria();

        criteria.setKeyword("John");
        criteria.setDepartmentId(1L);
        criteria.setDesignationId(2L);
        criteria.setStatus(EmployeeStatus.ACTIVE);
        criteria.setManagerId("M123");

        assertEquals("John", criteria.getKeyword());
        assertEquals(1L, criteria.getDepartmentId());
        assertEquals(2L, criteria.getDesignationId());
        assertEquals(EmployeeStatus.ACTIVE, criteria.getStatus());
        assertEquals("M123", criteria.getManagerId());
    }

    @Test
    void testHasFilters_NoFilters() {
        EmployeeSearchCriteria criteria = new EmployeeSearchCriteria();
        assertFalse(criteria.hasFilters());
    }

    @Test
    void testHasFilters_WithKeyword() {
        EmployeeSearchCriteria criteria = new EmployeeSearchCriteria();
        criteria.setKeyword("Alice");
        assertTrue(criteria.hasFilters());
    }

    @Test
    void testHasFilters_WithDepartment() {
        EmployeeSearchCriteria criteria = new EmployeeSearchCriteria();
        criteria.setDepartmentId(10L);
        assertTrue(criteria.hasFilters());
    }

    @Test
    void testHasFilters_WithDesignation() {
        EmployeeSearchCriteria criteria = new EmployeeSearchCriteria();
        criteria.setDesignationId(5L);
        assertTrue(criteria.hasFilters());
    }

    @Test
    void testHasFilters_WithStatus() {
        EmployeeSearchCriteria criteria = new EmployeeSearchCriteria();
        criteria.setStatus(EmployeeStatus.INACTIVE);
        assertTrue(criteria.hasFilters());
    }

    @Test
    void testHasFilters_WithManagerId() {
        EmployeeSearchCriteria criteria = new EmployeeSearchCriteria();
        criteria.setManagerId("M456");
        assertTrue(criteria.hasFilters());
    }

    @Test
    void testHasFilters_KeywordAndManagerEmpty() {
        EmployeeSearchCriteria criteria = new EmployeeSearchCriteria();
        criteria.setKeyword("   ");
        criteria.setManagerId("");
        assertFalse(criteria.hasFilters());
    }
}