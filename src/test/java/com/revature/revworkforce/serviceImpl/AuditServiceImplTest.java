package com.revature.revworkforce.serviceImpl;

import com.revature.revworkforce.dto.AuditLogDTO;
import com.revature.revworkforce.enums.AuditAction;
import com.revature.revworkforce.model.AuditLog;
import com.revature.revworkforce.model.Employee;
import com.revature.revworkforce.repository.AuditLogRepository;
import com.revature.revworkforce.repository.EmployeeRepository;
import com.revature.revworkforce.service.impl.AuditServiceImpl;
import com.revature.revworkforce.util.Constants;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuditServiceImplTest {

    @Mock
    private AuditLogRepository auditLogRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private AuditServiceImpl auditService;

    private Employee employee;
    private AuditLog auditLog;

    @BeforeEach
    void setUp() {

        employee = new Employee();
        employee.setEmployeeId("EMP001");
        employee.setFirstName("John");
        employee.setLastName("Doe");

        auditLog = new AuditLog();
        auditLog.setAuditId(1L);
        auditLog.setEmployee(employee);
        auditLog.setAction("TEST_ACTION");
        auditLog.setTableName("TEST_TABLE");
        auditLog.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void createAuditLog_WithEmployee() {

        when(employeeRepository.findById("EMP001"))
                .thenReturn(Optional.of(employee));

        when(auditLogRepository.save(any(AuditLog.class)))
                .thenReturn(auditLog);

        AuditLog result = auditService.createAuditLog(
                "EMP001","ACTION","TABLE","1","OLD","NEW","IP","UA");

        assertThat(result).isNotNull();
        verify(auditLogRepository).save(any(AuditLog.class));
    }

    @Test
    void createAuditLog_System() {

        when(auditLogRepository.save(any(AuditLog.class)))
                .thenReturn(auditLog);

        AuditLog result = auditService.createAuditLog(
                "SYSTEM","ACTION","TABLE","1","OLD","NEW","IP","UA");

        assertThat(result).isNotNull();
        verify(employeeRepository, never()).findById(anyString());
    }

    @Test
    void logEmployeeCreation_Success() {

        when(employeeRepository.findById("EMP001"))
                .thenReturn(Optional.of(employee));

        when(auditLogRepository.save(any(AuditLog.class)))
                .thenReturn(auditLog);

        auditService.logEmployeeCreation("EMP001", employee);

        verify(auditLogRepository).save(any(AuditLog.class));
    }

    @Test
    void logEmployeeUpdate_Success() {

        when(employeeRepository.findById("EMP001"))
                .thenReturn(Optional.of(employee));

        when(auditLogRepository.save(any(AuditLog.class)))
                .thenReturn(auditLog);

        auditService.logEmployeeUpdate("EMP001", "EMP002", "Changes");

        verify(auditLogRepository).save(any(AuditLog.class));
    }

    @Test
    void logLogin_Success() {

        when(employeeRepository.findById("EMP001"))
                .thenReturn(Optional.of(employee));

        when(auditLogRepository.save(any(AuditLog.class)))
                .thenReturn(auditLog);

        auditService.logLogin("EMP001","IP","UA");

        verify(auditLogRepository).save(any(AuditLog.class));
    }

    @Test
    void logLogout_Success() {

        when(employeeRepository.findById("EMP001"))
                .thenReturn(Optional.of(employee));

        when(auditLogRepository.save(any(AuditLog.class)))
                .thenReturn(auditLog);

        auditService.logLogout("EMP001","IP");

        verify(auditLogRepository).save(any(AuditLog.class));
    }

    @Test
    void logLeaveApproval_Success() {

        when(employeeRepository.findById("EMP001"))
                .thenReturn(Optional.of(employee));

        when(auditLogRepository.save(any(AuditLog.class)))
                .thenReturn(auditLog);

        auditService.logLeaveApproval("EMP001",1L);

        verify(auditLogRepository).save(any(AuditLog.class));
    }

    @Test
    void logLeaveRejection_Success() {

        when(employeeRepository.findById("EMP001"))
                .thenReturn(Optional.of(employee));

        when(auditLogRepository.save(any(AuditLog.class)))
                .thenReturn(auditLog);

        auditService.logLeaveRejection("EMP001",1L);

        verify(auditLogRepository).save(any(AuditLog.class));
    }

    @Test
    void getAllAuditLogs_Success() {

        when(auditLogRepository.findAll())
                .thenReturn(Arrays.asList(auditLog));

        List<AuditLog> result = auditService.getAllAuditLogs();

        assertThat(result).hasSize(1);
    }

    @Test
    void getRecentAuditLogs_Success() {

        when(auditLogRepository.findRecentLogs(10))
                .thenReturn(Arrays.asList(auditLog));

        List<AuditLog> result = auditService.getRecentAuditLogs(10);

        assertThat(result).hasSize(1);
    }

    @Test
    void getLoginActivities_Success() {

        when(auditLogRepository.findByAction(Constants.AUDIT_LOGIN))
                .thenReturn(Arrays.asList(auditLog));

        List<AuditLog> result = auditService.getLoginActivities();

        assertThat(result).hasSize(1);
    }

    @Test
    void getLoginActivitiesAsDTO_Success() {

        when(auditLogRepository.findByAction(Constants.AUDIT_LOGIN))
                .thenReturn(Arrays.asList(auditLog));

        List<AuditLogDTO> result = auditService.getLoginActivitiesAsDTO();

        assertThat(result).hasSize(1);
    }

    @Test
    void cleanOldAuditLogs_Success() {

        when(auditLogRepository.deleteOldLogs(any()))
                .thenReturn(5);

        auditService.cleanOldAuditLogs();

        verify(auditLogRepository).deleteOldLogs(any());
    }

    @Test
    void logAction_Success() {

        when(employeeRepository.findById("EMP001"))
                .thenReturn(Optional.of(employee));

        when(auditLogRepository.save(any(AuditLog.class)))
                .thenReturn(auditLog);

        auditService.logAction(
                "EMP001",
                AuditAction.UPDATE,
                "ENTITY",
                "1",
                "Details",
                "IP");

        verify(auditLogRepository).save(any(AuditLog.class));
    }

    @Test
    void searchAuditLogs_Success() {

        when(auditLogRepository.searchLogs(any(), any(), any(), any()))
                .thenReturn(Arrays.asList(auditLog));

        List<AuditLogDTO> result =
                auditService.searchAuditLogs("All Modules", null, null, "keyword");

        assertThat(result).hasSize(1);
    }

    @Test
    void exportAuditLogsToCSV_Success() {

        AuditLogDTO dto = new AuditLogDTO();
        dto.setAuditId(1L);
        dto.setPerformedAt(LocalDateTime.now());
        dto.setPerformedBy("EMP001");
        dto.setAction("ACTION");
        dto.setTableName("TABLE");
        dto.setNewValue("NEW");

        byte[] result = auditService.exportAuditLogsToCSV(Arrays.asList(dto));

        assertThat(result).isNotEmpty();
    }
}