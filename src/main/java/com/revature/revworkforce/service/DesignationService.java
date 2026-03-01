package com.revature.revworkforce.service;

import com.revature.revworkforce.dto.DesignationDTO;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service interface for Designation operations.
 * 
 * Provides methods for CRUD operations, salary validation, and business logic
 * related to Designations.
 * 
 * @author RevWorkForce Team
 */
public interface DesignationService {

    /**
     * Creates a new designation.
     * 
     * @param designationDTO the designation data
     * @return the created designation
     */
    DesignationDTO createDesignation(DesignationDTO designationDTO);

    /**
     * Updates an existing designation.
     * 
     * @param designationId  the ID of the designation to update
     * @param designationDTO the updated designation data
     * @return the updated designation
     */
    DesignationDTO updateDesignation(Long designationId, DesignationDTO designationDTO);

    /**
     * Retrieves a designation by its ID.
     * 
     * @param designationId the designation ID
     * @return the designation
     */
    DesignationDTO getDesignationById(Long designationId);

    /**
     * Retrieves a designation by its name.
     * 
     * @param designationName the designation name
     * @return the designation
     */
    DesignationDTO getDesignationByName(String designationName);

    /**
     * Retrieves all designations.
     * 
     * @return list of all designations
     */
    List<DesignationDTO> getAllDesignations();

    /**
     * Retrieves all active designations.
     * 
     * @return list of active designations
     */
    List<DesignationDTO> getActiveDesignations();

    /**
     * Retrieves designations by level.
     * 
     * @param designationLevel the level
     * @return list of designations
     */
    List<DesignationDTO> getDesignationsByLevel(String designationLevel);

    /**
     * Deletes (soft deletes or deactivates) a designation by its ID.
     * 
     * @param designationId the designation ID
     */
    void deleteDesignation(Long designationId);

    /**
     * Checks if a designation exists by its name.
     * 
     * @param designationName the designation name
     * @return true if exists, false otherwise
     */
    boolean existsByDesignationName(String designationName);

    /**
     * Checks if a given salary is within the range of the designation's min and max
     * salary.
     * 
     * @param designationId the designation ID
     * @param salary        the salary to check
     * @return true if salary is within range
     */
    boolean isSalaryInRange(Long designationId, BigDecimal salary);

    /**
     * Updates the salary range for a specific designation.
     * 
     * @param designationId the designation ID
     * @param minSalary     the minimum salary
     * @param maxSalary     the maximum salary
     * @return the updated designation
     */
    DesignationDTO updateSalaryRange(Long designationId, BigDecimal minSalary, BigDecimal maxSalary);
}
