package com.revature.revworkforce.repository;

import com.revature.revworkforce.model.EmployeeRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for EmployeeRole entity.
 * 
 * Provides CRUD operations and custom query methods for EmployeeRole.
 * 
 * @author RevWorkForce Team
 */
@Repository
public interface EmployeeRoleRepository extends JpaRepository<EmployeeRole, EmployeeRole.EmployeeRoleId> {
    
    /**
     * Find all roles for an employee.
     * 
     * @param employeeId the employee ID
     * @return list of employee roles
     */
    List<EmployeeRole> findByEmployeeId(String employeeId);
    
    /**
     * Find all employees with a specific role.
     * 
     * @param roleId the role ID
     * @return list of employee roles
     */
    List<EmployeeRole> findByRoleId(Long roleId);
    
    /**
     * Check if employee has a specific role.
     * 
     * @param employeeId the employee ID
     * @param roleId the role ID
     * @return true if employee has the role
     */
    boolean existsByEmployeeIdAndRoleId(String employeeId, Long roleId);
    
    /**
     * Delete role assignment for an employee.
     * 
     * @param employeeId the employee ID
     * @param roleId the role ID
     */
    void deleteByEmployeeIdAndRoleId(String employeeId, Long roleId);
    
    /**
     * Delete all role assignments for an employee.
     * 
     * @param employeeId the employee ID
     */
    void deleteByEmployeeId(String employeeId);
    
    /**
     * Get role names for an employee.
     * 
     * @param employeeId the employee ID
     * @return list of role names
     */
    @Query("SELECT r.roleName FROM EmployeeRole er JOIN er.role r WHERE er.employeeId = :employeeId")
    List<String> findRoleNamesByEmployeeId(@Param("employeeId") String employeeId);
}