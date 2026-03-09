package com.revature.revworkforce.service.impl;

import com.revature.revworkforce.dto.DesignationDTO;
import com.revature.revworkforce.exception.DuplicateResourceException;
import com.revature.revworkforce.exception.ResourceNotFoundException;
import com.revature.revworkforce.exception.ValidationException;
import com.revature.revworkforce.model.Designation;
import com.revature.revworkforce.repository.DesignationRepository;
import com.revature.revworkforce.repository.EmployeeRepository;
import com.revature.revworkforce.service.AuditService;
import com.revature.revworkforce.service.DesignationService;
import com.revature.revworkforce.util.Constants;
import com.revature.revworkforce.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of DesignationService.
 * 
 * Handles business logic for Designation operations.
 * 
 * @author RevWorkForce Team
 */
@Service
@Transactional
public class DesignationServiceImpl implements DesignationService {

    private final DesignationRepository designationRepository;
    private final EmployeeRepository employeeRepository;
    private final AuditService auditService;

    @Autowired
    public DesignationServiceImpl(DesignationRepository designationRepository, EmployeeRepository employeeRepository,
            AuditService auditService) {
        this.designationRepository = designationRepository;
        this.employeeRepository = employeeRepository;
        this.auditService = auditService;
    }

    @Override
    public DesignationDTO createDesignation(DesignationDTO designationDTO) {
        if (designationRepository.existsByDesignationName(designationDTO.getDesignationName())) {
            throw new DuplicateResourceException(
                    "Designation with name '" + designationDTO.getDesignationName() + "' already exists");
        }

        validateSalaryRange(designationDTO.getMinSalary(), designationDTO.getMaxSalary());

        Designation designation = mapToEntity(designationDTO);
        designation = designationRepository.save(designation);

        String performedBy = SecurityUtils.getCurrentUsername() != null ? SecurityUtils.getCurrentUsername() : "SYSTEM";
        auditService.createAuditLog(
                performedBy,
                Constants.AUDIT_INSERT,
                "DESIGNATIONS",
                designation.getDesignationId().toString(),
                null,
                "Created designation: " + designation.getDesignationName(),
                null,
                null);

        return mapToDTO(designation);
    }

    @Override
    public DesignationDTO updateDesignation(Long designationId, DesignationDTO designationDTO) {
        Designation designation = getEntityById(designationId);

        if (!designation.getDesignationName().equalsIgnoreCase(designationDTO.getDesignationName()) &&
                designationRepository.existsByDesignationName(designationDTO.getDesignationName())) {
            throw new DuplicateResourceException(
                    "Designation with name '" + designationDTO.getDesignationName() + "' already exists");
        }

        validateSalaryRange(designationDTO.getMinSalary(), designationDTO.getMaxSalary());

        designation.setDesignationName(designationDTO.getDesignationName());
        designation.setDesignationLevel(designationDTO.getDesignationLevel());
        designation.setMinSalary(designationDTO.getMinSalary());
        designation.setMaxSalary(designationDTO.getMaxSalary());
        designation.setDescription(designationDTO.getDescription());

        if (designationDTO.getIsActive() != null) {
            designation.setIsActive(designationDTO.getIsActive());
        }

        designation = designationRepository.save(designation);

        String performedBy = SecurityUtils.getCurrentUsername() != null ? SecurityUtils.getCurrentUsername() : "SYSTEM";
        auditService.createAuditLog(
                performedBy,
                Constants.AUDIT_UPDATE,
                "DESIGNATIONS",
                designationId.toString(),
                null,
                "Updated designation details",
                null,
                null);

        return mapToDTO(designation);
    }

    @Override
    @Transactional(readOnly = true)
    public DesignationDTO getDesignationById(Long designationId) {
        return mapToDTO(getEntityById(designationId));
    }

