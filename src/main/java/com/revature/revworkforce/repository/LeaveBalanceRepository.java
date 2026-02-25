package com.revature.revworkforce.repository;

import com.revature.revworkforce.model.Employee;
import com.revature.revworkforce.model.LeaveBalance;
import com.revature.revworkforce.model.LeaveType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for LeaveBalance entity.
 * 
 * Provides CRUD operations and custom query methods for LeaveBalance.
 * 
 * @author RevWorkForce Team
 */
@Repository
public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance, Long> {
    
    /**
     * Find leave balances by employee.
     * 
     * @param employee the employee
     * @return list of leave balances
     */
    List<LeaveBalance> findByEmployee(Employee employee);
    
    /**
     * Find leave balances by employee and year.
     * 
     * @param employee the employee
     * @param year the year
     * @return list of leave balances
     */
    List<LeaveBalance> findByEmployeeAndYear(Employee employee, Integer year);
    
    /**
     * Find leave balance by employee, leave type, and year.
     * 
     * @param employee the employee
     * @param leaveType the leave type
     * @param year the year
     * @return Optional containing the leave balance if found
     */
    Optional<LeaveBalance> findByEmployeeAndLeaveTypeAndYear(Employee employee, 
                                                              LeaveType leaveType, 
                                                              Integer year);
    
    /**
     * Find leave balances by employee ID and year.
     * 
     * @param employeeId the employee ID
     * @param year the year
     * @return list of leave balances
     */
    List<LeaveBalance> findByEmployee_EmployeeIdAndYear(String employeeId, Integer year);
    
    /**
     * Find all leave balances for a specific year.
     * 
     * @param year the year
     * @return list of leave balances
     */
    List<LeaveBalance> findByYear(Integer year);
    
    /**
     * Get total remaining balance for employee in a year.
     * 
     * @param employeeId the employee ID
     * @param year the year
     * @return total balance
     */
    @Query("SELECT SUM(lb.balance) FROM LeaveBalance lb WHERE lb.employee.employeeId = :employeeId AND lb.year = :year")
    Integer getTotalBalanceByEmployeeAndYear(@Param("employeeId") String employeeId, 
                                             @Param("year") Integer year);
    
    /**
     * Check if leave balance exists for employee, leave type, and year.
     * 
     * @param employee the employee
     * @param leaveType the leave type
     * @param year the year
     * @return true if exists
     */
    boolean existsByEmployeeAndLeaveTypeAndYear(Employee employee, LeaveType leaveType, Integer year);
}