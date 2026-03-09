package com.revature.revworkforce.serviceImpl;

import com.revature.revworkforce.dto.LeaveApplicationDTO;
import com.revature.revworkforce.enums.LeaveStatus;
import com.revature.revworkforce.exception.InsufficientLeaveBalanceException;
import com.revature.revworkforce.model.*;
import com.revature.revworkforce.repository.*;
import com.revature.revworkforce.service.HolidayService;
import com.revature.revworkforce.service.NotificationService;
import com.revature.revworkforce.service.AuditService;
import com.revature.revworkforce.service.impl.LeaveServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LeaveServiceImplTest {

        @Mock
        private LeaveApplicationRepository leaveApplicationRepository;

        @Mock
        private LeaveBalanceRepository leaveBalanceRepository;

        @Mock
        private LeaveTypeRepository leaveTypeRepository;

        @Mock
        private EmployeeRepository employeeRepository;

        @Mock
        private HolidayService holidayService;

        @Mock
        private NotificationService notificationService;

        @Mock
        private AuditService auditService;

        @InjectMocks
        private LeaveServiceImpl leaveService;

        // --------------------------------------------------
        // APPLY LEAVE
        // --------------------------------------------------

        @Test
        void applyLeave_Success() {

                // Arrange
                Employee employee = new Employee();
                employee.setEmployeeId("EMP001");

                LeaveType leaveType = new LeaveType();
                leaveType.setLeaveCode("CL");

                LeaveBalance balance = new LeaveBalance();
                balance.setBalance(10);

                LeaveApplicationDTO dto = new LeaveApplicationDTO();
                dto.setLeaveType("CL");
                dto.setStartDate(LocalDate.now());
                dto.setEndDate(LocalDate.now());
                dto.setReason("Medical");

                when(employeeRepository.findById("EMP001"))
                                .thenReturn(Optional.of(employee));

                when(leaveTypeRepository.findByLeaveCode("CL"))
                                .thenReturn(Optional.of(leaveType));

                when(leaveBalanceRepository.findByEmployeeAndLeaveTypeAndYear(any(), any(), any()))
                                .thenReturn(Optional.of(balance));

                when(holidayService.getHolidaysInRange(any(), any()))
                                .thenReturn(Set.of());

                when(leaveApplicationRepository.save(any()))
                                .thenAnswer(i -> i.getArguments()[0]);

                // Act
                LeaveApplication result = leaveService.applyLeave(dto, "EMP001");

                // Assert
                assertThat(result).isNotNull();
                verify(leaveApplicationRepository).save(any());
        }

        // --------------------------------------------------
        // GET LEAVE BY ID
        // --------------------------------------------------

        @Test
        void getLeaveById_Success() {

                // Arrange
                LeaveApplication leave = new LeaveApplication();
                leave.setApplicationId(1L);

                when(leaveApplicationRepository.findById(1L))
                                .thenReturn(Optional.of(leave));

                // Act
                LeaveApplication result = leaveService.getLeaveById(1L);

                // Assert
                assertThat(result).isNotNull();
        }

        // --------------------------------------------------
        // CANCEL LEAVE
        // --------------------------------------------------

        @Test
        void cancelLeave_Success() {

                // Arrange
                Employee employee = new Employee();
                employee.setEmployeeId("EMP001");

                LeaveApplication leave = new LeaveApplication();
                leave.setApplicationId(1L);
                leave.setEmployee(employee);
                leave.setStatus(LeaveStatus.PENDING);

                when(leaveApplicationRepository.findById(1L))
                                .thenReturn(Optional.of(leave));

                // Act
                leaveService.cancelLeave(1L, "EMP001");

                // Assert
                verify(leaveApplicationRepository).save(any());
        }

        // --------------------------------------------------
        // APPROVE LEAVE
        // --------------------------------------------------

        @Test
        void approveLeave_Success() {

                // Arrange
                Employee employee = new Employee();
                employee.setEmployeeId("EMP001");

                Employee manager = new Employee();
                manager.setEmployeeId("M001");

                employee.setManager(manager);

                LeaveType type = new LeaveType();
                type.setLeaveCode("CL");

                LeaveApplication leave = new LeaveApplication();
                leave.setApplicationId(1L);
                leave.setEmployee(employee);
                leave.setLeaveType(type);
                leave.setTotalDays(2);
                leave.setStatus(LeaveStatus.PENDING);
                leave.setStartDate(LocalDate.now());
                leave.setEndDate(LocalDate.now());

                LeaveBalance balance = new LeaveBalance();
                balance.setBalance(10);
                balance.setUsed(0);

                when(leaveApplicationRepository.findById(1L))
                                .thenReturn(Optional.of(leave));

                when(employeeRepository.findById("M001"))
                                .thenReturn(Optional.of(manager));

                when(leaveBalanceRepository.findByEmployeeAndLeaveTypeAndYear(any(), any(), any()))
                                .thenReturn(Optional.of(balance));

                when(leaveApplicationRepository.save(any()))
                                .thenAnswer(i -> i.getArguments()[0]);

                // Act
                LeaveApplication result = leaveService.approveLeave(1L, "M001", "Approved");

                // Assert
                assertThat(result.getStatus()).isEqualTo(LeaveStatus.APPROVED);
        }

        // --------------------------------------------------
        // REJECT LEAVE
        // --------------------------------------------------

        @Test
        void rejectLeave_Success() {

                // Arrange
                Employee employee = new Employee();
                employee.setEmployeeId("EMP001");

                Employee manager = new Employee();
                manager.setEmployeeId("M001");

                employee.setManager(manager);

                LeaveApplication leave = new LeaveApplication();
                leave.setApplicationId(1L);
                leave.setEmployee(employee);
                leave.setStatus(LeaveStatus.PENDING);
                leave.setStartDate(LocalDate.now());
                leave.setEndDate(LocalDate.now());

                when(leaveApplicationRepository.findById(1L))
                                .thenReturn(Optional.of(leave));

                when(employeeRepository.findById("M001"))
                                .thenReturn(Optional.of(manager));

                when(leaveApplicationRepository.save(any()))
                                .thenAnswer(i -> i.getArguments()[0]);

                // Act
                LeaveApplication result = leaveService.rejectLeave(1L, "M001", "Invalid request");

                // Assert
                assertThat(result.getStatus()).isEqualTo(LeaveStatus.REJECTED);
        }

        // --------------------------------------------------
        // VALIDATE LEAVE BALANCE
        // --------------------------------------------------

        @Test
        void validateLeaveBalance_ThrowsException() {

                // Arrange
                LeaveType type = new LeaveType();
                type.setLeaveCode("CL");

                LeaveBalance balance = new LeaveBalance();
                balance.setLeaveType(type);
                balance.setBalance(1);

                // Act + Assert
                assertThatThrownBy(() -> leaveService.validateLeaveBalance(balance, 5))
                                .isInstanceOf(InsufficientLeaveBalanceException.class);
        }

        // --------------------------------------------------
        // GET EMPLOYEE LEAVE HISTORY
        // --------------------------------------------------

        @Test
        void getEmployeeLeaveHistory_Success() {

                // Arrange
                when(leaveApplicationRepository
                                .findByEmployee_EmployeeIdOrderByAppliedOnDesc("EMP001"))
                                .thenReturn(List.of(new LeaveApplication()));

                // Act
                List<LeaveApplication> leaves = leaveService.getEmployeeLeaveHistory("EMP001");

                // Assert
                assertThat(leaves).isNotEmpty();
        }
}