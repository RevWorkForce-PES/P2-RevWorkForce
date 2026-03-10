package com.revature.revworkforce.service;

import com.revature.revworkforce.dto.ReportDTO;
import com.revature.revworkforce.enums.*;
import com.revature.revworkforce.model.*;
import com.revature.revworkforce.repository.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private LeaveApplicationRepository leaveApplicationRepository;

    @Mock
    private PerformanceReviewRepository performanceReviewRepository;

    @Mock
    private GoalRepository goalRepository;

    @Mock
    private HolidayRepository holidayRepository;

    @InjectMocks
    private ReportService reportService;

    @BeforeEach
    void setup() {
        // Basic setup for tests that need it can override these.
    }

    // -----------------------------
    // Dashboard Statistics
    // -----------------------------
    @Test
    void getDashboardStatistics_ShouldReturnStats() {
        when(employeeRepository.count()).thenReturn(10L);
        when(employeeRepository.countByStatus(EmployeeStatus.ACTIVE)).thenReturn(8L);
        when(departmentRepository.count()).thenReturn(3L);
        when(leaveApplicationRepository.countByStatus(LeaveStatus.PENDING)).thenReturn(2L);
        when(leaveApplicationRepository.countByStatus(LeaveStatus.APPROVED)).thenReturn(5L);
        when(goalRepository.count()).thenReturn(6L);

        List<Object[]> goalData = new ArrayList<>();
        goalData.add(new Object[] { GoalStatus.COMPLETED, 3L });
        when(goalRepository.countGoalsByStatus()).thenReturn(goalData);

        ReportDTO report = reportService.getDashboardStatistics();

        assertEquals(10L, report.getTotalEmployees());
        assertEquals(8L, report.getActiveEmployees());
        assertEquals(2L, report.getInactiveEmployees()); // 10 - 8
        assertEquals(3L, report.getTotalDepartments());
        assertEquals(2L, report.getPendingLeaves());
        assertEquals(5L, report.getApprovedLeaves());
        assertEquals(6L, report.getTotalGoals());
        assertEquals(3L, report.getCompletedGoals());
    }

    @Test
    void getDashboardStatistics_EmptyGoals_CalculatesCorrectly() {
        when(employeeRepository.count()).thenReturn(0L);
        when(employeeRepository.countByStatus(EmployeeStatus.ACTIVE)).thenReturn(0L);
        when(departmentRepository.count()).thenReturn(0L);
        when(leaveApplicationRepository.countByStatus(any())).thenReturn(0L);
        when(goalRepository.count()).thenReturn(0L);
        when(goalRepository.countGoalsByStatus()).thenReturn(new ArrayList<>());

        ReportDTO report = reportService.getDashboardStatistics();
        assertEquals(0L, report.getCompletedGoals());
    }

    // -----------------------------
    // Employee Report
    // -----------------------------
    @Test
    void getEmployeeReport_ReturnsCorrectAggregations() {
        List<Object[]> deptDist = List.<Object[]>of(new Object[] { "Engineering", 5L }, new Object[] { "HR", 2L });
        List<Object[]> statDist = List.<Object[]>of(new Object[] { EmployeeStatus.ACTIVE, 6L },
                new Object[] { EmployeeStatus.INACTIVE, 1L });
        List<Object[]> desigDist = List.<Object[]>of(new Object[] { "Dev", 4L });
        List<Object[]> genderDist = List.<Object[]>of(new Object[] { Gender.M, 3L });

        when(employeeRepository.countEmployeesByDepartment()).thenReturn(deptDist);
        when(employeeRepository.countEmployeesByStatus()).thenReturn(statDist);
        when(employeeRepository.countEmployeesByDesignation()).thenReturn(desigDist);
        when(employeeRepository.countEmployeesByGender()).thenReturn(genderDist);
        when(employeeRepository.getAverageTenure()).thenReturn(4.551);

        ReportDTO report = reportService.getEmployeeReport();

        assertEquals("Employee Distribution", report.getFilters().get("reportType"));
        assertEquals(5L, report.getEmployeeDistribution().get("Engineering"));

        Map<String, Object> stats = report.getDepartmentStatistics();
        assertEquals(4.55, stats.get("averageTenureYears"));

        Map<String, Long> byStatus = (Map<String, Long>) stats.get("byStatus");
        assertEquals(6L, byStatus.get(EmployeeStatus.ACTIVE.toString()));
    }

    // -----------------------------
    // Leave Report
    // -----------------------------
    @Test
    void getLeaveReport_ReturnsDetailedLeaveStats() {
        int year = 2024;

        List<Object[]> monthDist = List.<Object[]>of(new Object[] { 1, 10L }); // Jan
        List<Object[]> typeDist = List.<Object[]>of(new Object[] { "Annual Leave", 5L });
        List<Object[]> statusDist = List.<Object[]>of(new Object[] { LeaveStatus.APPROVED, 15L });

        when(leaveApplicationRepository.countLeavesByStatus(year)).thenReturn(statusDist);
        when(leaveApplicationRepository.countLeavesByType(year)).thenReturn(typeDist);
        when(leaveApplicationRepository.countLeavesByMonth(year)).thenReturn(monthDist);
        when(leaveApplicationRepository.sumTotalLeaveDaysByStatus(year, LeaveStatus.APPROVED)).thenReturn(50L);
        when(employeeRepository.countByStatus(EmployeeStatus.ACTIVE)).thenReturn(10L);

        ReportDTO report = reportService.getLeaveReport(year);

        assertEquals("Leave Statistics", report.getFilters().get("reportType"));
        assertEquals(year, report.getFilters().get("year"));

        Map<String, Object> stats = report.getDepartmentStatistics();
        assertEquals(50L, stats.get("totalLeaveDaysTaken"));
        assertEquals(5.0, stats.get("averageLeaveDaysPerEmployee")); // 50 / 10

        Map<String, Long> byMonth = (Map<String, Long>) stats.get("byMonth");
        assertEquals(10L, byMonth.get("JANUARY"));
    }

    @Test
    void getLeaveReport_NullYearAndNoEmployees_HandlesGracefully() {
        when(leaveApplicationRepository.countLeavesByStatus(anyInt())).thenReturn(new ArrayList<>());
        when(leaveApplicationRepository.countLeavesByType(anyInt())).thenReturn(new ArrayList<>());
        when(leaveApplicationRepository.countLeavesByMonth(anyInt())).thenReturn(new ArrayList<>());
        when(leaveApplicationRepository.sumTotalLeaveDaysByStatus(anyInt(), any())).thenReturn(null); // null sum
        when(employeeRepository.countByStatus(EmployeeStatus.ACTIVE)).thenReturn(0L); // 0 employees

        ReportDTO report = reportService.getLeaveReport(null); // Should use current year

        Map<String, Object> stats = report.getDepartmentStatistics();
        assertEquals(0L, stats.get("totalLeaveDaysTaken"));
        assertEquals(0.0, stats.get("averageLeaveDaysPerEmployee")); // Division by zero handled
    }

    // -----------------------------
    // Department Report
    // -----------------------------
    @Test
    void getDepartmentReport_SpecificDepartment_CalculatesMetrics() {
        Long deptId = 1L;
        Department dept = new Department();
        dept.setDepartmentName("Engineering");
        Optional<Department> optDept = Optional.of(dept);

        Employee e1 = new Employee();
        e1.setSalary(BigDecimal.valueOf(50000));
        Employee e2 = new Employee();
        e2.setSalary(BigDecimal.valueOf(60000));

        when(departmentRepository.findById(deptId)).thenReturn(optDept);
        when(employeeRepository.countByDepartment(dept)).thenReturn(2L);
        when(employeeRepository.findByDepartment(dept)).thenReturn(List.of(e1, e2));

        Goal g1 = new Goal();
        g1.setStatus(GoalStatus.COMPLETED);
        Goal g2 = new Goal();
        g2.setStatus(GoalStatus.IN_PROGRESS);
        // Total goals: 2. Completed: 1 => 50%
        when(goalRepository.findByEmployeeOrderByCreatedAtDesc(e1)).thenReturn(List.of(g1, g2));
        when(goalRepository.findByEmployeeOrderByCreatedAtDesc(e2)).thenReturn(new ArrayList<>());

        ReportDTO report = reportService.getDepartmentReport(deptId);

        Map<String, Object> stats = report.getDepartmentStatistics();
        assertEquals("Engineering", stats.get("departmentName"));
        assertEquals(2L, stats.get("employeeCount"));
        assertEquals(55000.0, stats.get("averageSalary")); // (50k + 60k) / 2
        assertEquals(50.0, stats.get("goalCompletionRate")); // 1/2 complete
    }

    @Test
    void getDepartmentReport_OverviewMode() {
        List<Object[]> deptDist = List.<Object[]>of(new Object[] { "Engineering", 5L });
        when(employeeRepository.countEmployeesByDepartment()).thenReturn(deptDist);

        ReportDTO report = reportService.getDepartmentReport(null);

        Map<String, Object> stats = report.getDepartmentStatistics();
        Map<String, Long> empCount = (Map<String, Long>) stats.get("employeeCountPerDepartment");
        assertEquals(5L, empCount.get("Engineering"));
    }

    // -----------------------------
    // Performance Report
    // -----------------------------
    @Test
    void getPerformanceReport_CalculatesAveragesAndDistributions() {
        int year = 2024;
        when(performanceReviewRepository.findByReviewYear(year))
                .thenReturn(Arrays.asList(new PerformanceReview(), new PerformanceReview())); // Total 2
        when(performanceReviewRepository.findByReviewYearAndStatus(year, ReviewStatus.COMPLETED))
                .thenReturn(List.of(new PerformanceReview())); // Completed 1
        when(performanceReviewRepository.getAverageRating(year)).thenReturn(4.256);

        List<Object[]> distribution = List.<Object[]>of(new Object[] { 4.0, 10L });
        when(performanceReviewRepository.countRatingDistribution(year)).thenReturn(distribution);

        ReportDTO report = reportService.getPerformanceReport(year);

        Map<String, Object> stats = report.getPerformanceStatistics();
        assertEquals(2L, stats.get("totalReviews"));
        assertEquals(1L, stats.get("completedReviews"));
        assertEquals(4.26, stats.get("averageRating")); // Rounded

        Map<String, Long> distMap = (Map<String, Long>) stats.get("ratingDistribution");
        assertEquals(10L, distMap.get("Band_4.0"));
    }

    // -----------------------------
    // Goal Report
    // -----------------------------
    @Test
    void getGoalReport_CalculatesCompletionAndAdvances() {
        when(goalRepository.count()).thenReturn(10L); // total
        List<Object[]> statusDist = List.<Object[]>of(new Object[] { GoalStatus.COMPLETED, 4L },
                new Object[] { GoalStatus.IN_PROGRESS, 6L });
        when(goalRepository.countGoalsByStatus()).thenReturn(statusDist);
        when(goalRepository.getAverageProgress()).thenReturn(55.555);
        when(goalRepository.countOverdueGoals(any(LocalDate.class))).thenReturn(2L);

        ReportDTO report = reportService.getGoalReport();

        Map<String, Object> stats = report.getGoalStatistics();
        assertEquals(10L, stats.get("totalGoals"));
        assertEquals(2L, stats.get("overdueGoals"));

        Map<String, Double> rates = report.getCalculatedRates();
        assertEquals(40.0, rates.get("completionRate")); // 4 / 10
        assertEquals(55.56, rates.get("averageProgress")); // Rounded
    }

    @Test
    void getGoalReport_ZeroGoals_NoDivisionByZero() {
        when(goalRepository.count()).thenReturn(0L); // 0 goals total
        when(goalRepository.countGoalsByStatus()).thenReturn(new ArrayList<>());
        when(goalRepository.getAverageProgress()).thenReturn(null); // Average returns null on empty

        ReportDTO report = reportService.getGoalReport();
        Map<String, Double> rates = report.getCalculatedRates();
        assertEquals(0.0, rates.get("completionRate"));
        assertEquals(0.0, rates.get("averageProgress"));
    }

    // -----------------------------
    // Attendance Report
    // -----------------------------
    @Test
    void getAttendanceReport_CalculatesWorkingDaysAndLeavesIntersecting() {
        // Mock a month (Jan 2024 = 31 days)
        // Assume 2 holidays, making working days = (31 - ~8 weekends) - 2 holidays = 21
        // working days (approx, logic handles exact).
        // For testing, let's just make sure DateUtil logic is invoked correctly by
        // returning specific holidays and leaves.
        int year = 2024;
        int month = 1;

        Holiday h1 = new Holiday();
        h1.setHolidayDate(LocalDate.of(2024, 1, 15));
        when(holidayRepository.findByDateRange(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(List.of(h1));

        when(employeeRepository.countByStatus(EmployeeStatus.ACTIVE)).thenReturn(5L);

        LeaveApplication leave = new LeaveApplication();
        leave.setStatus(LeaveStatus.APPROVED);
        leave.setStartDate(LocalDate.of(2024, 1, 10));
        leave.setEndDate(LocalDate.of(2024, 1, 12)); // 3 working days
        when(leaveApplicationRepository.findByDateRange(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(List.of(leave));

        ReportDTO report = reportService.getAttendanceReport(year, month);

        Map<String, Object> stats = report.getAttendanceStatistics();
        assertNotNull(stats.get("workingDaysInPeriod"));
        assertEquals(3L, stats.get("totalLeaveDaysTaken")); // 3 days extracted successfully

        Map<String, Double> rates = report.getCalculatedRates();
        assertNotNull(rates.get("attendanceRate"));
        assertTrue(rates.get("attendanceRate") > 0.0);
    }

    @Test
    void getAttendanceReport_FullYearMode_AndLeaveEdgeOverlaps() {
        int year = 2024; // If month null, tests full year

        when(holidayRepository.findByDateRange(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(new ArrayList<>()); // No holidays
        when(employeeRepository.countByStatus(EmployeeStatus.ACTIVE)).thenReturn(1L);

        // Leave overflowing start date (e.g. started previous year)
        LeaveApplication leave = new LeaveApplication();
        leave.setStatus(LeaveStatus.APPROVED);
        leave.setStartDate(LocalDate.of(2023, 12, 30));
        leave.setEndDate(LocalDate.of(2024, 1, 3)); // 3 working days inside bounds (Jan 1, 2, 3)
        when(leaveApplicationRepository.findByDateRange(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(List.of(leave));

        ReportDTO report = reportService.getAttendanceReport(year, null);

        Map<String, Object> stats = report.getAttendanceStatistics();
        assertNotNull(stats.get("totalLeaveDaysTaken"));
        assertEquals(3L, stats.get("totalLeaveDaysTaken")); // Caps at boundaries
    }
}