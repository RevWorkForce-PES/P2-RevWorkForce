package com.revature.revworkforce.repository;

import com.revature.revworkforce.model.AuditLog;
import com.revature.revworkforce.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for AuditLog entity.
 * 
 * Provides CRUD operations and custom query methods for AuditLog.
 * 
 * @author RevWorkForce Team
 */
@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    
    /**
     * Find audit logs by employee.
     * 
     * @param employee the employee
     * @return list of audit logs
     */
    List<AuditLog> findByEmployeeOrderByCreatedAtDesc(Employee employee);
    
    /**
     * Find audit logs by action.
     * 
     * @param action the action (INSERT, UPDATE, DELETE, LOGIN, etc.)
     * @return list of audit logs
     */
    List<AuditLog> findByAction(String action);
    
    /**
     * Find audit logs by table name.
     * 
     * @param tableName the table name
     * @return list of audit logs
     */
    List<AuditLog> findByTableNameOrderByCreatedAtDesc(String tableName);
    
    /**
     * Find audit logs by table and record ID.
     * 
     * @param tableName the table name
     * @param recordId the record ID
     * @return list of audit logs
     */
    List<AuditLog> findByTableNameAndRecordIdOrderByCreatedAtDesc(String tableName, String recordId);
    
    /**
     * Find audit logs by date range.
     * 
     * @param startDate the start date
     * @param endDate the end date
     * @return list of audit logs
     */
    @Query("SELECT al FROM AuditLog al WHERE al.createdAt BETWEEN :startDate AND :endDate ORDER BY al.createdAt DESC")
    List<AuditLog> findByDateRange(@Param("startDate") LocalDateTime startDate, 
                                    @Param("endDate") LocalDateTime endDate);
    
    /**
     * Find audit logs by employee and date range.
     * 
     * @param employee the employee
     * @param startDate the start date
     * @param endDate the end date
     * @return list of audit logs
     */
    @Query("SELECT al FROM AuditLog al WHERE al.employee = :employee " +
           "AND al.createdAt BETWEEN :startDate AND :endDate ORDER BY al.createdAt DESC")
    List<AuditLog> findByEmployeeAndDateRange(@Param("employee") Employee employee,
                                               @Param("startDate") LocalDateTime startDate,
                                               @Param("endDate") LocalDateTime endDate);
    
    /**
     * Find audit logs by employee and action.
     * 
     * @param employee the employee
     * @param action the action
     * @return list of audit logs
     */
    List<AuditLog> findByEmployeeAndAction(Employee employee, String action);
    
    /**
     * Find recent audit logs (last N records).
     * 
     * @param limit the maximum number of records
     * @return list of recent audit logs
     */
    @Query(value = "SELECT * FROM AUDIT_LOGS ORDER BY created_at DESC FETCH FIRST :limit ROWS ONLY", 
           nativeQuery = true)
    List<AuditLog> findRecentLogs(@Param("limit") int limit);
    
    /**
     * Count audit logs by action.
     * 
     * @param action the action
     * @return count of audit logs
     */
    long countByAction(String action);
    
    /**
     * Count audit logs by employee.
     * 
     * @param employee the employee
     * @return count of audit logs
     */
    long countByEmployee(Employee employee);
    
    /**
     * Find login activities by employee.
     * 
     * @param employee the employee
     * @return list of login audit logs
     */
    @Query("SELECT al FROM AuditLog al WHERE al.employee = :employee AND al.action = 'LOGIN' ORDER BY al.createdAt DESC")
    List<AuditLog> findLoginActivities(@Param("employee") Employee employee);
}