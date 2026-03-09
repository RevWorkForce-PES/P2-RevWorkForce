package com.revature.revworkforce.serviceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
    void setup() {

        employee = new Employee();
        employee.setEmployeeId("EMP001");
        employee.setFirstName("Aishwarya");

        auditLog = new AuditLog();
        auditLog.setAuditId(1L);
        auditLog.setEmployee(employee);
        auditLog.setAction("INSERT");
        auditLog.setTableName("EMPLOYEES");
        auditLog.setRecordId("EMP001");
        auditLog.setOldValue(null);
        auditLog.setNewValue("Created employee");
        auditLog.setCreatedAt(LocalDateTime.now());
    }

    // =====================================================
    // CREATE AUDIT LOG
    // =====================================================

    @Test
    void createAuditLog_Success() {

        when(employeeRepository.findById("EMP001"))
                .thenReturn(Optional.of(employee));

        when(auditLogRepository.save(any(AuditLog.class)))
                .thenReturn(auditLog);

        AuditLog result = auditService.createAuditLog(
                "EMP001",
                "INSERT",
                "EMPLOYEES",
                "EMP001",
                null,
                "Created employee",
                null,
                null);

        assertNotNull(result);
        verify(auditLogRepository).save(any(AuditLog.class));
    }

    // =====================================================
    // EMPLOYEE LOGS
    // =====================================================

    @Test
    void logEmployeeCreation_Success() {

        when(auditLogRepository.save(any())).thenReturn(auditLog);

        auditService.logEmployeeCreation("EMP001", employee);

        verify(auditLogRepository).save(any());
    }

    @Test
    void logEmployeeUpdate_Success() {

        when(auditLogRepository.save(any())).thenReturn(auditLog);

        auditService.logEmployeeUpdate("EMP001", "EMP001", "Updated employee");

        verify(auditLogRepository).save(any());
    }

    // =====================================================
    // LOGIN / LOGOUT
    // =====================================================

    @Test
    void logLogin_Success() {

        when(auditLogRepository.save(any())).thenReturn(auditLog);

        auditService.logLogin("EMP001", "127.0.0.1", "Chrome");

        verify(auditLogRepository).save(any());
    }

    @Test
    void logLogout_Success() {

        when(auditLogRepository.save(any())).thenReturn(auditLog);

        auditService.logLogout("EMP001", "127.0.0.1");

        verify(auditLogRepository).save(any());
    }
    @Test
    void convertToDTO_Success() {

        AuditLogDTO dto = auditService.convertToDTO(auditLog);

        assertNotNull(dto);
        assertEquals(auditLog.getAuditId(), dto.getAuditId());
        assertEquals(employee.getEmployeeId(), dto.getPerformedBy());
        assertEquals(employee.getFullName(), dto.getPerformedByName());
        assertEquals(auditLog.getAction(), dto.getAction());
    }
    // =====================================================
    // LEAVE LOGS
    // =====================================================

    @Test
    void logLeaveApproval_Success() {

        when(auditLogRepository.save(any())).thenReturn(auditLog);

        auditService.logLeaveApproval("EMP001", 10L);

        verify(auditLogRepository).save(any());
    }

    @Test
    void logLeaveRejection_Success() {

        when(auditLogRepository.save(any())).thenReturn(auditLog);

        auditService.logLeaveRejection("EMP001", 10L);

        verify(auditLogRepository).save(any());
    }

    // =====================================================
    // FETCH METHODS
    // =====================================================

    @Test
    void getAllAuditLogs_Success() {

        when(auditLogRepository.findAll()).thenReturn(List.of(auditLog));

        List<AuditLog> result = auditService.getAllAuditLogs();

        assertEquals(1, result.size());
    }

    @Test
    void getRecentAuditLogs_Success() {

        when(auditLogRepository.findRecentLogs(5)).thenReturn(List.of(auditLog));

        List<AuditLog> result = auditService.getRecentAuditLogs(5);

        assertEquals(1, result.size());
    }

    @Test
    void getLoginActivities_Success() {

        when(auditLogRepository.findByAction(Constants.AUDIT_LOGIN))
                .thenReturn(List.of(auditLog));

        List<AuditLog> result = auditService.getLoginActivities();

        assertEquals(1, result.size());
    }

    // =====================================================
    // DTO CONVERSION
    // =====================================================

   

    @Test
    void getLoginActivitiesAsDTO_Success() {

        when(auditLogRepository.findByAction(Constants.AUDIT_LOGIN))
                .thenReturn(List.of(auditLog));

        List<AuditLogDTO> result = auditService.getLoginActivitiesAsDTO();

        assertEquals(1, result.size());
    }

    // =====================================================
    // CLEAN OLD LOGS
    // =====================================================

    @Test
    void cleanOldAuditLogs_Success() {

        when(auditLogRepository.deleteOldLogs(any())).thenReturn(2);

        auditService.cleanOldAuditLogs();

        verify(auditLogRepository).deleteOldLogs(any());
    }

    // =====================================================
    // CSV EXPORT
    // =====================================================

    @Test
    void exportAuditLogsToCSV_Success() {

        AuditLogDTO dto = new AuditLogDTO();
        dto.setAuditId(1L);
        dto.setPerformedBy("EMP001");
        dto.setAction("INSERT");
        dto.setTableName("EMPLOYEES");
        dto.setNewValue("Created employee");
        dto.setPerformedAt(LocalDateTime.now());

        byte[] result = auditService.exportAuditLogsToCSV(List.of(dto));

        assertNotNull(result);
        assertTrue(new String(result).contains("EMP001"));
    }

    // =====================================================
    // GENERIC ACTION LOG
    // =====================================================

    @Test
    void logAction_Success() {

        when(auditLogRepository.save(any())).thenReturn(auditLog);

        auditService.logAction(
                "EMP001",
                AuditAction.INSERT,
                "GOALS",
                "1",
                "Goal created",
                "127.0.0.1");

        verify(auditLogRepository).save(any());
    }

}