    @Override
    @Transactional(readOnly = true)
    public DesignationDTO getDesignationByName(String designationName) {
        Designation designation = designationRepository.findByDesignationName(designationName)
                .orElseThrow(() -> new ResourceNotFoundException("Designation", "name", designationName));
        return mapToDTO(designation);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DesignationDTO> getAllDesignations() {
        return designationRepository.findAllByOrderByDesignationNameAsc()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DesignationDTO> getActiveDesignations() {
        return designationRepository.findByIsActive('Y')
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DesignationDTO> getDesignationsByLevel(String designationLevel) {
        return designationRepository.findByDesignationLevel(designationLevel)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteDesignation(Long designationId) {
        Designation designation = getEntityById(designationId);
        designation.setIsActive('N');
        designationRepository.save(designation);

        String performedBy = SecurityUtils.getCurrentUsername() != null ? SecurityUtils.getCurrentUsername() : "SYSTEM";
        auditService.createAuditLog(
                performedBy,
                Constants.AUDIT_UPDATE,
                "DESIGNATIONS",
                designationId.toString(),
                "Y",
                "N",
                null,
                null);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByDesignationName(String designationName) {
        return designationRepository.existsByDesignationName(designationName);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isSalaryInRange(Long designationId, BigDecimal salary) {
        Designation designation = getEntityById(designationId);

        if (salary == null)
            return false;

        BigDecimal min = designation.getMinSalary();
        BigDecimal max = designation.getMaxSalary();

        boolean isGteMin = (min == null) || (salary.compareTo(min) >= 0);
        boolean isLteMax = (max == null) || (salary.compareTo(max) <= 0);

        return isGteMin && isLteMax;
    }

    @Override
    public DesignationDTO updateSalaryRange(Long designationId, BigDecimal minSalary, BigDecimal maxSalary) {
        Designation designation = getEntityById(designationId);

        validateSalaryRange(minSalary, maxSalary);

        designation.setMinSalary(minSalary);
        designation.setMaxSalary(maxSalary);

        designation = designationRepository.save(designation);

        String performedBy = SecurityUtils.getCurrentUsername() != null ? SecurityUtils.getCurrentUsername() : "SYSTEM";
        auditService.createAuditLog(
                performedBy,
                Constants.AUDIT_UPDATE,
                "DESIGNATIONS",
                designationId.toString(),
                null,
                "Updated salary range: " + minSalary + " - " + maxSalary,
                null,
                null);

        return mapToDTO(designation);
    }

    // Helper methods
    private Designation getEntityById(Long designationId) {
        return designationRepository.findById(designationId)
                .orElseThrow(() -> new ResourceNotFoundException("Designation", "id", designationId));
    }

    private void validateSalaryRange(BigDecimal minSalary, BigDecimal maxSalary) {
        if (minSalary != null && maxSalary != null && minSalary.compareTo(maxSalary) > 0) {
            throw new ValidationException("Minimum salary cannot be greater than maximum salary");
        }
    }

    private Designation mapToEntity(DesignationDTO dto) {
        Designation entity = new Designation();
        entity.setDesignationId(dto.getDesignationId());
        entity.setDesignationName(dto.getDesignationName());
        entity.setDesignationLevel(dto.getDesignationLevel());
        entity.setMinSalary(dto.getMinSalary());
        entity.setMaxSalary(dto.getMaxSalary());
        entity.setDescription(dto.getDescription());

        if (dto.getIsActive() != null) {
            entity.setIsActive(dto.getIsActive());
        }

        return entity;
    }

    private DesignationDTO mapToDTO(Designation entity) {
        DesignationDTO dto = new DesignationDTO();
        dto.setDesignationId(entity.getDesignationId());
        dto.setDesignationName(entity.getDesignationName());
        dto.setDesignationLevel(entity.getDesignationLevel());
        dto.setMinSalary(entity.getMinSalary());
        dto.setMaxSalary(entity.getMaxSalary());
        dto.setDescription(entity.getDescription());
        dto.setIsActive(entity.getIsActive());

        dto.setEmployeeCount(employeeRepository.countByDesignation(entity));

        return dto;
    }
}
