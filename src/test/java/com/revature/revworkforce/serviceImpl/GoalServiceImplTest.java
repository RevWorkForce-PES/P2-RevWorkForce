package com.revature.revworkforce.serviceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

import com.revature.revworkforce.enums.GoalStatus;
import com.revature.revworkforce.enums.Priority;
import com.revature.revworkforce.exception.ResourceNotFoundException;
import com.revature.revworkforce.exception.UnauthorizedException;
import com.revature.revworkforce.exception.ValidationException;
import com.revature.revworkforce.model.Employee;
import com.revature.revworkforce.model.Goal;
import com.revature.revworkforce.repository.EmployeeRepository;
import com.revature.revworkforce.repository.GoalRepository;
import com.revature.revworkforce.service.NotificationService;
import com.revature.revworkforce.service.impl.GoalServiceImpl;
import com.revature.revworkforce.service.AuditService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    void setup() {

        manager = new Employee();
        manager.setEmployeeId("MGR001");
        manager.setFirstName("Manager");

        employee = new Employee();
        employee.setEmployeeId("EMP001");
        employee.setFirstName("Employee");
        employee.setManager(manager);

        goal = new Goal();
        goal.setGoalId(1L);
        goal.setEmployee(employee);
        goal.setGoalTitle("Learn Spring Boot");
        goal.setStatus(GoalStatus.NOT_STARTED);
        goal.setProgress(0);
    }

    // =====================================================
    // CREATE GOAL
    // =====================================================

    @Test
    void createGoal_Success() {

        when(employeeRepository.findById("EMP001"))
                .thenReturn(Optional.of(employee));

        when(goalRepository.save(any(Goal.class)))
                .thenReturn(goal);

        Goal result = goalService.createGoal(
                "EMP001",
                "Learn Spring",
                "Master Spring Boot",
                "Technical",
                LocalDate.now().plusDays(10),
                Priority.HIGH
        );

        assertNotNull(result);
        verify(goalRepository).save(any(Goal.class));
    }

    @Test
    void createGoal_PastDeadline_ThrowsException() {

        when(employeeRepository.findById("EMP001"))
                .thenReturn(Optional.of(employee));

        assertThrows(ValidationException.class, () ->
                goalService.createGoal(
                        "EMP001",
                        "Goal",
                        "Desc",
                        "Tech",
                        LocalDate.now().minusDays(1),
                        Priority.MEDIUM));
    }

    // =====================================================
    // UPDATE GOAL
    // =====================================================

    @Test
    void updateGoal_Success() {

        when(goalRepository.findById(1L))
                .thenReturn(Optional.of(goal));

        when(goalRepository.save(any()))
                .thenReturn(goal);

        Goal result = goalService.updateGoal(
                1L,
                "EMP001",
                "Updated Goal",
                "Updated Desc",
                "Tech",
                LocalDate.now().plusDays(5),
                Priority.HIGH
        );

        assertNotNull(result);
        verify(goalRepository).save(goal);
    }

    @Test
    void updateGoal_Unauthorized_ThrowsException() {

        when(goalRepository.findById(1L))
                .thenReturn(Optional.of(goal));

        assertThrows(UnauthorizedException.class, () ->
                goalService.updateGoal(
                        1L,
                        "EMP999",
                        "Goal",
                        "Desc",
                        "Tech",
                        LocalDate.now().plusDays(10),
                        Priority.HIGH));
    }

    // =====================================================
    // UPDATE PROGRESS
    // =====================================================

    @Test
    void updateProgress_Success() {

        when(goalRepository.findById(1L))
                .thenReturn(Optional.of(goal));

        when(goalRepository.save(any()))
                .thenReturn(goal);

        Goal result = goalService.updateProgress(1L, "EMP001", 50);

        assertNotNull(result);
        verify(goalRepository).save(goal);
    }

    @Test
    void updateProgress_InvalidProgress() {

        when(goalRepository.findById(1L))
                .thenReturn(Optional.of(goal));

        assertThrows(ValidationException.class, () ->
                goalService.updateProgress(1L, "EMP001", 150));
    }

    // =====================================================
    // MANAGER COMMENT
    // =====================================================

    @Test
    void addManagerComments_Success() {

        when(goalRepository.findById(1L))
                .thenReturn(Optional.of(goal));

        when(employeeRepository.findById("MGR001"))
                .thenReturn(Optional.of(manager));

        when(goalRepository.save(any()))
                .thenReturn(goal);

        Goal result = goalService.addManagerComments(
                1L,
                "MGR001",
                "Good progress"
        );

        assertNotNull(result);
        verify(notificationService).createNotification(any(), any(), any(), any(), any());
    }

    @Test
    void addManagerComments_Unauthorized() {

        Employee otherManager = new Employee();
        otherManager.setEmployeeId("MGR999");

        when(goalRepository.findById(1L))
                .thenReturn(Optional.of(goal));

        when(employeeRepository.findById("MGR999"))
                .thenReturn(Optional.of(otherManager));

        assertThrows(UnauthorizedException.class, () ->
                goalService.addManagerComments(
                        1L,
                        "MGR999",
                        "Comment"));
    }

    // =====================================================
    // DELETE GOAL
    // =====================================================

    @Test
    void deleteGoal_Success() {

        when(goalRepository.findById(1L))
                .thenReturn(Optional.of(goal));

        goalService.deleteGoal(1L, "EMP001");

        verify(goalRepository).delete(goal);
    }

    @Test
    void deleteGoal_CompletedGoal_ThrowsException() {

        goal.setStatus(GoalStatus.COMPLETED);

        when(goalRepository.findById(1L))
                .thenReturn(Optional.of(goal));

        assertThrows(ValidationException.class,
                () -> goalService.deleteGoal(1L, "EMP001"));
    }

    // =====================================================
    // GET GOAL
    // =====================================================

    @Test
    void getGoalById_Success() {

        when(goalRepository.findById(1L))
                .thenReturn(Optional.of(goal));

        Goal result = goalService.getGoalById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getGoalId());
    }

    @Test
    void getGoalById_NotFound() {

        when(goalRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> goalService.getGoalById(1L));
    }

    // =====================================================
    // GET EMPLOYEE GOALS
    // =====================================================

    @Test
    void getEmployeeGoals_Success() {

        when(employeeRepository.findById("EMP001"))
                .thenReturn(Optional.of(employee));

        when(goalRepository.findByEmployeeOrderByCreatedAtDesc(employee))
                .thenReturn(List.of(goal));

        var result = goalService.getEmployeeGoals("EMP001");

        assertEquals(1, result.size());
    }

}