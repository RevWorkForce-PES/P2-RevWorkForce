package com.revature.revworkforce.service;

import com.revature.revworkforce.dto.ReportDTO;
import com.revature.revworkforce.enums.*;
import com.revature.revworkforce.repository.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        when(employeeRepository.count()).thenReturn(10L);
        when(employeeRepository.countByStatus(EmployeeStatus.ACTIVE)).thenReturn(8L);
        when(departmentRepository.count()).thenReturn(3L);
    }

    // -----------------------------
    // Dashboard Statistics
    // -----------------------------
    @Test
    void getDashboardStatistics_ShouldReturnStats() {

        when(leaveApplicationRepository.countByStatus(LeaveStatus.PENDING)).thenReturn(2L);
        when(leaveApplicationRepository.countByStatus(LeaveStatus.APPROVED)).thenReturn(5L);
        when(goalRepository.count()).thenReturn(6L);

        List<Object[]> goalData = new ArrayList<>();
        goalData.add(new Object[]{GoalStatus.COMPLETED, 3L});

        when(goalRepository.countGoalsByStatus()).thenReturn(goalData);

        ReportDTO report = reportService.getDashboardStatistics();

        assertEquals(10L, report.getTotalEmployees());
        assertEquals(8L, report.getActiveEmployees());
        assertEquals(3L, report.getTotalDepartments());
        assertEquals(6L, report.getTotalGoals());
    }

    
    
}