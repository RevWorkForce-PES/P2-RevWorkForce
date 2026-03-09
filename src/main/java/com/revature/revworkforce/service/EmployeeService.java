package com.revature.revworkforce.service;

import com.revature.revworkforce.dto.EmployeeDTO;
import com.revature.revworkforce.dto.EmployeeSearchCriteria;
import com.revature.revworkforce.model.Employee;
import com.revature.revworkforce.model.Role;

import java.util.List;
import java.util.Set;

/**
 * Employee Service Interface.
 *
 * <p>
 * Defines business operations for managing employees
 * in the RevWorkForce system.
 * </p>
 *
 * <p>
 * This interface abstracts employee-related business logic
 * and allows multiple implementations if required.
 * </p>
 *
 * @author RevWorkForce Team
 */
public interface EmployeeService {

    /**
     * Creates a new employee in the system.
     *
     * @param dto employee data transfer object
     * @return created Employee entity
     */
    Employee createEmployee(EmployeeDTO dto);

    /**
     * Updates an existing employee.
     *
     * @param employeeId unique employee ID
     * @param dto        updated employee data
     * @return updated Employee entity
     */
    Employee updateEmployee(String employeeId, EmployeeDTO dto);

    /**
     * Retrieves employee by ID.
     *
     * @param employeeId unique employee ID
     * @return Employee entity
     */
    Employee getEmployeeById(String employeeId);

    EmployeeDTO getEmployeeDTOById(String employeeId);

    /**
     * Retrieves all employees.
     *
     * @return list of employees
     */
    List<Employee> getAllEmployees();

    /**
     * Retrieves all active employees.
     *
     * @return list of active employees
     */
    List<Employee> getActiveEmployees();

    List<EmployeeDTO> getActiveEmployeesAsDTO();

    /**
     * Searches employees based on criteria.
     *
     * @param criteria search filters
     * @return list of matching employees
     */
    List<Employee> searchEmployees(EmployeeSearchCriteria criteria);

    List<EmployeeDTO> searchEmployeesAsDTO(EmployeeSearchCriteria criteria);

    /**
     * Retrieves employees by department.
     *
     * @param departmentId department ID
     * @return list of employees
     */
    List<Employee> getEmployeesByDepartment(Long departmentId);

    /**
     * Retrieves employees by designation.
     *
     * @param designationId designation ID
     * @return list of employees
     */
    List<Employee> getEmployeesByDesignation(Long designationId);

    /**
     * Retrieves team members reporting to a manager.
     *
     * @param managerId manager employee ID
     * @return list of team members
     */
    List<Employee> getTeamMembers(String managerId);

    /**
     * Deactivate an employee (soft delete).
     * 
     * @param employeeId the ID of the employee
     */
    void deactivateEmployee(String employeeId);

    /**
     * Reactivate a deactivated employee.
     * 
     * @param employeeId the ID of the employee
     */
    void reactivateEmployee(String employeeId);

    /**
     * Soft deletes an employee (sets status to TERMINATED).
     *
     * @param employeeId employee ID
     */
    void deleteEmployee(String employeeId);

    /**
     * Assigns a role to an employee.
     *
     * @param employeeId employee ID
     * @param roleId     role ID
     */
    void assignRole(String employeeId, Long roleId);

    /**
     * Removes a role from an employee.
     *
     * @param employeeId employee ID
     * @param roleId     role ID
     */
    void removeRole(String employeeId, Long roleId);

    /**
     * Retrieves roles assigned to an employee.
     *
     * @param employeeId employee ID
     * @return set of roles
     */
    Set<Role> getEmployeeRoles(String employeeId);

    /**
     * Generates unique employee ID using prefix.
     *
     * @param prefix prefix such as EMP, MGR, ADM
     * @return generated employee ID
     */
    String generateEmployeeId(String prefix);

    /**
     * Converts Employee entity to DTO.
     *
     * @param employee Employee entity
     * @return EmployeeDTO
     */
    EmployeeDTO convertToDTO(Employee employee);
}