package com.revature.revworkforce.repository;

import com.revature.revworkforce.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Department entity.
 * 
 * Provides CRUD operations and custom query methods for Department.
 * 
 * @author RevWorkForce Team
 */
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    
    /**
     * Find department by name.
     * 
     * @param departmentName the department name
     * @return Optional containing the department if found
     */
    Optional<Department> findByDepartmentName(String departmentName);
    
    /**
     * Find all active departments.
     * 
     * @param isActive 'Y' for active, 'N' for inactive
     * @return list of departments
     */
    List<Department> findByIsActive(Character isActive);
    
    /**
     * Check if department exists by name.
     * 
     * @param departmentName the department name
     * @return true if exists
     */
    boolean existsByDepartmentName(String departmentName);
    
    /**
     * Find all departments ordered by name.
     * 
     * @return list of departments
     */
    List<Department> findAllByOrderByDepartmentNameAsc();
}