package com.revature.revworkforce.dto;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ReportDTOTest {

    @Test
    void testNoArgsConstructorAndSetters() {
        ReportDTO report = new ReportDTO();

        report.setTotalEmployees(100L);
        report.setActiveEmployees(80L);
        report.setInactiveEmployees(20L);
        report.setTotalDepartments(5L);
        report.setPendingLeaves(10L);
        report.setApprovedLeaves(15L);
        report.setTotalGoals(50L);
        report.setCompletedGoals(30L);

        Map<String, Long> empDist = new HashMap<>();
        empDist.put("IT", 40L);
        report.setEmployeeDistribution(empDist);

        Map<String, Long> leaveStats = new HashMap<>();
        leaveStats.put("CL", 5L);
        report.setLeaveStatistics(leaveStats);

        Map<String, Object> deptStats = new HashMap<>();
        deptStats.put("AverageEmployees", 20);
        report.setDepartmentStatistics(deptStats);

        Map<String, Object> perfStats = new HashMap<>();
        perfStats.put("AvgRating", 4.2);
        report.setPerformanceStatistics(perfStats);

        Map<String, Object> goalStats = new HashMap<>();
        goalStats.put("CompletedPercentage", 60);
        report.setGoalStatistics(goalStats);

        Map<String, Object> attendanceStats = new HashMap<>();
        attendanceStats.put("AbsentDays", 5);
        report.setAttendanceStatistics(attendanceStats);

        Map<String, Double> rates = new HashMap<>();
        rates.put("AttritionRate", 0.05);
        report.setCalculatedRates(rates);

        Map<String, Object> filters = new HashMap<>();
        filters.put("Year", 2026);
        report.setFilters(filters);

        assertEquals(100L, report.getTotalEmployees());
        assertEquals(80L, report.getActiveEmployees());
        assertEquals(20L, report.getInactiveEmployees());
        assertEquals(5L, report.getTotalDepartments());
        assertEquals(10L, report.getPendingLeaves());
        assertEquals(15L, report.getApprovedLeaves());
        assertEquals(50L, report.getTotalGoals());
        assertEquals(30L, report.getCompletedGoals());
        assertEquals(empDist, report.getEmployeeDistribution());
        assertEquals(leaveStats, report.getLeaveStatistics());
        assertEquals(deptStats, report.getDepartmentStatistics());
        assertEquals(perfStats, report.getPerformanceStatistics());
        assertEquals(goalStats, report.getGoalStatistics());
        assertEquals(attendanceStats, report.getAttendanceStatistics());
        assertEquals(rates, report.getCalculatedRates());
        assertEquals(filters, report.getFilters());
    }

    @Test
    void testAllArgsConstructorAndBuilder() {
        Map<String, Long> empDist = Map.of("IT", 40L);
        Map<String, Long> leaveStats = Map.of("CL", 5L);
        Map<String, Object> deptStats = Map.of("AvgEmployees", 20);
        Map<String, Object> perfStats = Map.of("AvgRating", 4.2);
        Map<String, Object> goalStats = Map.of("CompletedPercentage", 60);
        Map<String, Object> attendanceStats = Map.of("AbsentDays", 5);
        Map<String, Double> rates = Map.of("AttritionRate", 0.05);
        Map<String, Object> filters = Map.of("Year", 2026);

        ReportDTO report = ReportDTO.builder()
                .totalEmployees(100L)
                .activeEmployees(80L)
                .inactiveEmployees(20L)
                .totalDepartments(5L)
                .pendingLeaves(10L)
                .approvedLeaves(15L)
                .totalGoals(50L)
                .completedGoals(30L)
                .employeeDistribution(empDist)
                .leaveStatistics(leaveStats)
                .departmentStatistics(deptStats)
                .performanceStatistics(perfStats)
                .goalStatistics(goalStats)
                .attendanceStatistics(attendanceStats)
                .calculatedRates(rates)
                .filters(filters)
                .build();

        assertEquals(100L, report.getTotalEmployees());
        assertEquals(empDist, report.getEmployeeDistribution());
        assertEquals(rates, report.getCalculatedRates());
        assertEquals(filters, report.getFilters());
    }
}