package com.revature.revworkforce.dto;

import java.time.LocalDateTime;

public class AuditLogDTO {

    private Long auditId;
    private String employeeId;
    private String action;
    private String tableName;
    private String recordId;
    private String fieldName;
    private String oldValue;
    private String newValue;
    private String description;
    private String ipAddress;
    private String userAgent;
    private LocalDateTime createdAt;

    public AuditLogDTO() {}

    public AuditLogDTO(Long auditId, String employeeId, String action,
                       String tableName, String recordId, String fieldName,
                       String oldValue, String newValue,
                       String description, String ipAddress,
                       String userAgent, LocalDateTime createdAt) {

        this.auditId = auditId;
        this.employeeId = employeeId;
        this.action = action;
        this.tableName = tableName;
        this.recordId = recordId;
        this.fieldName = fieldName;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.description = description;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.createdAt = createdAt;
    }

	

	public Long getAuditId() {
		return auditId;
	}

	public void setAuditId(Long auditId) {
		this.auditId = auditId;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
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

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return "AuditLogDTO [auditId=" + auditId + ", employeeId=" + employeeId + ", action=" + action + ", tableName="
				+ tableName + ", recordId=" + recordId + ", fieldName=" + fieldName + ", oldValue=" + oldValue
				+ ", newValue=" + newValue + ", description=" + description + ", ipAddress=" + ipAddress
				+ ", userAgent=" + userAgent + ", createdAt=" + createdAt + "]";
	}

    
    
}