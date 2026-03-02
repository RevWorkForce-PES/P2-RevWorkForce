package com.revature.revworkforce.service;

import com.revature.revworkforce.dto.ReportDTO;
import com.revature.revworkforce.enums.*;
import com.revature.revworkforce.model.Department;
import com.revature.revworkforce.model.Employee;
import com.revature.revworkforce.model.Holiday;
import com.revature.revworkforce.repository.*;
import com.revature.revworkforce.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for generating Reports & Analytics.
 * Handles calculation and aggregation of metrics across all modules.
 * 
 * @author RevWorkForce Team
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final LeaveApplicationRepository leaveApplicationRepository;
    private final PerformanceReviewRepository performanceReviewRepository;
    private final GoalRepository goalRepository;
    private final HolidayRepository holidayRepository;

    /**
     * Generates general Dashboard statistics.
     */
    public ReportDTO getDashboardStatistics() {
        ReportDTO report = new ReportDTO();

        report.setTotalEmployees(employeeRepository.count());
        report.setActiveEmployees(employeeRepository.countByStatus(EmployeeStatus.ACTIVE));
        report.setInactiveEmployees(employeeRepository.count() - report.getActiveEmployees());

        report.setTotalDepartments(departmentRepository.count());

        report.setPendingLeaves(leaveApplicationRepository.countByStatus(LeaveStatus.PENDING)); // Assuming we want
                                                                                                // overall pending count
        // For Dashboard, could just be total approved leaves this year, or overall
        report.setApprovedLeaves(leaveApplicationRepository.countByStatus(LeaveStatus.APPROVED));

        report.setTotalGoals(goalRepository.count());
        // For completed goals, could use count query but will aggregate from DB
        List<Object[]> statusCounts = goalRepository.countGoalsByStatus();
        long completedGoalsCount = 0;
        for (Object[] count : statusCounts) {
            if (GoalStatus.COMPLETED.equals(count[0])) {
                completedGoalsCount = ((Number) count[1]).longValue();
                break;
            }
        }
        report.setCompletedGoals(completedGoalsCount);

        return report;
    }

    /**
     * Generates Employee distributions report.
     */
    public ReportDTO getEmployeeReport() {
        ReportDTO report = new ReportDTO();

        Map<String, Object> filters = new HashMap<>();
        filters.put("reportType", "Employee Distribution");
        report.setFilters(filters);

        // Helper to convert Object[] to Map<String, Long>
        report.setEmployeeDistribution(mapAggregations(employeeRepository.countEmployeesByDepartment()));

        // Let's add more detailed stats
        Map<String, Object> empStats = new HashMap<>();
        empStats.put("byStatus", mapAggregations(employeeRepository.countEmployeesByStatus()));
        empStats.put("byDesignation", mapAggregations(employeeRepository.countEmployeesByDesignation()));
        empStats.put("byGender", mapAggregations(employeeRepository.countEmployeesByGender()));

        Double avgTenure = employeeRepository.getAverageTenure();
        empStats.put("averageTenureYears", avgTenure != null ? Math.round(avgTenure * 100.0) / 100.0 : 0.0);

        report.setPerformanceStatistics(empStats); // We'll put it here or better, add a dedicated field, or use
                                                   // departmentStatistics as general map
        report.setDepartmentStatistics(empStats); // re-purposing for employee stats detailed

        return report;
    }

    /**
     * Generates Leave statistics report.
     */
    public ReportDTO getLeaveReport(Integer year) {
        if (year == null)
            year = LocalDate.now().getYear();

        ReportDTO report = new ReportDTO();
        Map<String, Object> filters = new HashMap<>();
        filters.put("reportType", "Leave Statistics");
        filters.put("year", year);
        report.setFilters(filters);

        Map<String, Long> leaveStats = new HashMap<>();

        // Map distributions
        Map<String, Long> byStatus = mapAggregations(leaveApplicationRepository.countLeavesByStatus(year));
        Map<String, Long> byType = mapAggregations(leaveApplicationRepository.countLeavesByType(year));

        // Month needs special handling as it returns integers
        List<Object[]> monthData = leaveApplicationRepository.countLeavesByMonth(year);
        Map<String, Long> byMonth = new HashMap<>();
        for (Object[] row : monthData) {
            if (row[0] != null) {
                String monthName = java.time.Month.of(((Number) row[0]).intValue()).name();
                byMonth.put(monthName, ((Number) row[1]).longValue());
            }
        }

        Long totalDaysTaken = leaveApplicationRepository.sumTotalLeaveDaysByStatus(year, LeaveStatus.APPROVED);
        if (totalDaysTaken == null)
            totalDaysTaken = 0L;

        long totalActiveEmployees = employeeRepository.countByStatus(EmployeeStatus.ACTIVE);
        double avgLeaves = totalActiveEmployees > 0 ? (double) totalDaysTaken / totalActiveEmployees : 0.0;

        Map<String, Object> detailedStats = new HashMap<>();
        detailedStats.put("byStatus", byStatus);
        detailedStats.put("byType", byType);
        detailedStats.put("byMonth", byMonth);
        detailedStats.put("totalLeaveDaysTaken", totalDaysTaken);
        detailedStats.put("averageLeaveDaysPerEmployee", Math.round(avgLeaves * 10.0) / 10.0);

        report.setLeaveStatistics(leaveStats); // Empty, returning via Map
        report.setDepartmentStatistics(detailedStats);

        return report;
    }

    /**
     * Generates Department specific or overview report.
     */
    public ReportDTO getDepartmentReport(Long departmentId) {
        ReportDTO report = new ReportDTO();
        Map<String, Object> filters = new HashMap<>();
        filters.put("reportType", "Department Statistics");

        if (departmentId != null) {
            filters.put("departmentId", departmentId);
            Optional<Department> optDept = departmentRepository.findById(departmentId);
            if (optDept.isPresent()) {
                Department dept = optDept.get();
                long empCount = employeeRepository.countByDepartment(dept);

                // Average Salary calculation
                List<Employee> deptEmps = employeeRepository.findByDepartment(dept);
                double avgSalary = deptEmps.stream()
                        .filter(e -> e.getSalary() != null)
                        .mapToDouble(e -> e.getSalary().doubleValue())
                        .average()
                        .orElse(0.0);

                Map<String, Object> metrics = new HashMap<>();
                metrics.put("departmentName", dept.getDepartmentName());
                metrics.put("employeeCount", empCount);
                metrics.put("averageSalary", Math.round(avgSalary * 100.0) / 100.0);

                // Department goal completion rate
                // Fetch goals for department employees
                long totalDeptGoals = 0;
                long completedDeptGoals = 0;
                for (Employee e : deptEmps) {
                    List<com.revature.revworkforce.model.Goal> goals = goalRepository
                            .findByEmployeeOrderByCreatedAtDesc(e);
                    totalDeptGoals += goals.size();
                    completedDeptGoals += goals.stream().filter(g -> g.getStatus() == GoalStatus.COMPLETED).count();
                }
                double goalRate = totalDeptGoals > 0 ? ((double) completedDeptGoals / totalDeptGoals) * 100 : 0.0;
                metrics.put("goalCompletionRate", Math.round(goalRate * 10.0) / 10.0);

                report.setDepartmentStatistics(metrics);
            }
        } else {
            // Overview
            report.setDepartmentStatistics(new HashMap<>()); // Placeholder
            report.getDepartmentStatistics().put("employeeCountPerDepartment",
                    mapAggregations(employeeRepository.countEmployeesByDepartment()));
        }

        report.setFilters(filters);
        return report;
    }

    /**
     * Generates Performance distributions report.
     */
    public ReportDTO getPerformanceReport(Integer year) {
        if (year == null)
            year = LocalDate.now().getYear();

        ReportDTO report = new ReportDTO();
        Map<String, Object> filters = new HashMap<>();
        filters.put("reportType", "Performance Report");
        filters.put("year", year);
        report.setFilters(filters);

        long totalReviews = performanceReviewRepository.findByReviewYear(year).size();
        long completedReviews = performanceReviewRepository.findByReviewYearAndStatus(year, ReviewStatus.COMPLETED)
                .size();

        Double avgRating = performanceReviewRepository.getAverageRating(year);

        Map<String, Object> perfStats = new HashMap<>();
        perfStats.put("totalReviews", totalReviews);
        perfStats.put("completedReviews", completedReviews);
        perfStats.put("averageRating", avgRating != null ? Math.round(avgRating * 100.0) / 100.0 : 0.0);

        // Rating Distribution (Floor(rating)) => count
        Map<String, Long> distribution = new HashMap<>();
        List<Object[]> rawDist = performanceReviewRepository.countRatingDistribution(year);
        for (Object[] row : rawDist) {
            String band = String.valueOf(row[0]);
            distribution.put("Band_" + band, ((Number) row[1]).longValue());
        }
        perfStats.put("ratingDistribution", distribution);

        report.setPerformanceStatistics(perfStats);
        return report;
    }

    /**
     * Generates Goals report.
     */
    public ReportDTO getGoalReport() {
        ReportDTO report = new ReportDTO();

        Map<String, Object> filters = new HashMap<>();
        filters.put("reportType", "Goal Report");
        report.setFilters(filters);

        Map<String, Object> goalStats = new HashMap<>();
        long totalGoals = goalRepository.count();
        goalStats.put("totalGoals", totalGoals);

        Map<String, Long> byStatus = mapAggregations(goalRepository.countGoalsByStatus());
        goalStats.put("goalsByStatus", byStatus);

        long completedGoals = byStatus.getOrDefault(GoalStatus.COMPLETED.name(), 0L);
        double completionRate = totalGoals > 0 ? ((double) completedGoals / totalGoals) * 100 : 0.0;

        Double avgProgress = goalRepository.getAverageProgress();
        long overdueGoals = goalRepository.countOverdueGoals(LocalDate.now());

        Map<String, Double> rates = new HashMap<>();
        rates.put("completionRate", Math.round(completionRate * 100.0) / 100.0);
        rates.put("averageProgress", avgProgress != null ? Math.round(avgProgress * 100.0) / 100.0 : 0.0);
        report.setCalculatedRates(rates);

        goalStats.put("overdueGoals", overdueGoals);
        report.setGoalStatistics(goalStats);

        return report;
    }

    /**
     * Generates Attendance/Working Days report.
     */
    public ReportDTO getAttendanceReport(Integer year, Integer month) {
        if (year == null)
            year = LocalDate.now().getYear();

        LocalDate startDate;
        LocalDate endDate;
        if (month != null) {
            YearMonth ym = YearMonth.of(year, month);
            startDate = ym.atDay(1);
            endDate = ym.atEndOfMonth();
        } else {
            startDate = LocalDate.of(year, 1, 1);
            endDate = LocalDate.of(year, 12, 31);
        }

        if (endDate.isAfter(LocalDate.now())) {
            endDate = LocalDate.now(); // Calculate up to today
        }

        // Fetch Holidays
        List<Holiday> holidaysInPeriod = holidayRepository.findByDateRange(startDate, endDate);
        Set<LocalDate> holidayDates = holidaysInPeriod.stream()
                .map(Holiday::getHolidayDate)
                .collect(Collectors.toSet());

        // Calculate raw working days
        int workingDays = DateUtil.calculateWorkingDays(startDate, endDate, holidayDates);

        // Need total employees and their leaves
        long totalEmps = employeeRepository.countByStatus(EmployeeStatus.ACTIVE);

        // Get all approved leaves intersecting this period
        List<com.revature.revworkforce.model.LeaveApplication> allLeaves = leaveApplicationRepository
                .findByDateRange(startDate, endDate);
        long totalLeaveDaysTakenPeriod = 0;

        for (com.revature.revworkforce.model.LeaveApplication leave : allLeaves) {
            if (leave.getStatus() == LeaveStatus.APPROVED) {
                // Determine intersection with start/end
                LocalDate lStart = leave.getStartDate().isBefore(startDate) ? startDate : leave.getStartDate();
                LocalDate lEnd = leave.getEndDate().isAfter(endDate) ? endDate : leave.getEndDate();
                if (!lStart.isAfter(lEnd)) {
                    totalLeaveDaysTakenPeriod += DateUtil.calculateWorkingDays(lStart, lEnd, holidayDates);
                }
            }
        }

        long totalExpectedWorkDays = workingDays * totalEmps;
        double attendanceRate = totalExpectedWorkDays > 0
                ? ((double) (totalExpectedWorkDays - totalLeaveDaysTakenPeriod) / totalExpectedWorkDays) * 100
                : 0.0;

        ReportDTO report = new ReportDTO();
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalEmployees", totalEmps);
        stats.put("workingDaysInPeriod", workingDays);
        stats.put("totalLeaveDaysTaken", totalLeaveDaysTakenPeriod);
        report.setAttendanceStatistics(stats);

        Map<String, Double> rates = new HashMap<>();
        rates.put("attendanceRate", Math.round(attendanceRate * 100.0) / 100.0);
        report.setCalculatedRates(rates);

        Map<String, Object> filters = new HashMap<>();
        filters.put("reportType", "Attendance Report");
        filters.put("year", year);
        if (month != null)
            filters.put("month", month);
        report.setFilters(filters);

        return report;
    }

    /**
     * Helper to map List<Object[]> where Object[0]=Key, Object[1]=Count to
     * Map<String, Long>
     */
    private Map<String, Long> mapAggregations(List<Object[]> rawData) {
        Map<String, Long> mapped = new HashMap<>();
        for (Object[] row : rawData) {
            if (row[0] != null) {
                String key = String.valueOf(row[0]);
                Long count = ((Number) row[1]).longValue();
                mapped.put(key, count);
            }
        }
        return mapped;
    }
}
