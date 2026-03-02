package com.revature.revworkforce.repository;

import com.revature.revworkforce.enums.ReviewStatus;
import com.revature.revworkforce.model.Employee;
import com.revature.revworkforce.model.PerformanceReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for PerformanceReview entity.
 * 
 * Provides CRUD operations and custom query methods for PerformanceReview.
 * 
 * @author RevWorkForce Team
 */
@Repository
public interface PerformanceReviewRepository extends JpaRepository<PerformanceReview, Long> {
    
    /**
     * Find all performance reviews by employee.
     * 
     * @param employee the employee
     * @return list of performance reviews
     */
    List<PerformanceReview> findByEmployeeOrderByReviewYearDesc(Employee employee);
    
    /**
     * Find performance review by employee and year.
     * 
     * @param employee the employee
     * @param reviewYear the review year
     * @return Optional containing the performance review if found
     */
    Optional<PerformanceReview> findByEmployeeAndReviewYear(Employee employee, Integer reviewYear);
    
    /**
     * Find all performance reviews by status.
     * 
     * @param status the review status
     * @return list of performance reviews
     */
    List<PerformanceReview> findByStatus(ReviewStatus status);
    
    /**
     * Find all performance reviews by employee and status.
     * 
     * @param employee the employee
     * @param status the review status
     * @return list of performance reviews
     */
    List<PerformanceReview> findByEmployeeAndStatus(Employee employee, ReviewStatus status);
    
    /**
     * Find pending reviews for manager.
     * (Reviews submitted by employees reporting to the manager)
     * 
     * @param managerId the manager's employee ID
     * @param status the review status (typically SUBMITTED)
     * @return list of pending performance reviews
     */
    @Query("SELECT pr FROM PerformanceReview pr WHERE pr.employee.manager.employeeId = :managerId AND pr.status = :status")
    List<PerformanceReview> findPendingReviewsByManagerId(@Param("managerId") String managerId, 
                                                           @Param("status") ReviewStatus status);
    
    /**
     * Find all reviews for team members (employees reporting to manager).
     * 
     * @param managerId the manager's employee ID
     * @return list of performance reviews
     */
    @Query("SELECT pr FROM PerformanceReview pr WHERE pr.employee.manager.employeeId = :managerId ORDER BY pr.reviewYear DESC")
    List<PerformanceReview> findTeamReviewsByManagerId(@Param("managerId") String managerId);
    
    /**
     * Find reviews by year.
     * 
     * @param reviewYear the review year
     * @return list of performance reviews
     */
    List<PerformanceReview> findByReviewYear(Integer reviewYear);
    
    /**
     * Find reviews by year and status.
     * 
     * @param reviewYear the review year
     * @param status the review status
     * @return list of performance reviews
     */
    List<PerformanceReview> findByReviewYearAndStatus(Integer reviewYear, ReviewStatus status);
    
    /**
     * Check if employee has review for a specific year.
     * 
     * @param employee the employee
     * @param reviewYear the review year
     * @return true if exists
     */
    boolean existsByEmployeeAndReviewYear(Employee employee, Integer reviewYear);
    
    /**
     * Count reviews by status.
     * 
     * @param status the review status
     * @return count of reviews
     */
    long countByStatus(ReviewStatus status);
}
