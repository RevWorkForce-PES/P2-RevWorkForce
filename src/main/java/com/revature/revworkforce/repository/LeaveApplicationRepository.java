package com.revature.revworkforce.repository;

import com.revature.revworkforce.enums.LeaveStatus;
import com.revature.revworkforce.model.Employee;
import com.revature.revworkforce.model.LeaveApplication;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for LeaveApplication entity.
 * 
 * Provides CRUD operations and custom query methods for LeaveApplication.
 * 
 * @author RevWorkForce Team
 */
@Repository
public interface LeaveApplicationRepository extends JpaRepository<LeaveApplication, Long> {

       /**
        * Find all leave applications by employee.
        * 
        * @param employee the employee
        * @return list of leave applications
        */
       List<LeaveApplication> findByEmployeeOrderByAppliedOnDesc(Employee employee);

       /**
        * Find all leave applications by employee ID.
        * 
        * @param employeeId the employee ID
        * @return list of leave applications
        */
       List<LeaveApplication> findByEmployee_EmployeeIdOrderByAppliedOnDesc(String employeeId);

       /**
        * Find all leave applications by status.
        * 
        * @param status the leave status
        * @return list of leave applications
        */
       List<LeaveApplication> findByStatus(LeaveStatus status);

       /**
        * Find all leave applications by employee and status.
        * 
        * @param employee the employee
        * @param status   the leave status
        * @return list of leave applications
        */
       List<LeaveApplication> findByEmployeeAndStatus(Employee employee, LeaveStatus status);

       /**
        * Find pending leave applications for manager approval.
        * (Employee's manager is the specified manager)
        * 
        * @param managerId the manager's employee ID
        * @param status    the leave status (typically PENDING)
        * @return list of pending leave applications
        */
       @Query("SELECT la FROM LeaveApplication la WHERE la.employee.manager.employeeId = :managerId AND la.status = :status")
       List<LeaveApplication> findPendingLeavesByManagerId(@Param("managerId") String managerId,
                     @Param("status") LeaveStatus status);

       /**
        * Find all leaves for team members (employees reporting to manager).
        * 
        * @param managerId the manager's employee ID
        * @return list of leave applications
        */
       @Query("SELECT la FROM LeaveApplication la WHERE la.employee.manager.employeeId = :managerId ORDER BY la.appliedOn DESC")
       List<LeaveApplication> findTeamLeavesByManagerId(@Param("managerId") String managerId);

       /**
        * Check for overlapping leave applications.
        * 
        * @param employeeId      the employee ID
        * @param startDate       the start date
        * @param endDate         the end date
        * @param excludeStatuses the statuses to exclude (REJECTED, CANCELLED)
        * @return list of overlapping leave applications
        */
       @Query("SELECT la FROM LeaveApplication la WHERE la.employee.employeeId = :employeeId " +
                     "AND la.status NOT IN :excludeStatuses " +
                     "AND ((la.startDate <= :endDate AND la.endDate >= :startDate))")
       List<LeaveApplication> findOverlappingLeaves(@Param("employeeId") String employeeId,
                     @Param("startDate") LocalDate startDate,
                     @Param("endDate") LocalDate endDate,
                     @Param("excludeStatuses") List<LeaveStatus> excludeStatuses);

       /**
        * Find leave applications by date range.
        * 
        * @param startDate the start date
        * @param endDate   the end date
        * @return list of leave applications
        */
       @Query("SELECT la FROM LeaveApplication la WHERE la.startDate <= :endDate AND la.endDate >= :startDate")
       List<LeaveApplication> findByDateRange(@Param("startDate") LocalDate startDate,
                     @Param("endDate") LocalDate endDate);

       /**
        * Find approved leaves by employee and year.
        * 
        * @param employeeId the employee ID
        * @param year       the year
        * @param status     the leave status (APPROVED)
        * @return list of leave applications
        */
       @Query("SELECT la FROM LeaveApplication la WHERE la.employee.employeeId = :employeeId " +
                     "AND EXTRACT(YEAR FROM la.startDate) = :year AND la.status = :status")
       List<LeaveApplication> findApprovedLeavesByEmployeeAndYear(@Param("employeeId") String employeeId,
                     @Param("year") Integer year,
                     @Param("status") LeaveStatus status);

       /**
        * Count leave applications by employee and status.
        * 
        * @param employee the employee
        * @param status   the leave status
        * @return count of leave applications
        */
       long countByEmployeeAndStatus(Employee employee, LeaveStatus status);
}