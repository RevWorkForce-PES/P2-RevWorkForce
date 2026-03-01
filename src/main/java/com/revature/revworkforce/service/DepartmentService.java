package com.revature.revworkforce.service;

import com.revature.revworkforce.dto.DepartmentDTO;

import java.util.List;

/**
 * Service interface for Department operations.
 * 
 * Provides methods for CRUD operations and business logic related to
 * Departments.
 * 
 * @author RevWorkForce Team
 */
public interface DepartmentService {

    /**
     * Creates a new department.
     * 
     * @param departmentDTO the department data
     * @return the created department
     */
    DepartmentDTO createDepartment(DepartmentDTO departmentDTO);

    /**
     * Updates an existing department.
     * 
     * @param departmentId  the ID of the department to update
     * @param departmentDTO the updated department data
     * @return the updated department
     */
    DepartmentDTO updateDepartment(Long departmentId, DepartmentDTO departmentDTO);

    /**
     * Retrieves a department by its ID.
     * 
     * @param departmentId the department ID
     * @return the department
     */
    DepartmentDTO getDepartmentById(Long departmentId);

    /**
     * Retrieves a department by its name.
     * 
     * @param departmentName the department name
     * @return the department
     */
    DepartmentDTO getDepartmentByName(String departmentName);

    /**
     * Retrieves all departments.
     * 
     * @return list of all departments
     */
    List<DepartmentDTO> getAllDepartments();

    /**
     * Retrieves all active departments.
     * 
     * @return list of active departments
     */
    List<DepartmentDTO> getActiveDepartments();

    /**
     * Deletes (soft deletes or deactivates) a department by its ID.
     * 
     * @param departmentId the department ID
     */
    void deleteDepartment(Long departmentId);

    /**
     * Checks if a department exists by its name.
     * 
     * @param departmentName the department name
     * @return true if exists, false otherwise
     */
    boolean existsByDepartmentName(String departmentName);
}
