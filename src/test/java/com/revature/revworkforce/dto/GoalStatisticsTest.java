package com.revature.revworkforce.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GoalStatisticsTest {

    @Test
    void testGetters() {
        GoalStatistics stats = new GoalStatistics(10, 4, 3, 2, 5);

        assertEquals(10, stats.getTotalGoals());
        assertEquals(4, stats.getCompletedGoals());
        assertEquals(3, stats.getInProgressGoals());
        assertEquals(2, stats.getNotStartedGoals());
        assertEquals(5, stats.getActiveGoals());
    }

    @Test
    void testCompletionRate_Normal() {
        GoalStatistics stats = new GoalStatistics(10, 4, 3, 2, 5);
        // 4/10 * 100 = 40.0
        assertEquals(40.0, stats.getCompletionRate());
    }

    @Test
    void testCompletionRate_ZeroTotal() {
        GoalStatistics stats = new GoalStatistics(0, 0, 0, 0, 0);
        assertEquals(0.0, stats.getCompletionRate());
    }

    @Test
    void testCompletionRate_FullCompletion() {
        GoalStatistics stats = new GoalStatistics(5, 5, 0, 0, 5);
        assertEquals(100.0, stats.getCompletionRate());
    }
}