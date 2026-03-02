package com.revature.revworkforce.dto;

public class GoalStatistics {

    private final long totalGoals;
    private final long completedGoals;
    private final long inProgressGoals;
    private final long notStartedGoals;
    private final long activeGoals;

    public GoalStatistics(long totalGoals,
                          long completedGoals,
                          long inProgressGoals,
                          long notStartedGoals,
                          long activeGoals) {
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
        return totalGoals > 0
                ? (completedGoals * 100.0 / totalGoals)
                : 0.0;
    }
}