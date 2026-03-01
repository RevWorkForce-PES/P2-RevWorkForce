package com.revature.revworkforce.dto;

public class HolidayStatisticsDTO {

    private long total;
    private long nationalCount;
    private long regionalCount;
    private long optionalCount;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getNationalCount() {
        return nationalCount;
    }

    public void setNationalCount(long nationalCount) {
        this.nationalCount = nationalCount;
    }

    public long getRegionalCount() {
        return regionalCount;
    }

    public void setRegionalCount(long regionalCount) {
        this.regionalCount = regionalCount;
    }

    public long getOptionalCount() {
        return optionalCount;
    }

    public void setOptionalCount(long optionalCount) {
        this.optionalCount = optionalCount;
    }
}