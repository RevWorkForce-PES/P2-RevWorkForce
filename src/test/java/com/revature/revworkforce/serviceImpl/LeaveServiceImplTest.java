package com.revature.revworkforce.serviceImpl;

import com.revature.revworkforce.dto.LeaveApplicationDTO;
import com.revature.revworkforce.dto.LeaveBalanceDTO;
import com.revature.revworkforce.enums.LeaveStatus;
import com.revature.revworkforce.exception.*;
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

        @Test
        void applyLeave_EndDateBeforeStartDate_ThrowsException() {
                LeaveApplicationDTO dto = new LeaveApplicationDTO();
                dto.setStartDate(LocalDate.now().plusDays(5));
                dto.setEndDate(LocalDate.now().plusDays(2));

                Employee employee = new Employee();
                employee.setEmployeeId("EMP001");
                when(employeeRepository.findById("EMP001")).thenReturn(Optional.of(employee));

                assertThatThrownBy(() -> leaveService.applyLeave(dto, "EMP001"))
                                .isInstanceOf(ValidationException.class)
                                .hasMessageContaining("End date cannot be before start date");
        }

        @Test
        void applyLeave_OverlappingLeave_ThrowsException() {
                LeaveApplicationDTO dto = new LeaveApplicationDTO();
                dto.setStartDate(LocalDate.now());
                dto.setEndDate(LocalDate.now().plusDays(2));

                Employee employee = new Employee();
                employee.setEmployeeId("EMP001");
                when(employeeRepository.findById("EMP001")).thenReturn(Optional.of(employee));

                LeaveApplication overlap = new LeaveApplication();
                overlap.setStartDate(LocalDate.now());
                overlap.setEndDate(LocalDate.now());

                when(leaveApplicationRepository.findOverlappingLeaves(anyString(), any(), any(), any()))
                                .thenReturn(List.of(overlap));

                assertThatThrownBy(() -> leaveService.applyLeave(dto, "EMP001"))
                                .isInstanceOf(LeaveOverlapException.class);
        }

        @Test
        void cancelLeave_Unauthorized_ThrowsException() {
                LeaveApplication leave = new LeaveApplication();
                Employee owner = new Employee();
                owner.setEmployeeId("OWNER");
                leave.setEmployee(owner);

                when(leaveApplicationRepository.findById(1L)).thenReturn(Optional.of(leave));

                assertThatThrownBy(() -> leaveService.cancelLeave(1L, "OTHER"))
                                .isInstanceOf(UnauthorizedException.class);
        }

        @Test
        void cancelLeave_InvalidStatus_ThrowsException() {
                LeaveApplication leave = new LeaveApplication();
                Employee owner = new Employee();
                owner.setEmployeeId("OWNER");
                leave.setEmployee(owner);
                leave.setStatus(LeaveStatus.APPROVED);

                when(leaveApplicationRepository.findById(1L)).thenReturn(Optional.of(leave));

                assertThatThrownBy(() -> leaveService.cancelLeave(1L, "OWNER"))
                                .isInstanceOf(InvalidStatusTransitionException.class);
        }

        @Test
        void revokeLeave_Success() {
                LeaveApplication leave = new LeaveApplication();
                leave.setApplicationId(1L);
                leave.setStatus(LeaveStatus.APPROVED);
                leave.setTotalDays(2);
                leave.setStartDate(LocalDate.now());
                Employee employee = new Employee();
                leave.setEmployee(employee);
                LeaveType type = new LeaveType();
                leave.setLeaveType(type);

                when(leaveApplicationRepository.findById(1L)).thenReturn(Optional.of(leave));

                LeaveBalance balance = new LeaveBalance();
                balance.setUsed(2);
                balance.setBalance(10);
                when(leaveBalanceRepository.findByEmployeeAndLeaveTypeAndYear(any(), any(), any()))
                                .thenReturn(Optional.of(balance));

                leaveService.revokeLeave(1L, "ADMIN");

                assertThat(leave.getStatus()).isEqualTo(LeaveStatus.CANCELLED);
                assertThat(balance.getBalance()).isEqualTo(12);
                assertThat(balance.getUsed()).isEqualTo(0);
                verify(leaveApplicationRepository).save(leave);
        }

        @Test
        void approveLeave_Unauthorized_ThrowsException() {
                LeaveApplication leave = new LeaveApplication();
                Employee emp = new Employee();
                Employee manager = new Employee();
                manager.setEmployeeId("MANAGER");
                emp.setManager(manager);
                leave.setEmployee(emp);
                leave.setStatus(LeaveStatus.PENDING);

                Employee other = new Employee();
                other.setEmployeeId("OTHER");
                Set<Role> roles = Set.of();
                other.setRoles(roles);

                when(leaveApplicationRepository.findById(1L)).thenReturn(Optional.of(leave));
                when(employeeRepository.findById("OTHER")).thenReturn(Optional.of(other));

                assertThatThrownBy(() -> leaveService.approveLeave(1L, "OTHER", "OK"))
                                .isInstanceOf(UnauthorizedException.class);
        }

        @Test
        void getLeaveBalances_Success() {
                LeaveBalance lb = new LeaveBalance();
                LeaveType type = new LeaveType();
                type.setLeaveCode("CL");
                type.setLeaveName("Casual Leave");
                lb.setLeaveType(type);
                lb.setTotalAllocated(12);
                lb.setUsed(2);
                lb.setBalance(10);
                lb.setYear(2024);

                when(leaveBalanceRepository.findByEmployee_EmployeeIdAndYear("EMP001", 2024))
                                .thenReturn(List.of(lb));

                List<LeaveBalanceDTO> balances = leaveService.getLeaveBalances("EMP001", 2024);

                assertThat(balances).hasSize(1);
                assertThat(balances.get(0).getLeaveType()).isEqualTo("CL");
                assertThat(balances.get(0).getRemainingBalance()).isEqualTo(10);
        }

        @Test
        void getTeamLeaveBalances_Success() {
                Employee emp = new Employee();
                emp.setEmployeeId("EMP001");
                when(employeeRepository.findByManager_EmployeeId("M001")).thenReturn(List.of(emp));

                LeaveBalance lb = new LeaveBalance();
                when(leaveBalanceRepository.findByEmployeeIdWithLeaveType("EMP001")).thenReturn(List.of(lb));

                List<LeaveBalance> balances = leaveService.getTeamLeaveBalances("M001");

                assertThat(balances).hasSize(1);
        }

        @Test
        void initializeLeaveBalances_Success() {
                Employee emp = new Employee();
                LeaveType type = new LeaveType();
                type.setDefaultDays(12);
                when(leaveTypeRepository.findAll()).thenReturn(List.of(type));
                when(leaveBalanceRepository.findByEmployeeAndLeaveTypeAndYear(any(), any(), any()))
                                .thenReturn(Optional.empty());

                leaveService.initializeLeaveBalances(emp);

                verify(leaveBalanceRepository).save(any(LeaveBalance.class));
        }

        @Test
        void getPendingLeavesForManager_Success() {
                when(leaveApplicationRepository.findPendingLeavesByManagerId("M001", LeaveStatus.PENDING))
                                .thenReturn(List.of(new LeaveApplication()));

                List<LeaveApplication> result = leaveService.getPendingLeavesForManager("M001");

                assertThat(result).hasSize(1);
        }

        @Test
        void getTeamLeaves_Success() {
                when(leaveApplicationRepository.findTeamLeavesByManagerId("M001"))
                                .thenReturn(List.of(new LeaveApplication()));

                List<LeaveApplication> result = leaveService.getTeamLeaves("M001");

                assertThat(result).hasSize(1);
        }
}