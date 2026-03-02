package com.revature.revworkforce.repository;

import com.revature.revworkforce.enums.GoalStatus;
import com.revature.revworkforce.enums.Priority;
import com.revature.revworkforce.model.Employee;
import com.revature.revworkforce.model.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for Goal entity.
 * 
 * Provides CRUD operations and custom query methods for Goal.
 * 
 * @author RevWorkForce Team
 */
@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {

       /**
        * Find all goals by employee.
        * 
        * @param employee the employee
        * @return list of goals
        */
       List<Goal> findByEmployeeOrderByCreatedAtDesc(Employee employee);

       /**
        * Find goals by employee and status.
        * 
        * @param employee the employee
        * @param status   the goal status
        * @return list of goals
        */
       List<Goal> findByEmployeeAndStatus(Employee employee, GoalStatus status);

       /**
        * Find goals by status.
        * 
        * @param status the goal status
        * @return list of goals
        */
       List<Goal> findByStatus(GoalStatus status);

       /**
        * Find goals by employee and priority.
        * 
        * @param employee the employee
        * @param priority the priority
        * @return list of goals
        */
       List<Goal> findByEmployeeAndPriority(Employee employee, Priority priority);

       /**
        * Find all goals for team members (employees reporting to manager).
        * 
        * @param managerId the manager's employee ID
        * @return list of goals
        */
       @Query("SELECT g FROM Goal g WHERE g.employee.manager.employeeId = :managerId ORDER BY g.createdAt DESC")
       List<Goal> findTeamGoalsByManagerId(@Param("managerId") String managerId);

       /**
        * Find goals with upcoming deadlines (within specified days).
        * 
        * @param employee   the employee
        * @param today      the current date
        * @param futureDate the future date threshold
        * @return list of goals
        */
       @Query("SELECT g FROM Goal g WHERE g.employee = :employee AND g.deadline BETWEEN :today AND :futureDate " +
                     "AND g.status NOT IN ('COMPLETED', 'CANCELLED') ORDER BY g.deadline")
       List<Goal> findUpcomingDeadlines(@Param("employee") Employee employee,
                     @Param("today") LocalDate today,
                     @Param("futureDate") LocalDate futureDate);

       /**
        * Find overdue goals (deadline passed and not completed).
        * 
        * @param employee the employee
        * @param today    the current date
        * @return list of overdue goals
        */
       @Query("SELECT g FROM Goal g WHERE g.employee = :employee AND g.deadline < :today " +
                     "AND g.status NOT IN ('COMPLETED', 'CANCELLED') ORDER BY g.deadline")
       List<Goal> findOverdueGoals(@Param("employee") Employee employee, @Param("today") LocalDate today);

       /**
        * Find active goals (NOT_STARTED or IN_PROGRESS).
        * 
        * @param employee the employee
        * @return list of active goals
        */
       @Query("SELECT g FROM Goal g WHERE g.employee = :employee AND g.status IN ('NOT_STARTED', 'IN_PROGRESS') ORDER BY g.priority DESC, g.deadline")
       List<Goal> findActiveGoals(@Param("employee") Employee employee);

       /**
        * Count goals by employee and status.
        * 
        * @param employee the employee
        * @param status   the goal status
        * @return count of goals
        */
       long countByEmployeeAndStatus(Employee employee, GoalStatus status);

       /**
        * Count active goals for employee.
        * 
        * @param employee the employee
        * @return count of active goals
        */
       @Query("SELECT COUNT(g) FROM Goal g WHERE g.employee = :employee AND g.status IN ('NOT_STARTED', 'IN_PROGRESS')")
       long countActiveGoals(@Param("employee") Employee employee);

       /**
        * Count all goals grouped by status.
        * 
        * @return List of Object arrays with GoalStatus and count
        */
       @Query("SELECT g.status, COUNT(g) FROM Goal g GROUP BY g.status")
       List<Object[]> countGoalsByStatus();

       /**
        * Get the average progress of all goals.
        * 
        * @return Average progress
        */
       @Query("SELECT AVG(g.progress) FROM Goal g")
       Double getAverageProgress();

       /**
        * Count overdue goals for all employees.
        * 
        * @param today the current date
        * @return count of overdue goals
        */
       @Query("SELECT COUNT(g) FROM Goal g WHERE g.deadline < :today AND g.status NOT IN ('COMPLETED', 'CANCELLED')")
       Long countOverdueGoals(@Param("today") LocalDate today);
}