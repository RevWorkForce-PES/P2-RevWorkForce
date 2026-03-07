package com.revature.revworkforce.repository;

import com.revature.revworkforce.model.AuditLog;
import com.revature.revworkforce.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for AuditLog entity.
 * Provides CRUD operations and custom queries for AuditLog.
 */
@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    // -------------------------
    // Standard Queries
    // -------------------------

    List<AuditLog> findByEmployeeOrderByCreatedAtDesc(Employee employee);

    List<AuditLog> findByAction(String action);

    List<AuditLog> findByTableNameOrderByCreatedAtDesc(String tableName);

    List<AuditLog> findByTableNameAndRecordIdOrderByCreatedAtDesc(String tableName, String recordId);

    @Query("SELECT al FROM AuditLog al WHERE al.createdAt BETWEEN :startDate AND :endDate ORDER BY al.createdAt DESC")
    List<AuditLog> findByDateRange(@Param("startDate") LocalDateTime startDate,
                                   @Param("endDate") LocalDateTime endDate);

    @Query("SELECT al FROM AuditLog al WHERE al.employee = :employee " +
           "AND al.createdAt BETWEEN :startDate AND :endDate ORDER BY al.createdAt DESC")
    List<AuditLog> findByEmployeeAndDateRange(@Param("employee") Employee employee,
                                              @Param("startDate") LocalDateTime startDate,
                                              @Param("endDate") LocalDateTime endDate);

    List<AuditLog> findByEmployeeAndAction(Employee employee, String action);

    @Query("SELECT al FROM AuditLog al WHERE al.employee = :employee AND al.action = 'LOGIN' ORDER BY al.createdAt DESC")
    List<AuditLog> findLoginActivities(@Param("employee") Employee employee);

    @Query(value = "SELECT * FROM AUDIT_LOGS ORDER BY created_at DESC FETCH FIRST :limit ROWS ONLY", nativeQuery = true)
    List<AuditLog> findRecentLogs(@Param("limit") int limit);

    long countByAction(String action);

    long countByEmployee(Employee employee);

    // -------------------------
    // Custom Deletion
    // -------------------------

    /**
     * Delete audit logs older than a given date.
     *
     * @param retentionDate cutoff date
     * @return number of rows deleted
     */
    @Modifying
    @Query("DELETE FROM AuditLog al WHERE al.createdAt < :retentionDate")
    int deleteOldLogs(@Param("retentionDate") LocalDateTime retentionDate);
}