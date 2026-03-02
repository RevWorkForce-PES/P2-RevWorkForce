package com.revature.revworkforce.service;

import com.revature.revworkforce.dto.GoalDTO;
import com.revature.revworkforce.dto.GoalStatistics;
import com.revature.revworkforce.enums.GoalStatus;
import com.revature.revworkforce.enums.Priority;
import com.revature.revworkforce.model.Goal;

import java.time.LocalDate;
import java.util.List;

public interface GoalService {

    Goal createGoal(String employeeId, String goalTitle, String goalDescription,
                    String category, LocalDate deadline, Priority priority);

    Goal updateGoal(Long goalId, String employeeId, String goalTitle,
                    String goalDescription, String category,
                    LocalDate deadline, Priority priority);

    Goal updateProgress(Long goalId, String employeeId, Integer progress);

    Goal addManagerComments(Long goalId, String managerId, String comments);

    void deleteGoal(Long goalId, String employeeId);

    Goal getGoalById(Long goalId);

    List<Goal> getEmployeeGoals(String employeeId);

    List<Goal> getActiveGoals(String employeeId);

    List<Goal> getGoalsByStatus(String employeeId, GoalStatus status);

    List<Goal> getTeamGoals(String managerId);

    List<Goal> getUpcomingDeadlines(String employeeId, int days);

    List<Goal> getOverdueGoals(String employeeId);

    List<Goal> getGoalsByPriority(String employeeId, Priority priority);

    GoalStatistics getEmployeeStatistics(String employeeId);

    GoalDTO convertToDTO(Goal goal);
}