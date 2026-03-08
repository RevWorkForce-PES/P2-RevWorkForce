package com.revature.revworkforce.repository;

import com.revature.revworkforce.enums.EmployeeStatus;
import com.revature.revworkforce.model.Department;
import com.revature.revworkforce.model.Designation;
import com.revature.revworkforce.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
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
        */
	Optional<Employee> findByEmployeeId(String employeeId);
       Optional<Employee> findByEmail(String email);

       /**
        * Find employee by email or employee ID. Used for login.
        */
       Optional<Employee> findByEmailOrEmployeeId(String email, String employeeId);

       /**
        * Find all employees by status.
        */
       List<Employee> findByStatus(EmployeeStatus status);

       /**
        * Find all employees by department.
        */
       List<Employee> findByDepartment(Department department);

       /**
        * Find all employees by designation.
        */
       List<Employee> findByDesignation(Designation designation);

       /**
        * Find all employees reporting to a manager.
        */
       List<Employee> findByManager(Employee manager);

       /**
        * Find all employees by manager ID.
        */
       List<Employee> findByManager_EmployeeId(String managerId);

       /**
        * Search employees by name (first name or last name).
        */
       @Query("SELECT e FROM Employee e WHERE " +
                     "LOWER(e.firstName) LIKE LOWER(CONCAT('%', :firstName, '%')) OR " +
                     "LOWER(e.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))")
       List<Employee> searchByName(@Param("firstName") String firstName,
                     @Param("lastName") String lastName);

       /**
        * Search employees by keyword (searches in name, email, employee ID).
        */
       @Query("SELECT e FROM Employee e WHERE " +
                     "LOWER(e.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                     "LOWER(e.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                     "LOWER(e.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                     "LOWER(e.employeeId) LIKE LOWER(CONCAT('%', :keyword, '%'))")
       List<Employee> searchByKeyword(@Param("keyword") String keyword);

       /**
        * Find all active employees ordered by first name.
        */
       List<Employee> findByStatusOrderByFirstNameAsc(EmployeeStatus status);

       /**
        * Count employees by department.
        */
       long countByDepartment(Department department);

       /**
        * Count employees by designation.
        */
       long countByDesignation(Designation designation);

       /**
        * Count employees by status.
        */
       long countByStatus(EmployeeStatus status);

       /**
        * Check if email exists.
        */
       boolean existsByEmail(String email);

       /**
        * Check if employee ID exists.
        */
       boolean existsById(String employeeId);

       /**
        * Find employees by department and status.
        */
       List<Employee> findByDepartmentAndStatus(Department department, EmployeeStatus status);

       /**
        * Count employees grouped by status.
        */
       @Query("SELECT e.status, COUNT(e) FROM Employee e GROUP BY e.status")
       List<Object[]> countEmployeesByStatus();

       /**
        * Count employees grouped by department.
        */
       @Query("SELECT e.department.departmentName, COUNT(e) FROM Employee e GROUP BY e.department.departmentName")
       List<Object[]> countEmployeesByDepartment();

       /**
        * Count employees grouped by designation.
        */
       @Query("SELECT e.designation.designationName, COUNT(e) FROM Employee e GROUP BY e.designation.designationName")
       List<Object[]> countEmployeesByDesignation();

       /**
        * Count employees grouped by gender.
        */
       @Query("SELECT e.gender, COUNT(e) FROM Employee e GROUP BY e.gender")
       List<Object[]> countEmployeesByGender();

       /**
        * Get average employee tenure in years.
        */
       @Query("SELECT AVG(EXTRACT(YEAR FROM CURRENT_DATE) - EXTRACT(YEAR FROM e.joiningDate)) FROM Employee e WHERE e.joiningDate IS NOT NULL")
       Double getAverageTenure();

       /**
        * Find employees by account locked status.
        */
       List<Employee> findByAccountLocked(Character accountLocked);

       /**
        * Find employees by first login status.
        */
       List<Employee> findByFirstLogin(Character firstLogin);

       /**
        * Find all ACTIVE employees that have a given role name (case-insensitive).
        * Used to show only managers in the Reporting Manager dropdown.
        */
       @Query("SELECT DISTINCT e FROM Employee e JOIN e.roles r "
                     + "WHERE UPPER(r.roleName) = UPPER(:roleName) "
                     + "AND e.status = com.revature.revworkforce.enums.EmployeeStatus.ACTIVE "
                     + "ORDER BY e.firstName ASC")
       List<Employee> findActiveEmployeesByRoleName(@Param("roleName") String roleName);

       /**
        * Find all ACTIVE employees that have any of the given role names.
        * Used to populate the Reporting Manager dropdown with both MANAGER and ADMIN
        * employees.
        */
       @Query("SELECT DISTINCT e FROM Employee e JOIN e.roles r "
                     + "WHERE UPPER(r.roleName) IN :roleNames "
                     + "AND e.status = com.revature.revworkforce.enums.EmployeeStatus.ACTIVE "
                     + "ORDER BY e.firstName ASC")
       List<Employee> findActiveEmployeesByRoleNames(@Param("roleNames") Collection<String> roleNames);
}
