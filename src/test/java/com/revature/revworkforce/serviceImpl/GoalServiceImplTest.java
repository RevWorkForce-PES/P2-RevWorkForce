package com.revature.revworkforce.serviceImpl;

import com.revature.revworkforce.dto.GoalDTO;
import com.revature.revworkforce.dto.GoalStatistics;
import com.revature.revworkforce.enums.GoalStatus;
import com.revature.revworkforce.enums.Priority;
import com.revature.revworkforce.exception.ResourceNotFoundException;
import com.revature.revworkforce.exception.UnauthorizedException;
import com.revature.revworkforce.exception.ValidationException;
import com.revature.revworkforce.model.Employee;
import com.revature.revworkforce.model.Goal;
import com.revature.revworkforce.repository.EmployeeRepository;
import com.revature.revworkforce.repository.GoalRepository;
import com.revature.revworkforce.service.AuditService;
import com.revature.revworkforce.service.NotificationService;
import com.revature.revworkforce.service.impl.GoalServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoalServiceImplTest {

    @Mock
    private GoalRepository goalRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private NotificationService notificationService;

    @Mock
    private AuditService auditService;

    @InjectMocks
    private GoalServiceImpl goalService;

    private Employee employee;
    private Employee manager;
    private Goal goal;

    @BeforeEach
    void setUp() {
        manager = new Employee();
        manager.setEmployeeId("MGR001");
        manager.setFirstName("Jane");

        employee = new Employee();
        employee.setEmployeeId("EMP001");
        employee.setFirstName("John");
        employee.setManager(manager);

        goal = new Goal();
        goal.setGoalId(1L);
        goal.setEmployee(employee);
        goal.setGoalTitle("Test Goal");
        goal.setStatus(GoalStatus.NOT_STARTED);
        goal.setProgress(0);
    }

    // ==========================================
    // CREATE GOAL
    // ==========================================

    @Test
    void createGoal_Success() {
        when(employeeRepository.findById("EMP001")).thenReturn(Optional.of(employee));
        when(goalRepository.countActiveGoals(employee)).thenReturn(5L);
        when(goalRepository.save(any(Goal.class))).thenReturn(goal);

        Goal result = goalService.createGoal(
                "EMP001", "Title", "Description", "Category",
                LocalDate.now().plusDays(5), Priority.MEDIUM);

        assertThat(result).isNotNull();
        verify(goalRepository).save(any(Goal.class));
        verify(auditService).createAuditLog(any(), any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    void createGoal_InvalidDeadline() {
        when(employeeRepository.findById("EMP001")).thenReturn(Optional.of(employee));

        assertThrows(ValidationException.class,
                () -> goalService.createGoal("EMP001", "Title", "Description", "Category",
                        LocalDate.now().minusDays(1), Priority.MEDIUM));
    }

    @Test
    void createGoal_EmployeeNotFound() {
        when(employeeRepository.findById("UNKNOWN")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> goalService.createGoal("UNKNOWN", "Title", "Description", "Category",
                        LocalDate.now().plusDays(1), Priority.MEDIUM));
    }

    @Test
    void createGoal_MaxActiveGoalsReached() {
        when(employeeRepository.findById("EMP001")).thenReturn(Optional.of(employee));
        when(goalRepository.countActiveGoals(employee)).thenReturn(10L);

        assertThrows(ValidationException.class,
                () -> goalService.createGoal("EMP001", "Title", "Description", "Category",
                        LocalDate.now().plusDays(1), Priority.MEDIUM));
    }

    // ==========================================
    // UPDATE GOAL
    // ==========================================

    @Test
    void updateGoal_Success() {
        when(goalRepository.findById(1L)).thenReturn(Optional.of(goal));
        when(goalRepository.save(any(Goal.class))).thenReturn(goal);

        Goal result = goalService.updateGoal(
                1L, "EMP001", "Updated", "Desc", "Category",
                LocalDate.now().plusDays(5), Priority.HIGH);

        assertThat(result.getGoalTitle()).isEqualTo("Updated");
        verify(goalRepository).save(any(Goal.class));
    }

    @Test
    void updateGoal_Unauthorized() {
        when(goalRepository.findById(1L)).thenReturn(Optional.of(goal));

        assertThrows(UnauthorizedException.class, () -> goalService.updateGoal(1L, "EMP999", "Title", "Desc", "Cat",
                LocalDate.now().plusDays(5), Priority.HIGH));
    }

    @Test
    void updateGoal_InvalidStatus() {
        goal.setStatus(GoalStatus.COMPLETED);
        when(goalRepository.findById(1L)).thenReturn(Optional.of(goal));

        assertThrows(ValidationException.class, () -> goalService.updateGoal(1L, "EMP001", "Title", "Desc", "Cat",
                LocalDate.now().plusDays(5), Priority.HIGH));
    }

    @Test
    void updateGoal_InvalidDeadline() {
        when(goalRepository.findById(1L)).thenReturn(Optional.of(goal));

        assertThrows(ValidationException.class, () -> goalService.updateGoal(1L, "EMP001", "Title", "Desc", "Cat",
                LocalDate.now().minusDays(5), Priority.HIGH));
    }

    // ==========================================
    // CANCEL GOAL
    // ==========================================

    @Test
    void cancelGoal_Success() {
        when(goalRepository.findById(1L)).thenReturn(Optional.of(goal));
        when(goalRepository.save(any(Goal.class))).thenReturn(goal);

        Goal result = goalService.cancelGoal(1L, "EMP001");

        assertThat(result.getStatus()).isEqualTo(GoalStatus.CANCELLED);
    }

    @Test
    void cancelGoal_Unauthorized() {
        when(goalRepository.findById(1L)).thenReturn(Optional.of(goal));
        assertThrows(UnauthorizedException.class, () -> goalService.cancelGoal(1L, "EMP999"));
    }

    @Test
    void cancelGoal_AlreadyCompleted() {
        goal.setStatus(GoalStatus.COMPLETED);
        when(goalRepository.findById(1L)).thenReturn(Optional.of(goal));
        assertThrows(ValidationException.class, () -> goalService.cancelGoal(1L, "EMP001"));
    }

    // ==========================================
    // UPDATE PROGRESS
    // ==========================================

    @Test
    void updateProgress_Success_InProgress() {
        when(goalRepository.findById(1L)).thenReturn(Optional.of(goal));
        when(goalRepository.save(any(Goal.class))).thenReturn(goal);

        Goal result = goalService.updateProgress(1L, "EMP001", 50);

        assertThat(result.getProgress()).isEqualTo(50);
        assertThat(result.getStatus()).isEqualTo(GoalStatus.IN_PROGRESS);
    }

    @Test
    void updateProgress_Completed() {
        when(goalRepository.findById(1L)).thenReturn(Optional.of(goal));
        when(goalRepository.save(any(Goal.class))).thenReturn(goal);

        Goal result = goalService.updateProgress(1L, "EMP001", 100);

        assertThat(result.getStatus()).isEqualTo(GoalStatus.COMPLETED);
        verify(notificationService).createNotification(any(), any(), any(), any(), any());
    }

    @Test
    void updateProgress_InvalidAmount() {
        when(goalRepository.findById(1L)).thenReturn(Optional.of(goal));
        assertThrows(ValidationException.class, () -> goalService.updateProgress(1L, "EMP001", 150));
        assertThrows(ValidationException.class, () -> goalService.updateProgress(1L, "EMP001", -10));
    }

    @Test
    void updateProgress_Unauthorized() {
        when(goalRepository.findById(1L)).thenReturn(Optional.of(goal));
        assertThrows(UnauthorizedException.class, () -> goalService.updateProgress(1L, "EMP999", 50));
    }

    // ==========================================
    // MANAGER COMMENTS
    // ==========================================

    @Test
    void addManagerComments_Success() {
        when(goalRepository.findById(1L)).thenReturn(Optional.of(goal));
        when(employeeRepository.findById("MGR001")).thenReturn(Optional.of(manager));
        when(goalRepository.save(any(Goal.class))).thenReturn(goal);

        Goal result = goalService.addManagerComments(1L, "MGR001", "Good progress");

        assertThat(result.getManagerComments()).isEqualTo("Good progress");
        verify(notificationService).createNotification(any(), any(), any(), any(), any());
    }

    @Test
    void addManagerComments_Unauthorized() {
        Employee otherManager = new Employee();
        otherManager.setEmployeeId("MGR002");
        when(goalRepository.findById(1L)).thenReturn(Optional.of(goal));
        when(employeeRepository.findById("MGR002")).thenReturn(Optional.of(otherManager));

        assertThrows(UnauthorizedException.class, () -> goalService.addManagerComments(1L, "MGR002", "Comment"));
    }

    // ==========================================
    // DELETE GOAL
    // ==========================================

    @Test
    void deleteGoal_Success() {
        when(goalRepository.findById(1L)).thenReturn(Optional.of(goal));
        goalService.deleteGoal(1L, "EMP001");
        verify(goalRepository).delete(goal);
    }

    @Test
    void deleteGoal_Unauthorized() {
        when(goalRepository.findById(1L)).thenReturn(Optional.of(goal));
        assertThrows(UnauthorizedException.class, () -> goalService.deleteGoal(1L, "EMP002"));
    }

    @Test
    void deleteGoal_AlreadyCompleted() {
        goal.setStatus(GoalStatus.COMPLETED);
        when(goalRepository.findById(1L)).thenReturn(Optional.of(goal));
        assertThrows(ValidationException.class, () -> goalService.deleteGoal(1L, "EMP001"));
    }

    // ==========================================
    // RETRIEVAL & STATISTICS
    // ==========================================

    @Test
    void getEmployeeGoals_Success() {
        when(employeeRepository.findById("EMP001")).thenReturn(Optional.of(employee));
        when(goalRepository.findByEmployeeOrderByCreatedAtDesc(employee)).thenReturn(Arrays.asList(goal));
        List<GoalDTO> result = goalService.getEmployeeGoals("EMP001");
        assertThat(result).hasSize(1);
    }

    @Test
    void getActiveGoals_Success() {
        when(employeeRepository.findById("EMP001")).thenReturn(Optional.of(employee));
        when(goalRepository.findActiveGoals(employee)).thenReturn(Arrays.asList(goal));
        List<GoalDTO> result = goalService.getActiveGoals("EMP001");
        assertThat(result).hasSize(1);
    }

    @Test
    void getGoalsByStatus_Success() {
        when(employeeRepository.findById("EMP001")).thenReturn(Optional.of(employee));
        when(goalRepository.findByEmployeeAndStatus(employee, GoalStatus.NOT_STARTED)).thenReturn(Arrays.asList(goal));
        List<GoalDTO> result = goalService.getGoalsByStatus("EMP001", GoalStatus.NOT_STARTED);
        assertThat(result).hasSize(1);
    }

    @Test
    void getTeamGoals_Success() {
        when(goalRepository.findTeamGoalsByManagerId("MGR001")).thenReturn(Arrays.asList(goal));
        List<GoalDTO> result = goalService.getTeamGoals("MGR001");
        assertThat(result).hasSize(1);
    }

    @Test
    void getTeamActiveGoals_Success() {
        when(goalRepository.findTeamGoalsByManagerId("MGR001")).thenReturn(Arrays.asList(goal));
        List<GoalDTO> result = goalService.getTeamActiveGoals("MGR001");
        assertThat(result).hasSize(1);
    }

    @Test
    void getUpcomingDeadlines_Success() {
        when(employeeRepository.findById("EMP001")).thenReturn(Optional.of(employee));
        when(goalRepository.findUpcomingDeadlines(eq(employee), any(), any())).thenReturn(Arrays.asList(goal));
        List<GoalDTO> result = goalService.getUpcomingDeadlines("EMP001", 30);
        assertThat(result).hasSize(1);
    }

    @Test
    void getOverdueGoals_Success() {
        when(employeeRepository.findById("EMP001")).thenReturn(Optional.of(employee));
        when(goalRepository.findOverdueGoals(eq(employee), any())).thenReturn(Arrays.asList(goal));
        List<GoalDTO> result = goalService.getOverdueGoals("EMP001");
        assertThat(result).hasSize(1);
    }

    @Test
    void getGoalsByPriority_Success() {
        when(employeeRepository.findById("EMP001")).thenReturn(Optional.of(employee));
        when(goalRepository.findByEmployeeAndPriority(employee, Priority.HIGH)).thenReturn(Arrays.asList(goal));
        List<GoalDTO> result = goalService.getGoalsByPriority("EMP001", Priority.HIGH);
        assertThat(result).hasSize(1);
    }

    @Test
    void getEmployeeStatistics_Success() {
        when(employeeRepository.findById("EMP001")).thenReturn(Optional.of(employee));
        when(goalRepository.findByEmployeeOrderByCreatedAtDesc(employee)).thenReturn(Arrays.asList(goal));
        when(goalRepository.countByEmployeeAndStatus(employee, GoalStatus.COMPLETED)).thenReturn(0L);
        when(goalRepository.countByEmployeeAndStatus(employee, GoalStatus.IN_PROGRESS)).thenReturn(0L);
        when(goalRepository.countByEmployeeAndStatus(employee, GoalStatus.NOT_STARTED)).thenReturn(1L);
        when(goalRepository.countActiveGoals(employee)).thenReturn(1L);

        GoalStatistics stats = goalService.getEmployeeStatistics("EMP001");

        assertThat(stats.getTotalGoals()).isEqualTo(1);
        assertThat(stats.getNotStartedGoals()).isEqualTo(1);
    }

    @Test
    void getTeamStatistics_Success() {
        when(goalRepository.findTeamGoalsByManagerId("MGR001")).thenReturn(Arrays.asList(goal));
        GoalStatistics stats = goalService.getTeamStatistics("MGR001");
        assertThat(stats.getTotalGoals()).isEqualTo(1);
        assertThat(stats.getNotStartedGoals()).isEqualTo(1);
    }

    @Test
    void getGoalDTOById_Success() {
        when(goalRepository.findById(1L)).thenReturn(Optional.of(goal));
        GoalDTO result = goalService.getGoalDTOById(1L);
        assertThat(result.getGoalId()).isEqualTo(1L);
    }
}