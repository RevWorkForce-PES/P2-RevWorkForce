package com.revature.revworkforce.repository;

import com.revature.revworkforce.model.LeaveType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for LeaveType entity.
 * 
 * Provides CRUD operations and custom query methods for LeaveType.
 * 
 * @author RevWorkForce Team
 */
@Repository
public interface LeaveTypeRepository extends JpaRepository<LeaveType, Long> {
    
    /**
     * Find leave type by code.
     * 
     * @param leaveCode the leave code (CL, SL, PL, PRIV)
     * @return Optional containing the leave type if found
     */
    Optional<LeaveType> findByLeaveCode(String leaveCode);
    
    /**
     * Find leave type by name.
     * 
     * @param leaveName the leave name
     * @return Optional containing the leave type if found
     */
    Optional<LeaveType> findByLeaveName(String leaveName);
    
    /**
     * Find all active leave types.
     * 
     * @param isActive 'Y' for active, 'N' for inactive
     * @return list of leave types
     */
    List<LeaveType> findByIsActive(Character isActive);
    
    /**
     * Check if leave type exists by code.
     * 
     * @param leaveCode the leave code
     * @return true if exists
     */
    boolean existsByLeaveCode(String leaveCode);
    
    /**
     * Find all leave types ordered by name.
     * 
     * @return list of leave types
     */
    List<LeaveType> findAllByOrderByLeaveNameAsc();
}