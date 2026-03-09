package com.revature.revworkforce.dto;

import com.revature.revworkforce.enums.GoalStatus;
import com.revature.revworkforce.enums.Priority;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class GoalDTOTest {

    private GoalDTO goal;

    @BeforeEach
    void setUp() {
        goal = new GoalDTO();
    }

    // =========================
    // Test isEditable()
    // =========================
    @Test
    void isEditable_NotStartedOrInProgress_ReturnsTrue() {
        goal.setStatus(GoalStatus.NOT_STARTED);
        assertTrue(goal.isEditable());

        goal.setStatus(GoalStatus.IN_PROGRESS);
        assertTrue(goal.isEditable());
    }

    @Test
    void isEditable_OtherStatus_ReturnsFalse() {
        goal.setStatus(GoalStatus.COMPLETED);
        assertFalse(goal.isEditable());

        goal.setStatus(GoalStatus.CANCELLED);
        assertFalse(goal.isEditable());

        goal.setStatus(GoalStatus.DEFERRED);
        assertFalse(goal.isEditable());
    }

    // =========================
    // Test isOverdue()
    // =========================
    @Test
    void isOverdue_DeadlinePastAndNotCompleted_ReturnsTrue() {
        goal.setDeadline(LocalDate.now().minusDays(1));
        goal.setStatus(GoalStatus.IN_PROGRESS);
        assertTrue(goal.isOverdue());
    }

    @Test
    void isOverdue_DeadlineFutureOrCompleted_ReturnsFalse() {
        goal.setDeadline(LocalDate.now().plusDays(1));
        goal.setStatus(GoalStatus.IN_PROGRESS);
        assertFalse(goal.isOverdue());

        goal.setDeadline(LocalDate.now().minusDays(1));
        goal.setStatus(GoalStatus.COMPLETED);
        assertFalse(goal.isOverdue());

        goal.setDeadline(null);
        goal.setStatus(GoalStatus.NOT_STARTED);
        assertFalse(goal.isOverdue());
    }

    // =========================
    // Test getProgressLabel()
    // =========================
    @Test
    void getProgressLabel_ReturnsCorrectLabels() {
        goal.setProgress(0);
        assertEquals("Not Started", goal.getProgressLabel());

        goal.setProgress(10);
        assertEquals("Just Started", goal.getProgressLabel());

        goal.setProgress(30);
        assertEquals("In Progress", goal.getProgressLabel());

        goal.setProgress(60);
        assertEquals("More Than Half", goal.getProgressLabel());

        goal.setProgress(90);
        assertEquals("Almost Done", goal.getProgressLabel());

        goal.setProgress(100);
        assertEquals("Completed", goal.getProgressLabel());
    }

    // =========================
    // Test getPriorityColor()
    // =========================
    @Test
    void getPriorityColor_ReturnsBootstrapColors() {
        goal.setPriority(Priority.URGENT);
        assertEquals("danger", goal.getPriorityColor());

        goal.setPriority(Priority.HIGH);
        assertEquals("warning", goal.getPriorityColor());

        goal.setPriority(Priority.MEDIUM);
        assertEquals("info", goal.getPriorityColor());

        goal.setPriority(Priority.LOW);
        assertEquals("success", goal.getPriorityColor());

        goal.setPriority(null);
        assertEquals("secondary", goal.getPriorityColor());
    }

    // =========================
    // Test getStatusColor()
    // =========================
    @Test
    void getStatusColor_ReturnsBootstrapColors() {
        goal.setStatus(GoalStatus.NOT_STARTED);
        assertEquals("secondary", goal.getStatusColor());

        goal.setStatus(GoalStatus.IN_PROGRESS);
        assertEquals("primary", goal.getStatusColor());

        goal.setStatus(GoalStatus.COMPLETED);
        assertEquals("success", goal.getStatusColor());

        goal.setStatus(GoalStatus.CANCELLED);
        assertEquals("danger", goal.getStatusColor());

        goal.setStatus(GoalStatus.DEFERRED);
        assertEquals("warning", goal.getStatusColor());

        goal.setStatus(null);
        assertEquals("secondary", goal.getStatusColor());
    }

    // =========================
    // Test getters/setters
    // =========================
    @Test
    void testSettersAndGetters() {
        goal.setGoalId(1L);
        assertEquals(1L, goal.getGoalId());

        goal.setGoalTitle("Goal 1");
        assertEquals("Goal 1", goal.getGoalTitle());

        goal.setGoalDescription("Description");
        assertEquals("Description", goal.getGoalDescription());

        goal.setCategory("Category");
        assertEquals("Category", goal.getCategory());

        LocalDate deadline = LocalDate.now().plusDays(5);
        goal.setDeadline(deadline);
        assertEquals(deadline, goal.getDeadline());

        goal.setProgress(50);
        assertEquals(50, goal.getProgress());

        goal.setManagerComments("Good");
        assertEquals("Good", goal.getManagerComments());

        goal.setEmployeeId("EMP001");
        assertEquals("EMP001", goal.getEmployeeId());
    }
}