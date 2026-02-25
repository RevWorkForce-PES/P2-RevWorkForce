package com.revature.revworkforce.repository;

import com.revature.revworkforce.model.Designation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Designation entity.
 * 
 * Provides CRUD operations and custom query methods for Designation.
 * 
 * @author RevWorkForce Team
 */
@Repository
public interface DesignationRepository extends JpaRepository<Designation, Long> {
    
    /**
     * Find designation by name.
     * 
     * @param designationName the designation name
     * @return Optional containing the designation if found
     */
    Optional<Designation> findByDesignationName(String designationName);
    
    /**
     * Find all active designations.
     * 
     * @param isActive 'Y' for active, 'N' for inactive
     * @return list of designations
     */
    List<Designation> findByIsActive(Character isActive);
    
    /**
     * Find designations by level.
     * 
     * @param designationLevel the designation level (Junior, Mid, Senior, etc.)
     * @return list of designations
     */
    List<Designation> findByDesignationLevel(String designationLevel);
    
    /**
     * Check if designation exists by name.
     * 
     * @param designationName the designation name
     * @return true if exists
     */
    boolean existsByDesignationName(String designationName);
    
    /**
     * Find all designations ordered by name.
     * 
     * @return list of designations
     */
    List<Designation> findAllByOrderByDesignationNameAsc();
}