package com.revature.revworkforce.service.impl;

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
import com.revature.revworkforce.service.GoalService;
import com.revature.revworkforce.service.NotificationService;
import com.revature.revworkforce.service.AuditService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import com.revature.revworkforce.dto.GoalStatistics;

/**
 * Goal Service - P2 VERSION
 * Works with existing Goal entity and repository
 * 
 * @author RevWorkForce Team
 */
@Service
@Transactional
public class GoalServiceImpl implements GoalService {
    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    private final NotificationService notificationService;
    private final AuditService auditService;

    public GoalServiceImpl(GoalRepository goalRepository,
            EmployeeRepository employeeRepository,
            NotificationService notificationService,
            AuditService auditService) {
        this.goalRepository = goalRepository;
        this.employeeRepository = employeeRepository;
        this.notificationService = notificationService;
        this.auditService = auditService;
    }

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

        // Check max active goals (NOT_STARTED or IN_PROGRESS)
        long activeCount = goalRepository.countActiveGoals(employee);
        if (activeCount >= 10) {
            throw new ValidationException("Maximum of 10 active goals allowed per employee");
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

        Goal saved = goalRepository.save(goal);

        // Audit Logging
        auditService.createAuditLog(
                employeeId,
                "GOAL_CREATED",
                "GOALS",
                String.valueOf(saved.getGoalId()),
                null,
                saved.getGoalTitle(),
                null,
                null);

        return saved;
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

        Goal updated = goalRepository.save(goal);

        // Audit Logging
        auditService.createAuditLog(
                employeeId,
                "GOAL_UPDATED",
                "GOALS",
                goalId.toString(),
                null,
                updated.getGoalTitle(),
                null,
                null);

        return updated;
    }

    /**
     * P2: Cancel goal (employee cancels own goal)
     */
    @Override
    public Goal cancelGoal(Long goalId, String employeeId) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new ResourceNotFoundException("Goal", "goalId", goalId));

        if (!goal.getEmployee().getEmployeeId().equals(employeeId)) {
            throw new UnauthorizedException("You can only cancel your own goals");
        }

        if (goal.getStatus() == GoalStatus.COMPLETED) {
            throw new ValidationException("Cannot cancel a completed goal");
        }

        goal.setStatus(GoalStatus.CANCELLED);
        goal.setUpdatedAt(LocalDateTime.now());

        Goal saved = goalRepository.save(goal);

        // Audit Logging
        auditService.createAuditLog(
                employeeId,
                "GOAL_CANCELLED",
                "GOALS",
                goalId.toString(),
                null,
                "Goal cancelled: " + saved.getGoalTitle(),
                null,
                null);

        return saved;
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

        Goal saved = goalRepository.save(goal);

        // Audit Logging
        auditService.createAuditLog(
                employeeId,
                "GOAL_PROGRESS_UPDATED",
                "GOALS",
                goalId.toString(),
                null,
                "Progress: " + progress + "%, Status: " + saved.getStatus(),
                null,
                null);

        if (saved.getStatus() == GoalStatus.COMPLETED) {
            notificationService.createNotification(
                    saved.getEmployee(),
                    com.revature.revworkforce.enums.NotificationType.GOAL,
                    "Goal Completed 🎉",
                    "You have successfully completed: " + saved.getGoalTitle(),
                    com.revature.revworkforce.enums.NotificationPriority.NORMAL);
        }

        return saved;
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

        Goal saved = goalRepository.save(goal);

        // Audit Logging
        auditService.createAuditLog(
                managerId,
                "GOAL_COMMENT_ADDED",
                "GOALS",
                goalId.toString(),
                null,
                "Manager added comments",
                null,
                comments);

        notificationService.createNotification(
                saved.getEmployee(),
                com.revature.revworkforce.enums.NotificationType.GOAL,
                "Manager Comment on Goal",
                "Your manager added a comment on goal: " + saved.getGoalTitle(),
                com.revature.revworkforce.enums.NotificationPriority.NORMAL);

        return saved;
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

        // Audit Logging
        auditService.createAuditLog(
                employeeId,
                "GOAL_DELETED",
                "GOALS",
                goalId.toString(),
                null,
                "Goal deleted: " + goal.getGoalTitle(),
                null,
                null);
    }

    /**
     * Get goal by ID
     */
    @Override
    @Transactional(readOnly = true)
    public Goal getGoalById(Long goalId) {
        return goalRepository.findById(goalId)
                .orElseThrow(() -> new ResourceNotFoundException("Goal", "goalId", goalId));
    }

    @Override
    @Transactional(readOnly = true)
    public GoalDTO getGoalDTOById(Long goalId) {
        Goal goal = getGoalById(goalId);
        return convertToDTO(goal);
    }

    /**
     * P2: View all my goals with status
     */
    @Override
    @Transactional(readOnly = true)
    public List<GoalDTO> getEmployeeGoals(String employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "employeeId", employeeId));

        return goalRepository.findByEmployeeOrderByCreatedAtDesc(employee).stream()
                .map(this::convertToDTO)
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Get active goals (NOT_STARTED or IN_PROGRESS)
     */
    @Override
    @Transactional(readOnly = true)
    public List<GoalDTO> getActiveGoals(String employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "employeeId", employeeId));

        return goalRepository.findActiveGoals(employee).stream()
                .map(this::convertToDTO)
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Get goals by status
     */
    @Override
    @Transactional(readOnly = true)
    public List<GoalDTO> getGoalsByStatus(String employeeId, GoalStatus status) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "employeeId", employeeId));

        return goalRepository.findByEmployeeAndStatus(employee, status).stream()
                .map(this::convertToDTO)
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * P2: View team member goal progress (manager view)
     */
    @Override
    @Transactional(readOnly = true)
    public List<GoalDTO> getTeamGoals(String managerId) {
        return goalRepository.findTeamGoalsByManagerId(managerId).stream()
                .map(this::convertToDTO)
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Get active team goals
     */
    @Override
    @Transactional(readOnly = true)
    public List<GoalDTO> getTeamActiveGoals(String managerId) {
        return goalRepository.findTeamGoalsByManagerId(managerId).stream()
                .filter(g -> g.getStatus() == GoalStatus.NOT_STARTED || g.getStatus() == GoalStatus.IN_PROGRESS)
                .map(this::convertToDTO)
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Get upcoming deadlines (next 30 days)
     */
    @Override
    @Transactional(readOnly = true)
    public List<GoalDTO> getUpcomingDeadlines(String employeeId, int days) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "employeeId", employeeId));

        LocalDate today = LocalDate.now();
        LocalDate futureDate = today.plusDays(days);

        return goalRepository.findUpcomingDeadlines(employee, today, futureDate).stream()
                .map(this::convertToDTO)
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Get overdue goals
     */
    @Transactional(readOnly = true)
    public List<GoalDTO> getOverdueGoals(String employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "employeeId", employeeId));

        LocalDate today = LocalDate.now();

        return goalRepository.findOverdueGoals(employee, today).stream()
                .map(this::convertToDTO)
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Get goals by priority
     */
    @Transactional(readOnly = true)
    public List<GoalDTO> getGoalsByPriority(String employeeId, Priority priority) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "employeeId", employeeId));

        return goalRepository.findByEmployeeAndPriority(employee, priority).stream()
                .map(this::convertToDTO)
                .collect(java.util.stream.Collectors.toList());
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

}