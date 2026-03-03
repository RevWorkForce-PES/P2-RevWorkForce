package com.revature.revworkforce.dto;

import java.time.LocalDateTime;

/**
 * DTO for Audit Log data transfer.
 * 
 * @author RevWorkForce Team
 */
public class AuditLogDTO {
    
    private Long auditId;
    
    private String performedBy;
    
    private String performedByName;
    
    private String action;
    
    private String tableName;
    
    private String recordId;
    
    private String oldValue;
    
    private String newValue;
    
    private String ipAddress;
    
    private String userAgent;
    
    private LocalDateTime performedAt;
    
    // Derived fields
    private String actionLabel;
    
    private String timeAgo;
    
    // Constructors
    public AuditLogDTO() {
    }
    
    // Getters and Setters
    public Long getAuditId() {
        return auditId;
    }
    
    public void setAuditId(Long auditId) {
        this.auditId = auditId;
    }
    
    public String getPerformedBy() {
        return performedBy;
    }
    
    public void setPerformedBy(String performedBy) {
        this.performedBy = performedBy;
    }
    
    public String getPerformedByName() {
        return performedByName;
    }
    
    public void setPerformedByName(String performedByName) {
        this.performedByName = performedByName;
    }
    
    public String getAction() {
        return action;
    }
    
    public void setAction(String action) {
        this.action = action;
        updateActionLabel();
    }
    
    public String getTableName() {
        return tableName;
    }
    
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    
    public String getRecordId() {
        return recordId;
    }
    
    public void setRecordId(String string) {
        this.recordId = string;
    }
    
    public String getOldValue() {
        return oldValue;
    }
    
    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }
    
    public String getNewValue() {
        return newValue;
    }
    
    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }
    
    public String getIpAddress() {
        return ipAddress;
    }
    
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    
    public String getUserAgent() {
        return userAgent;
    }
    
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
    
    public LocalDateTime getPerformedAt() {
        return performedAt;
    }
    
    public void setPerformedAt(LocalDateTime performedAt) {
        this.performedAt = performedAt;
        updateTimeAgo();
    }
    
    public String getActionLabel() {
        return actionLabel;
    }
    
    public void setActionLabel(String actionLabel) {
        this.actionLabel = actionLabel;
    }
    
    public String getTimeAgo() {
        return timeAgo;
    }
    
    public void setTimeAgo(String timeAgo) {
        this.timeAgo = timeAgo;
    }
    
    /**
     * Update action label for display.
     */
    private void updateActionLabel() {
        if (action == null) {
            return;
        }
        
        switch (action) {
            case "INSERT":
                this.actionLabel = "Created";
                break;
            case "UPDATE":
                this.actionLabel = "Updated";
                break;
            case "DELETE":
                this.actionLabel = "Deleted";
                break;
            case "LOGIN":
                this.actionLabel = "Logged In";
                break;
            case "LOGOUT":
                this.actionLabel = "Logged Out";
                break;
            case "APPROVE":
                this.actionLabel = "Approved";
                break;
            case "REJECT":
                this.actionLabel = "Rejected";
                break;
            default:
                this.actionLabel = action;
        }
    }
    
    /**
     * Calculate time ago.
     */
    private void updateTimeAgo() {
        if (performedAt == null) {
            return;
        }
        
        LocalDateTime now = LocalDateTime.now();
        long minutes = java.time.Duration.between(performedAt, now).toMinutes();
        
        if (minutes < 1) {
            this.timeAgo = "Just now";
        } else if (minutes < 60) {
            this.timeAgo = minutes + " minute" + (minutes > 1 ? "s" : "") + " ago";
        } else if (minutes < 1440) {
            long hours = minutes / 60;
            this.timeAgo = hours + " hour" + (hours > 1 ? "s" : "") + " ago";
        } else {
            long days = minutes / 1440;
            this.timeAgo = days + " day" + (days > 1 ? "s" : "") + " ago";
        }
    }
}