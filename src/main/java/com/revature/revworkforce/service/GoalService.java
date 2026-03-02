package com.revature.revworkforce.service;

import com.revature.revworkforce.dto.GoalDTO;
import com.revature.revworkforce.enums.GoalStatus;
import com.revature.revworkforce.enums.Priority;
import com.revature.revworkforce.exception.ResourceNotFoundException;
import com.revature.revworkforce.exception.UnauthorizedException;
import com.revature.revworkforce.exception.ValidationException;
import com.revature.revworkforce.model.Employee;
import com.revature.revworkforce.model.Goal;
import com.revature.revworkforce.repository.EmployeeRepository;
import com.revature.revworkforce.repository.GoalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
* Goal Service - P2 VERSION
* Works with existing Goal entity and repository
* 
* @author RevWorkForce Team
*/
@Service
@Transactional
public class GoalService {

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * P2: Create new goal for employee
     */
    public Goal createGoal(String employeeId, String goalTitle, String goalDescription,
                          String category, LocalDate deadline, Priority priority) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "employeeId", employeeId));

        // Validate deadline is in future
        if (deadline != null && deadline.isBefore(LocalDate.now())) {
            throw new ValidationException("Deadline must be in the future");
        }

        Goal goal = new Goal();
        goal.setEmployee(employee);
        goal.setGoalTitle(goalTitle);
        goal.setGoalDescription(goalDescription);
        goal.setCategory(category);
        goal.setDeadline(deadline);
        goal.setPriority(priority != null ? priority : Priority.MEDIUM);
        goal.setProgress(0);
        goal.setStatus(GoalStatus.NOT_STARTED);
        goal.setCreatedAt(LocalDateTime.now());
        goal.setUpdatedAt(LocalDateTime.now());

        return goalRepository.save(goal);
    }

    /**
     * P2: Update goal (employee edits goal)
     */
    public Goal updateGoal(Long goalId, String employeeId, String goalTitle, 
                          String goalDescription, String category, 
                          LocalDate deadline, Priority priority) {

        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new ResourceNotFoundException("Goal", "goalId", goalId));

        // Check authorization
        if (!goal.getEmployee().getEmployeeId().equals(employeeId)) {
            throw new UnauthorizedException("You can only update your own goals");
        }

        // Can only update if NOT completed or cancelled
        if (goal.getStatus() == GoalStatus.COMPLETED || goal.getStatus() == GoalStatus.CANCELLED) {
            throw new ValidationException("Cannot update completed or cancelled goals");
        }

        // Validate deadline
        if (deadline != null && deadline.isBefore(LocalDate.now())) {
            throw new ValidationException("Deadline must be in the future");
        }

        goal.setGoalTitle(goalTitle);
        goal.setGoalDescription(goalDescription);
        goal.setCategory(category);
        goal.setDeadline(deadline);
        goal.setPriority(priority);
        goal.setUpdatedAt(LocalDateTime.now());

        return goalRepository.save(goal);
    }

    /**
     * P2: Update goal progress (employee updates progress)
     */
    public Goal updateProgress(Long goalId, String employeeId, Integer progress) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new ResourceNotFoundException("Goal", "goalId", goalId));

        // Check authorization
        if (!goal.getEmployee().getEmployeeId().equals(employeeId)) {
            throw new UnauthorizedException("You can only update your own goals");
        }

        // Validate progress
        if (progress < 0 || progress > 100) {
            throw new ValidationException("Progress must be between 0 and 100");
        }

        // Update progress (this will auto-update status in Goal entity)
        goal.setProgress(progress);

        // If progress is 100 and not already completed
        if (progress == 100 && goal.getStatus() != GoalStatus.COMPLETED) {
            goal.setStatus(GoalStatus.COMPLETED);
            goal.setCompletedAt(LocalDateTime.now());
        } else if (progress > 0 && progress < 100 && goal.getStatus() == GoalStatus.NOT_STARTED) {
            goal.setStatus(GoalStatus.IN_PROGRESS);
        }

        goal.setUpdatedAt(LocalDateTime.now());

        return goalRepository.save(goal);
    }

    /**
     * P2: Manager adds comments to goal
     */
    public Goal addManagerComments(Long goalId, String managerId, String comments) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new ResourceNotFoundException("Goal", "goalId", goalId));

        Employee manager = employeeRepository.findById(managerId)
                .orElseThrow(() -> new ResourceNotFoundException("Manager", "employeeId", managerId));

        // Verify manager is the employee's manager
        if (goal.getEmployee().getManager() == null || 
            !goal.getEmployee().getManager().getEmployeeId().equals(managerId)) {
            throw new UnauthorizedException("You can only comment on your direct reports' goals");
        }

        goal.setManagerComments(comments);
        goal.setUpdatedAt(LocalDateTime.now());

        return goalRepository.save(goal);
    }

    /**
     * Delete goal (employee only if not started or in progress)
     */
    public void deleteGoal(Long goalId, String employeeId) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new ResourceNotFoundException("Goal", "goalId", goalId));

        // Check authorization
        if (!goal.getEmployee().getEmployeeId().equals(employeeId)) {
            throw new UnauthorizedException("You can only delete your own goals");
        }

        // Can only delete if not completed
        if (goal.getStatus() == GoalStatus.COMPLETED) {
            throw new ValidationException("Cannot delete completed goals");
        }

        goalRepository.delete(goal);
    }

    /**
     * Get goal by ID
     */
    @Transactional(readOnly = true)
    public Goal getGoalById(Long goalId) {
        return goalRepository.findById(goalId)
                .orElseThrow(() -> new ResourceNotFoundException("Goal", "goalId", goalId));
    }

    /**
     * P2: View all my goals with status
     */
    @Transactional(readOnly = true)
    public List<Goal> getEmployeeGoals(String employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "employeeId", employeeId));

        return goalRepository.findByEmployeeOrderByCreatedAtDesc(employee);
    }

    /**
     * Get active goals (NOT_STARTED or IN_PROGRESS)
     */
    @Transactional(readOnly = true)
    public List<Goal> getActiveGoals(String employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "employeeId", employeeId));

        return goalRepository.findActiveGoals(employee);
    }

    /**
     * Get goals by status
     */
    @Transactional(readOnly = true)
    public List<Goal> getGoalsByStatus(String employeeId, GoalStatus status) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "employeeId", employeeId));

        return goalRepository.findByEmployeeAndStatus(employee, status);
    }

    /**
     * P2: View team member goal progress (manager view)
     */
    @Transactional(readOnly = true)
    public List<Goal> getTeamGoals(String managerId) {
        return goalRepository.findTeamGoalsByManagerId(managerId);
    }

    /**
     * Get upcoming deadlines (next 30 days)
     */
    @Transactional(readOnly = true)
    public List<Goal> getUpcomingDeadlines(String employeeId, int days) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "employeeId", employeeId));

        LocalDate today = LocalDate.now();
        LocalDate futureDate = today.plusDays(days);

        return goalRepository.findUpcomingDeadlines(employee, today, futureDate);
    }

    /**
     * Get overdue goals
     */
    @Transactional(readOnly = true)
    public List<Goal> getOverdueGoals(String employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "employeeId", employeeId));

        LocalDate today = LocalDate.now();

        return goalRepository.findOverdueGoals(employee, today);
    }

    /**
     * Get goals by priority
     */
    @Transactional(readOnly = true)
    public List<Goal> getGoalsByPriority(String employeeId, Priority priority) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "employeeId", employeeId));

        return goalRepository.findByEmployeeAndPriority(employee, priority);
    }

    /**
     * Get statistics for employee
     */
    @Transactional(readOnly = true)
    public GoalStatistics getEmployeeStatistics(String employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "employeeId", employeeId));

        long total = goalRepository.findByEmployeeOrderByCreatedAtDesc(employee).size();
        long completed = goalRepository.countByEmployeeAndStatus(employee, GoalStatus.COMPLETED);
        long inProgress = goalRepository.countByEmployeeAndStatus(employee, GoalStatus.IN_PROGRESS);
        long notStarted = goalRepository.countByEmployeeAndStatus(employee, GoalStatus.NOT_STARTED);
        long active = goalRepository.countActiveGoals(employee);

        return new GoalStatistics(total, completed, inProgress, notStarted, active);
    }

    /**
     * Convert Goal entity to DTO
     */
    @Transactional(readOnly = true)
    public GoalDTO convertToDTO(Goal goal) {
        GoalDTO dto = new GoalDTO();

        dto.setGoalId(goal.getGoalId());
        dto.setEmployeeId(goal.getEmployee().getEmployeeId());
        dto.setEmployeeName(goal.getEmployee().getFullName());

        if (goal.getEmployee().getDepartment() != null) {
            dto.setDepartmentName(goal.getEmployee().getDepartment().getDepartmentName());
        }

        if (goal.getEmployee().getDesignation() != null) {
            dto.setDesignationName(goal.getEmployee().getDesignation().getDesignationName());
        }

        dto.setGoalTitle(goal.getGoalTitle());
        dto.setGoalDescription(goal.getGoalDescription());
        dto.setCategory(goal.getCategory());
        dto.setDeadline(goal.getDeadline());
        dto.setPriority(goal.getPriority());
        dto.setProgress(goal.getProgress());
        dto.setStatus(goal.getStatus());
        dto.setManagerComments(goal.getManagerComments());
        dto.setCompletedAt(goal.getCompletedAt());
        dto.setCreatedAt(goal.getCreatedAt());
        dto.setUpdatedAt(goal.getUpdatedAt());

        if (goal.getEmployee().getManager() != null) {
            dto.setManagerId(goal.getEmployee().getManager().getEmployeeId());
            dto.setManagerName(goal.getEmployee().getManager().getFullName());
        }

        return dto;
    }

    /**
     * Inner class for goal statistics
     */
    public static class GoalStatistics {
        private final long totalGoals;
        private final long completedGoals;
        private final long inProgressGoals;
        private final long notStartedGoals;
        private final long activeGoals;

        public GoalStatistics(long totalGoals, long completedGoals, long inProgressGoals, 
                            long notStartedGoals, long activeGoals) {
            this.totalGoals = totalGoals;
            this.completedGoals = completedGoals;
            this.inProgressGoals = inProgressGoals;
            this.notStartedGoals = notStartedGoals;
            this.activeGoals = activeGoals;
        }

        public long getTotalGoals() { return totalGoals; }
        public long getCompletedGoals() { return completedGoals; }
        public long getInProgressGoals() { return inProgressGoals; }
        public long getNotStartedGoals() { return notStartedGoals; }
        public long getActiveGoals() { return activeGoals; }

        public double getCompletionRate() {
            return totalGoals > 0 ? (completedGoals * 100.0 / totalGoals) : 0.0;
        }
    }
}