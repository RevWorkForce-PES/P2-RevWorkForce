package com.revature.revworkforce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * DTO for transferring generated Reports and Analytics data.
 * Aggregates counts, percentages, and metrics.
 * 
 * @author RevWorkForce Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {

    // Overall counts
    private Long totalEmployees;
    private Long activeEmployees;
    private Long inactiveEmployees;
    private Long totalDepartments;
    private Long pendingLeaves;
    private Long approvedLeaves;
    private Long totalGoals;
    private Long completedGoals;

    // Distributions and Statistics
    private Map<String, Long> employeeDistribution;
    private Map<String, Long> leaveStatistics;
    private Map<String, Object> departmentStatistics;
    private Map<String, Object> performanceStatistics;
    private Map<String, Object> goalStatistics;
    private Map<String, Object> attendanceStatistics;

    // KPI Data Points
    private Map<String, Double> calculatedRates;

    // Filters applied
    private Map<String, Object> filters;
}