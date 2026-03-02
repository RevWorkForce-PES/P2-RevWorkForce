package com.revature.revworkforce.repository;

import com.revature.revworkforce.enums.EmployeeStatus;
import com.revature.revworkforce.model.Department;
import com.revature.revworkforce.model.Designation;
import com.revature.revworkforce.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Employee entity.
 * 
 * Provides CRUD operations and custom query methods for Employee.
 * 
 * @author RevWorkForce Team
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {

       /**
        * Find employee by email.
        * 
        * @param email the email address
        * @return Optional containing the employee if found
        */
	Optional<Employee> findByEmployeeId(String employeeId);
       Optional<Employee> findByEmail(String email);

       /**
        * Find employee by email or employee ID.
        * Used for login.
        * 
        * @param email      the email address
        * @param employeeId the employee ID
        * @return Optional containing the employee if found
        */
       Optional<Employee> findByEmailOrEmployeeId(String email, String employeeId);

       /**
        * Find all employees by status.
        * 
        * @param status the employee status
        * @return list of employees
        */
       List<Employee> findByStatus(EmployeeStatus status);

       /**
        * Find all employees by department.
        * 
        * @param department the department
        * @return list of employees
        */
       List<Employee> findByDepartment(Department department);

       /**
        * Find all employees by designation.
        * 
        * @param designation the designation
        * @return list of employees
        */
       List<Employee> findByDesignation(Designation designation);

       /**
        * Find all employees reporting to a manager.
        * 
        * @param manager the manager employee
        * @return list of employees
        */
       List<Employee> findByManager(Employee manager);

       /**
        * Find all employees by manager ID.
        * 
        * @param managerId the manager's employee ID
        * @return list of employees
        */
       List<Employee> findByManager_EmployeeId(String managerId);

       /**
        * Search employees by name (first name or last name).
        * 
        * @param firstName the first name
        * @param lastName  the last name
        * @return list of employees
        */
       @Query("SELECT e FROM Employee e WHERE " +
                     "LOWER(e.firstName) LIKE LOWER(CONCAT('%', :firstName, '%')) OR " +
                     "LOWER(e.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))")
       List<Employee> searchByName(@Param("firstName") String firstName,
                     @Param("lastName") String lastName);

       /**
        * Search employees by keyword (searches in name, email, employee ID).
        * 
        * @param keyword the search keyword
        * @return list of employees
        */
       @Query("SELECT e FROM Employee e WHERE " +
                     "LOWER(e.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                     "LOWER(e.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                     "LOWER(e.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                     "LOWER(e.employeeId) LIKE LOWER(CONCAT('%', :keyword, '%'))")
       List<Employee> searchByKeyword(@Param("keyword") String keyword);

       /**
        * Find all active employees.
        * 
        * @return list of active employees
        */
       List<Employee> findByStatusOrderByFirstNameAsc(EmployeeStatus status);

       /**
        * Count employees by department.
        * 
        * @param department the department
        * @return count of employees
        */
       long countByDepartment(Department department);

       /**
        * Count employees by designation.
        * 
        * @param designation the designation
        * @return count of employees
        */
       long countByDesignation(Designation designation);

       /**
        * Count employees by status.
        * 
        * @param status the employee status
        * @return count of employees
        */
       long countByStatus(EmployeeStatus status);

       /**
        * Check if email exists.
        * 
        * @param email the email address
        * @return true if exists
        */
       boolean existsByEmail(String email);

       /**
        * Check if employee ID exists.
        * 
        * @param employeeId the employee ID
        * @return true if exists
        */
       boolean existsById(String employeeId);

       /**
        * Find employees by department and status.
        * 
        * @param department the department
        * @param status     the employee status
        * @return list of employees
        */
       List<Employee> findByDepartmentAndStatus(Department department, EmployeeStatus status);

       /**
        * Count employees grouped by status.
        * 
        * @return List of Object arrays where [0] is EmployeeStatus and [1] is the
        *         count.
        */
       @Query("SELECT e.status, COUNT(e) FROM Employee e GROUP BY e.status")
       List<Object[]> countEmployeesByStatus();

       /**
        * Count employees grouped by department.
        * 
        * @return List of Object arrays where [0] is Department and [1] is the count.
        */
       @Query("SELECT e.department.departmentName, COUNT(e) FROM Employee e GROUP BY e.department.departmentName")
       List<Object[]> countEmployeesByDepartment();

       /**
        * Count employees grouped by designation.
        * 
        * @return List of Object arrays where [0] is Designation and [1] is the count.
        */
       @Query("SELECT e.designation.designationName, COUNT(e) FROM Employee e GROUP BY e.designation.designationName")
       List<Object[]> countEmployeesByDesignation();

       /**
        * Count employees grouped by gender.
        * 
        * @return List of Object arrays where [0] is Gender and [1] is the count.
        */
       @Query("SELECT e.gender, COUNT(e) FROM Employee e GROUP BY e.gender")
       List<Object[]> countEmployeesByGender();

       /**
        * Get average employee tenure in years.
        * 
        * @return Average tenure
        */
       @Query("SELECT AVG(EXTRACT(YEAR FROM CURRENT_DATE) - EXTRACT(YEAR FROM e.joiningDate)) FROM Employee e WHERE e.joiningDate IS NOT NULL")
       Double getAverageTenure();

       /**
        * Find employees by account locked status.
        * 
        * @param accountLocked the locked status ('Y' or 'N')
        * @return list of employees
        */
       List<Employee> findByAccountLocked(Character accountLocked);

       /**
        * Find employees by first login status.
        * 
        * @param firstLogin the first login status ('Y' or 'N')
        * @return list of employees
        */
       List<Employee> findByFirstLogin(Character firstLogin);
}