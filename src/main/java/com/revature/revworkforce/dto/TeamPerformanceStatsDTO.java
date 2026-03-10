package com.revature.revworkforce.dto;

import java.math.BigDecimal;
import java.util.Map;

/**
 * DTO for Team Performance Statistics
 * 
 * @author RevWorkForce Team
 */
public class TeamPerformanceStatsDTO {

    private BigDecimal overallRating;
    private Map<String, Long> ratingDistribution;
    private long totalReviews;
    private long completedReviews;

    public TeamPerformanceStatsDTO() {
    }

    public TeamPerformanceStatsDTO(BigDecimal overallRating, Map<String, Long> ratingDistribution, long totalReviews,
            long completedReviews) {
        this.overallRating = overallRating;
        this.ratingDistribution = ratingDistribution;
        this.totalReviews = totalReviews;
        this.completedReviews = completedReviews;
    }

    public BigDecimal getOverallRating() {
        return overallRating;
    }

    public void setOverallRating(BigDecimal overallRating) {
        this.overallRating = overallRating;
    }

    public Map<String, Long> getRatingDistribution() {
        return ratingDistribution;
    }

    public void setRatingDistribution(Map<String, Long> ratingDistribution) {
        this.ratingDistribution = ratingDistribution;
    }

    public long getTotalReviews() {
        return totalReviews;
    }

    public void setTotalReviews(long totalReviews) {
        this.totalReviews = totalReviews;
    }

    public long getCompletedReviews() {
        return completedReviews;
    }

    public void setCompletedReviews(long completedReviews) {
        this.completedReviews = completedReviews;
    }
}
