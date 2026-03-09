package com.revature.revworkforce.enums;

/**
 * Enum representing priority levels for goals, tasks, and notifications.
 * 
 * <p>
 * Priority levels help in organizing and managing workload by importance.
 * Used in:
 * <ul>
 * <li>Goals - to indicate goal importance</li>
 * <li>Notifications - to indicate urgency</li>
 * <li>Announcements - to indicate importance</li>
 * </ul>
 * 
 * @author RevWorkForce Team
 * @version 1.0
 * @since 2024
 */
public enum Priority {

    /**
     * Low priority - can be addressed when time permits
     */
    LOW("Low", "Can be addressed when time permits", "success", 1),

    /**
     * Medium priority - should be addressed in normal timeframe
     */
    MEDIUM("Medium", "Should be addressed in normal timeframe", "warning", 2),

    /**
     * High priority - requires immediate attention
     */
    HIGH("High", "Requires immediate attention", "danger", 3),

    /**
     * Urgent priority - critical, must be addressed immediately
     */
    URGENT("Urgent", "Critical, must be addressed immediately", "dark", 4);

    private final String displayName;
    private final String description;
    private final String bootstrapClass;
    private final int weight;

    Priority(String displayName, String description, String bootstrapClass, int weight) {
        this.displayName = displayName;
        this.description = description;
        this.bootstrapClass = bootstrapClass;
        this.weight = weight;
    }

    public String getBootstrapClass() {
        return bootstrapClass;
    }

    public int getWeight() {
        return weight;
    }

    /**
     * Gets the user-friendly display name.
     * 
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Gets the detailed description.
     * 
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Converts a string value to Priority enum.
     * Case-insensitive matching.
     * 
     * @param value the string value to convert
     * @return the corresponding Priority, or null if not found
     */
    public static Priority fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        for (Priority priority : Priority.values()) {
            if (priority.name().equalsIgnoreCase(value) ||
                    priority.displayName.equalsIgnoreCase(value)) {
                return priority;
            }
        }
        return null;
    }

    /**
     * Gets the default priority level.
     * 
     * @return MEDIUM priority
     */
    public static Priority getDefaultPriority() {
        return MEDIUM;
    }
}