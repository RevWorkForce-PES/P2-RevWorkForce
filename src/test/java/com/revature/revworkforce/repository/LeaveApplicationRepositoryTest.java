package com.revature.revworkforce.repository;

import com.revature.revworkforce.enums.LeaveStatus;
import com.revature.revworkforce.model.Employee;
import com.revature.revworkforce.model.LeaveApplication;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LeaveApplicationRepositoryTest {

    @Mock
    private LeaveApplicationRepository leaveApplicationRepository;

    // ------------------------------------------------
    // FIND BY STATUS
    // ------------------------------------------------

    @Test
    void findByStatus_ReturnsList() {

        // Arrange
        LeaveApplication leave = new LeaveApplication();
        leave.setStatus(LeaveStatus.PENDING);

        when(leaveApplicationRepository.findByStatus(LeaveStatus.PENDING))
                .thenReturn(List.of(leave));

        // Act
        List<LeaveApplication> result =
                leaveApplicationRepository.findByStatus(LeaveStatus.PENDING);

        // Assert
        assertThat(result).isNotEmpty();
    }

    // ------------------------------------------------
    // COUNT BY STATUS
    // ------------------------------------------------

    @Test
    void countByStatus_ReturnsCount() {

        // Arrange
        when(leaveApplicationRepository.countByStatus(LeaveStatus.APPROVED))
                .thenReturn(2L);

        // Act
        long count =
                leaveApplicationRepository.countByStatus(LeaveStatus.APPROVED);

        // Assert
        assertThat(count).isEqualTo(2);
    }

    // ------------------------------------------------
    // FIND BY EMPLOYEE
    // ------------------------------------------------

    @Test
    void findByEmployeeOrderByAppliedOnDesc_ReturnsList() {

        // Arrange
        Employee employee = new Employee();
        employee.setEmployeeId("EMP001");

        LeaveApplication leave = new LeaveApplication();
        leave.setEmployee(employee);

        when(leaveApplicationRepository
                .findByEmployeeOrderByAppliedOnDesc(employee))
                .thenReturn(List.of(leave));

        // Act
        List<LeaveApplication> result =
                leaveApplicationRepository
                        .findByEmployeeOrderByAppliedOnDesc(employee);

        // Assert
        assertThat(result).isNotEmpty();
    }

    // ------------------------------------------------
    // FIND BY EMPLOYEE ID
    // ------------------------------------------------

    @Test
    void findByEmployeeId_ReturnsLeaves() {

        // Arrange
        LeaveApplication leave = new LeaveApplication();

        when(leaveApplicationRepository
                .findByEmployee_EmployeeIdOrderByAppliedOnDesc("EMP001"))
                .thenReturn(List.of(leave));

        // Act
        List<LeaveApplication> result =
                leaveApplicationRepository
                        .findByEmployee_EmployeeIdOrderByAppliedOnDesc("EMP001");

        // Assert
        assertThat(result).isNotEmpty();
    }

    // ------------------------------------------------
    // FIND BY DATE RANGE
    // ------------------------------------------------

    @Test
    void findByDateRange_ReturnsLeaves() {

        // Arrange
        LeaveApplication leave = new LeaveApplication();
        leave.setStartDate(LocalDate.now());
        leave.setEndDate(LocalDate.now().plusDays(3));

        when(leaveApplicationRepository
                .findByDateRange(any(), any()))
                .thenReturn(List.of(leave));

        // Act
        List<LeaveApplication> result =
                leaveApplicationRepository
                        .findByDateRange(LocalDate.now(),
                                LocalDate.now().plusDays(5));

        // Assert
        assertThat(result).isNotEmpty();
    }

    // ------------------------------------------------
    // FIND TEAM LEAVES
    // ------------------------------------------------

    @Test
    void findTeamLeavesByManagerId_ReturnsLeaves() {

        // Arrange
        LeaveApplication leave = new LeaveApplication();

        when(leaveApplicationRepository
                .findTeamLeavesByManagerId("M001"))
                .thenReturn(List.of(leave));

        // Act
        List<LeaveApplication> result =
                leaveApplicationRepository
                        .findTeamLeavesByManagerId("M001");

        // Assert
        assertThat(result).isNotEmpty();
    }

    // ------------------------------------------------
    // FIND TEAM LEAVES BY STATUS
    // ------------------------------------------------

    @Test
    void findTeamLeavesByManagerIdAndStatus_ReturnsLeaves() {

        // Arrange
        LeaveApplication leave = new LeaveApplication();

        when(leaveApplicationRepository
                .findTeamLeavesByManagerIdAndStatus("M001", LeaveStatus.APPROVED))
                .thenReturn(List.of(leave));

        // Act
        List<LeaveApplication> result =
                leaveApplicationRepository
                        .findTeamLeavesByManagerIdAndStatus(
                                "M001", LeaveStatus.APPROVED);

        // Assert
        assertThat(result).isNotEmpty();
    }

    // ------------------------------------------------
    // FIND LEAVES BY EMPLOYEE
    // ------------------------------------------------

    @Test
    void findLeavesByEmployee_ReturnsLeaves() {

        // Arrange
        LeaveApplication leave = new LeaveApplication();

        when(leaveApplicationRepository
                .findLeavesByEmployee("EMP001"))
                .thenReturn(List.of(leave));

        // Act
        List<LeaveApplication> result =
                leaveApplicationRepository
                        .findLeavesByEmployee("EMP001");

        // Assert
        assertThat(result).isNotEmpty();
    }
}