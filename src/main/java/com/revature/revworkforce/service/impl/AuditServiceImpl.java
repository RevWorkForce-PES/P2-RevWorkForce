package com.revature.revworkforce.service.impl;

import com.revature.revworkforce.dto.AuditLogDTO;
import com.revature.revworkforce.enums.AuditAction;
import com.revature.revworkforce.exception.ResourceNotFoundException;
import com.revature.revworkforce.model.AuditLog;
import com.revature.revworkforce.model.Employee;
import com.revature.revworkforce.repository.AuditLogRepository;
import com.revature.revworkforce.repository.EmployeeRepository;
import com.revature.revworkforce.service.AuditService;
import com.revature.revworkforce.util.Constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AuditServiceImpl implements AuditService {

    private static final Logger logger =
            LoggerFactory.getLogger(AuditServiceImpl.class);

    private final AuditLogRepository auditLogRepository;
    private final EmployeeRepository employeeRepository;

    public AuditServiceImpl(AuditLogRepository auditLogRepository,
                            EmployeeRepository employeeRepository) {
        this.auditLogRepository = auditLogRepository;
        this.employeeRepository = employeeRepository;
    }

    // ============================================================
    // Core Audit Creation
    // ============================================================

    @Override
    public AuditLog createAuditLog(String performedBy,
                                   String action,
                                   String tableName,
                                   String recordId,
                                   String oldValue,
                                   String newValue,
                                   String ipAddress,
                                   String userAgent) {

        Employee employee = employeeRepository.findById(performedBy)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found: " + performedBy));

        AuditLog auditLog = new AuditLog();
        auditLog.setEmployee(employee);
        auditLog.setAction(action);
        auditLog.setTableName(tableName);
        auditLog.setRecordId(recordId);
        auditLog.setOldValue(oldValue);
        auditLog.setNewValue(newValue);
        auditLog.setIpAddress(ipAddress);
        auditLog.setUserAgent(userAgent);

        return auditLogRepository.save(auditLog);
    }

    // ============================================================
    // Employee Logs
    // ============================================================

    @Override
    public void logEmployeeCreation(String performedBy, Employee employee) {
        createAuditLog(
                performedBy,
                Constants.AUDIT_INSERT,
                "EMPLOYEES",
                employee.getEmployeeId(),
                null,
                "Created employee: " + employee.getFullName(),
                null,
                null
        );
    }

    @Override
    public void logEmployeeUpdate(String performedBy,
                                  String employeeId,
                                  String changes) {

        createAuditLog(
                performedBy,
                Constants.AUDIT_UPDATE,
                "EMPLOYEES",
                employeeId,
                null,
                changes,
                null,
                null
        );
    }

    // ============================================================
    // Login / Logout
    // ============================================================

    @Override
    public void logLogin(String employeeId,
                         String ipAddress,
                         String userAgent) {

        createAuditLog(
                employeeId,
                Constants.AUDIT_LOGIN,
                "LOGIN_ACTIVITY",
                null,
                null,
                "Login successful",
                ipAddress,
                userAgent
        );
    }

    @Override
    public void logLogout(String employeeId,
                          String ipAddress) {

        createAuditLog(
                employeeId,
                Constants.AUDIT_LOGOUT,
                "LOGIN_ACTIVITY",
                null,
                null,
                "Logout",
                ipAddress,
                null
        );
    }

    // ============================================================
    // Leave Management
    // ============================================================

    @Override
    public void logLeaveApproval(String performedBy,
                                 Long applicationId) {

        createAuditLog(
                performedBy,
                Constants.AUDIT_APPROVE,
                "LEAVE_APPLICATIONS",
                applicationId != null ? applicationId.toString() : null,
                "PENDING",
                "APPROVED",
                null,
                null
        );
    }

    @Override
    public void logLeaveRejection(String performedBy,
                                  Long applicationId) {

        createAuditLog(
                performedBy,
                Constants.AUDIT_REJECT,
                "LEAVE_APPLICATIONS",
                applicationId != null ? applicationId.toString() : null,
                "PENDING",
                "REJECTED",
                null,
                null
        );
    }

    // ============================================================
    // Query Methods
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getAllAuditLogs() {
        return auditLogRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getRecentAuditLogs(int limit) {
        return auditLogRepository.findRecentLogs(limit);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getAuditLogsByTableAndRecord(String tableName,
                                                       String recordId) {

        return auditLogRepository
                .findByTableNameAndRecordIdOrderByCreatedAtDesc(
                        tableName,
                        recordId
                );
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getAuditLogsByDateRange(LocalDateTime startDate,
                                                  LocalDateTime endDate) {

        return auditLogRepository.findByDateRange(startDate, endDate);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getLoginActivities() {
        return auditLogRepository.findByAction(Constants.AUDIT_LOGIN);
    }

    // ============================================================
    // Scheduled Cleanup
    // ============================================================

    @Override
    @Scheduled(cron = "0 0 3 1 * ?")
    public void cleanOldAuditLogs() {

        LocalDateTime retentionDate = LocalDateTime.now()
                .minusYears(Constants.AUDIT_RETENTION_YEARS);

        int deleted = auditLogRepository.deleteOldLogs(retentionDate);

        logger.info("Deleted {} old audit logs", deleted);
    }

    // ============================================================
    // DTO Conversion
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public AuditLogDTO convertToDTO(AuditLog auditLog) {

        AuditLogDTO dto = new AuditLogDTO();

        dto.setAuditId(auditLog.getAuditId());

        if (auditLog.getEmployee() != null) {
            dto.setPerformedBy(auditLog.getEmployee().getEmployeeId());
            dto.setPerformedByName(auditLog.getEmployee().getFullName());
        }

        dto.setAction(auditLog.getAction());
        dto.setTableName(auditLog.getTableName());
        dto.setRecordId(auditLog.getRecordId());
        dto.setOldValue(auditLog.getOldValue());
        dto.setNewValue(auditLog.getNewValue());
        dto.setIpAddress(auditLog.getIpAddress());
        dto.setUserAgent(auditLog.getUserAgent());
        dto.setPerformedAt(auditLog.getCreatedAt());

        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLogDTO> getRecentAuditLogsAsDTO(int limit) {

        return getRecentAuditLogs(limit)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void logAction(String employeeId,
                          AuditAction action,
                          String tableName,
                          String recordId,
                          Object details,
                          String ipAddress) {

        Employee employee = null;

        try {
            employee = employeeRepository.findById(employeeId).orElse(null);
        } catch (Exception e) {
            logger.warn("Employee not found for audit log: {}", employeeId);
        }

        AuditLog log = new AuditLog();

        log.setEmployee(employee);
        log.setAction(action.name());
        log.setTableName(tableName);
        log.setRecordId(recordId);

        if(details != null){
            log.setNewValue(details.toString());
            log.setDescription(details.toString());
        }

        log.setIpAddress(ipAddress);
        log.setCreatedAt(LocalDateTime.now());

        auditLogRepository.save(log);

        logger.info("Audit log stored: {} - {}", action, tableName);
    }
}