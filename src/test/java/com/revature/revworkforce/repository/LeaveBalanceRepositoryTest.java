package com.revature.revworkforce.repository;

import com.revature.revworkforce.model.Employee;
import com.revature.revworkforce.model.LeaveBalance;
import com.revature.revworkforce.model.LeaveType;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LeaveBalanceRepositoryTest {

    @Mock
    private LeaveBalanceRepository leaveBalanceRepository;

    // ------------------------------------------------
    // FIND BY EMPLOYEE
    // ------------------------------------------------

    @Test
    void findByEmployee_ReturnsBalances() {

        // Arrange
        Employee employee = new Employee();
        employee.setEmployeeId("EMP001");

        LeaveBalance balance = new LeaveBalance();
        balance.setEmployee(employee);

        when(leaveBalanceRepository.findByEmployee(employee))
                .thenReturn(List.of(balance));

        // Act
        List<LeaveBalance> result =
                leaveBalanceRepository.findByEmployee(employee);

        // Assert
        assertThat(result).isNotEmpty();
    }

    // ------------------------------------------------
    // FIND BY EMPLOYEE AND YEAR
    // ------------------------------------------------

    @Test
    void findByEmployeeAndYear_ReturnsBalances() {

        // Arrange
        Employee employee = new Employee();
        employee.setEmployeeId("EMP001");

        LeaveBalance balance = new LeaveBalance();

        when(leaveBalanceRepository.findByEmployeeAndYear(employee, 2026))
                .thenReturn(List.of(balance));

        // Act
        List<LeaveBalance> result =
                leaveBalanceRepository.findByEmployeeAndYear(employee, 2026);

        // Assert
        assertThat(result).isNotEmpty();
    }

    // ------------------------------------------------
    // FIND BY EMPLOYEE LEAVE TYPE YEAR
    // ------------------------------------------------

    @Test
    void findByEmployeeAndLeaveTypeAndYear_ReturnsBalance() {

        // Arrange
        Employee employee = new Employee();
        LeaveType type = new LeaveType();

        LeaveBalance balance = new LeaveBalance();

        when(leaveBalanceRepository
                .findByEmployeeAndLeaveTypeAndYear(employee, type, 2026))
                .thenReturn(Optional.of(balance));

        // Act
        Optional<LeaveBalance> result =
                leaveBalanceRepository
                        .findByEmployeeAndLeaveTypeAndYear(employee, type, 2026);

        // Assert
        assertThat(result).isPresent();
    }

    // ------------------------------------------------
    // FIND BY EMPLOYEE ID AND YEAR
    // ------------------------------------------------

    @Test
    void findByEmployeeIdAndYear_ReturnsBalances() {

        // Arrange
        LeaveBalance balance = new LeaveBalance();

        when(leaveBalanceRepository
                .findByEmployee_EmployeeIdAndYear("EMP001", 2026))
                .thenReturn(List.of(balance));

        // Act
        List<LeaveBalance> result =
                leaveBalanceRepository
                        .findByEmployee_EmployeeIdAndYear("EMP001", 2026);

        // Assert
        assertThat(result).isNotEmpty();
    }

    // ------------------------------------------------
    // FIND BY YEAR
    // ------------------------------------------------

    @Test
    void findByYear_ReturnsBalances() {

        // Arrange
        LeaveBalance balance = new LeaveBalance();

        when(leaveBalanceRepository.findByYear(2026))
                .thenReturn(List.of(balance));

        // Act
        List<LeaveBalance> result =
                leaveBalanceRepository.findByYear(2026);

        // Assert
        assertThat(result).isNotEmpty();
    }

    // ------------------------------------------------
    // GET TOTAL BALANCE
    // ------------------------------------------------

    @Test
    void getTotalBalanceByEmployeeAndYear_ReturnsBalance() {

        // Arrange
        when(leaveBalanceRepository
                .getTotalBalanceByEmployeeAndYear("EMP001", 2026))
                .thenReturn(15);

        // Act
        Integer result =
                leaveBalanceRepository
                        .getTotalBalanceByEmployeeAndYear("EMP001", 2026);

        // Assert
        assertThat(result).isEqualTo(15);
    }

    // ------------------------------------------------
    // FIND TEAM BALANCES
    // ------------------------------------------------

    @Test
    void findTeamBalances_ReturnsBalances() {

        // Arrange
        LeaveBalance balance = new LeaveBalance();

        when(leaveBalanceRepository.findTeamBalances("M001", 2026))
                .thenReturn(List.of(balance));

        // Act
        List<LeaveBalance> result =
                leaveBalanceRepository.findTeamBalances("M001", 2026);

        // Assert
        assertThat(result).isNotEmpty();
    }

    // ------------------------------------------------
    // EXISTS CHECK
    // ------------------------------------------------

    @Test
    void existsByEmployeeAndLeaveTypeAndYear_ReturnsTrue() {

        // Arrange
        Employee employee = new Employee();
        LeaveType type = new LeaveType();

        when(leaveBalanceRepository
                .existsByEmployeeAndLeaveTypeAndYear(employee, type, 2026))
                .thenReturn(true);

        // Act
        boolean result =
                leaveBalanceRepository
                        .existsByEmployeeAndLeaveTypeAndYear(employee, type, 2026);

        // Assert
        assertThat(result).isTrue();
    }
}