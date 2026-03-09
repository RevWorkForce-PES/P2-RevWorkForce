package com.revature.revworkforce.service;

import com.revature.revworkforce.dto.AuditLogDTO;
import com.revature.revworkforce.enums.AuditAction;
import com.revature.revworkforce.model.AuditLog;
import com.revature.revworkforce.model.Employee;

import java.time.LocalDateTime;
import java.util.List;

public interface AuditService {

        AuditLog createAuditLog(String performedBy,
                        String action,
                        String tableName,
                        String recordId,
                        String oldValue,
                        String newValue,
                        String ipAddress,
                        String userAgent);

        void logEmployeeCreation(String performedBy, Employee employee);

        void logEmployeeUpdate(String performedBy, String employeeId, String changes);

        void logLogin(String employeeId, String ipAddress, String userAgent);

        void logLogout(String employeeId, String ipAddress);

        void logLeaveApproval(String performedBy, Long applicationId);

        void logLeaveRejection(String performedBy, Long applicationId);

        void logAction(String employeeId, AuditAction passwordReset, String string, String employeeId2, Object object,
                        String string2);

        List<AuditLog> getAllAuditLogs();

        List<AuditLog> getRecentAuditLogs(int limit);

        List<AuditLog> getAuditLogsByTableAndRecord(String tableName,
                        String recordId);

        List<AuditLog> getAuditLogsByDateRange(LocalDateTime startDate,
                        LocalDateTime endDate);

        List<AuditLog> getLoginActivities();

        List<AuditLogDTO> getLoginActivitiesAsDTO();

        void cleanOldAuditLogs();

        AuditLogDTO convertToDTO(AuditLog auditLog);

        List<AuditLogDTO> getRecentAuditLogsAsDTO(int limit);

        List<AuditLogDTO> searchAuditLogs(String module, LocalDateTime startDate, LocalDateTime endDate,
                        String keyword);

        byte[] exportAuditLogsToCSV(List<AuditLogDTO> logs);

}