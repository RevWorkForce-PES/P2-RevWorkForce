package com.revature.revworkforce.dto;

public class ReportDTO {

    private long totalLogins;
    private long totalCrudOperations;
    private long totalApprovals;
    private long totalRejections;
    private String mostActiveUser;
    private String dateRange;

    public ReportDTO(long totalLogins, long totalCrudOperations,
                     long totalApprovals, long totalRejections,
                     String mostActiveUser, String dateRange) {
        this.totalLogins = totalLogins;
        this.totalCrudOperations = totalCrudOperations;
        this.totalApprovals = totalApprovals;
        this.totalRejections = totalRejections;
        this.mostActiveUser = mostActiveUser;
        this.dateRange = dateRange;
    }
}