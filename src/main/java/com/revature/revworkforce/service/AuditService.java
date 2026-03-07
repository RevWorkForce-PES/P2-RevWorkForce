package com.revature.revworkforce.service;

//import com.revature.revworkforce.dto.AuditLogDTO;
//import com.revature.revworkforce.enums.AuditAction;
//import com.revature.revworkforce.model.AuditLog;
//import com.revature.revworkforce.model.Employee;
//
//import com.revature.revworkforce.repository.AuditLogRepository;
//import com.revature.revworkforce.repository.EmployeeRepository;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@Transactional
//public class AuditService {
//
//    @Autowired
//    private AuditLogRepository auditLogRepository;
//
//    @Autowired
//    private EmployeeRepository employeeRepository;
//
//    /* =========================================================
//       CORE AUDIT CREATION METHOD
//    ========================================================= */
//
//    public void createAuditLog(String performedBy,
//                               String action,
//                               String tableName,
//                               Long recordId,
//                               String oldValue,
//                               String newValue,
//                               String ipAddress,
//                               String userAgent) {
//
//        Employee employee = null;
//
//        if (performedBy != null) {
//            employee = employeeRepository
//                    .findByEmployeeId(performedBy)
//                    .orElse(null);
//        }
//
//        AuditLog log = new AuditLog();
//        log.setEmployee(employee);
//        log.setAction(action);
//        log.setTableName(tableName);
//        log.setRecordId(recordId != null ? recordId.toString() : null);
//        log.setOldValue(oldValue);
//        log.setNewValue(newValue);
//        log.setIpAddress(ipAddress);
//        log.setUserAgent(userAgent);
//        log.setDescription(action + " operation on " + tableName);
//
//        auditLogRepository.save(log);
//    }
//
//    /* =========================================================
//       EMPLOYEE INSERT LOG
//    ========================================================= */
//
//    public void logEmployeeCreation(String performedBy, Employee employee) {
//
//        createAuditLog(
//                performedBy,
//                AuditAction.INSERT.name(),
//                "EMPLOYEE",
//                null,
//                null,
//                employee.toString(),
//                null,
//                null
//        );
//    }
//
//    /* =========================================================
//       EMPLOYEE UPDATE LOG
//    ========================================================= */
//
//    public void logEmployeeUpdate(String performedBy,
//                                  String employeeId,
//                                  String changes) {
//
//        createAuditLog(
//                performedBy,
//                AuditAction.UPDATE.name(),
//                "EMPLOYEE",
//                null,
//                null,
//                changes,
//                null,
//                null
//        );
//    }
//
//    /* =========================================================
//       FETCH RECENT LOGS (Used in Controller)
//    ========================================================= */
//
//    @PreAuthorize("hasRole('ADMIN')")
//    public List<AuditLogDTO> getRecentAuditLogs(int limit) {
//
//        return auditLogRepository
//                .findRecentLogs(limit)
//                .stream()
//                .map(this::convertToDTO)
//                .collect(Collectors.toList());
//    }
//
//    /* =========================================================
//       FETCH LOGIN ACTIVITIES
//    ========================================================= */
//
//    @PreAuthorize("hasRole('ADMIN')")
//    public List<AuditLogDTO> getLoginActivities() {
//
//        return auditLogRepository
//                .findByAction(AuditAction.LOGIN.name())
//                .stream()
//                .map(this::convertToDTO)
//                .collect(Collectors.toList());
//    }
//
//    /* =========================================================
//       FETCH BY DATE RANGE
//    ========================================================= */
//
//    @PreAuthorize("hasRole('ADMIN')")
//    public List<AuditLogDTO> getAuditLogsByDateRange(LocalDateTime start,
//                                                     LocalDateTime end) {
//
//        return auditLogRepository
//                .findByDateRange(start, end)
//                .stream()
//                .map(this::convertToDTO)
//                .collect(Collectors.toList());
//    }
//
//    /* =========================================================
//       DTO CONVERSION
//    ========================================================= */
//
//    private AuditLogDTO convertToDTO(AuditLog log) {
//
//        return new AuditLogDTO(
//                log.getAuditId(),
//                log.getEmployee() != null
//                        ? log.getEmployee().getEmployeeId()
//                        : null,
//                log.getAction(),
//                log.getTableName(),
//                log.getRecordId(),
//                log.getFieldName(),
//                log.getOldValue(),
//                log.getNewValue(),
//                log.getDescription(),
//                log.getIpAddress(),
//                log.getUserAgent(),
//                log.getCreatedAt()
//        );
//    }
//

import java.time.LocalDateTime;
import java.util.List;

import com.revature.revworkforce.dto.AuditLogDTO;
import com.revature.revworkforce.enums.AuditAction;
import com.revature.revworkforce.model.AuditLog;
import com.revature.revworkforce.model.Employee;

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
    
	void logAction(String employeeId, AuditAction passwordReset, String string, String employeeId2, Object object, String string2);



    List<AuditLog> getAllAuditLogs();

    List<AuditLog> getRecentAuditLogs(int limit);

    List<AuditLog> getAuditLogsByTableAndRecord(String tableName,
                                                String recordId); 

    List<AuditLog> getAuditLogsByDateRange(LocalDateTime startDate,
                                           LocalDateTime endDate);

    List<AuditLog> getLoginActivities();

    void cleanOldAuditLogs();

    AuditLogDTO convertToDTO(AuditLog auditLog);

    List<AuditLogDTO> getRecentAuditLogsAsDTO(int limit);


}