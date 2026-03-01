package com.revature.revworkforce.service.impl;

import com.revature.revworkforce.dto.DepartmentDTO;
import com.revature.revworkforce.exception.DuplicateResourceException;
import com.revature.revworkforce.exception.ResourceNotFoundException;
import com.revature.revworkforce.model.Department;
import com.revature.revworkforce.repository.DepartmentRepository;
import com.revature.revworkforce.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of DepartmentService.
 * 
 * Handles business logic for Department operations.
 * 
 * @author RevWorkForce Team
 */
@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public DepartmentDTO createDepartment(DepartmentDTO departmentDTO) {
        if (departmentRepository.existsByDepartmentName(departmentDTO.getDepartmentName())) {
            throw new DuplicateResourceException(
                    "Department with name '" + departmentDTO.getDepartmentName() + "' already exists");
        }

        Department department = mapToEntity(departmentDTO);
        department = departmentRepository.save(department);

        return mapToDTO(department);
    }

    @Override
    public DepartmentDTO updateDepartment(Long departmentId, DepartmentDTO departmentDTO) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", departmentId));

        // Check if updating name to an already existing name
        if (!department.getDepartmentName().equalsIgnoreCase(departmentDTO.getDepartmentName()) &&
                departmentRepository.existsByDepartmentName(departmentDTO.getDepartmentName())) {
            throw new DuplicateResourceException(
                    "Department with name '" + departmentDTO.getDepartmentName() + "' already exists");
        }

        department.setDepartmentName(departmentDTO.getDepartmentName());
        department.setDepartmentHead(departmentDTO.getDepartmentHead());
        department.setDescription(departmentDTO.getDescription());

        if (departmentDTO.getIsActive() != null) {
            department.setIsActive(departmentDTO.getIsActive());
        }

        department = departmentRepository.save(department);

        return mapToDTO(department);
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentDTO getDepartmentById(Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", departmentId));
        return mapToDTO(department);
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentDTO getDepartmentByName(String departmentName) {
        Department department = departmentRepository.findByDepartmentName(departmentName)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "name", departmentName));
        return mapToDTO(department);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentDTO> getAllDepartments() {
        List<Department> departments = departmentRepository.findAllByOrderByDepartmentNameAsc();
        return departments.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentDTO> getActiveDepartments() {
        List<Department> departments = departmentRepository.findByIsActive('Y');
        return departments.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteDepartment(Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", departmentId));

        // Soft delete
        department.setIsActive('N');
        departmentRepository.save(department);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByDepartmentName(String departmentName) {
        return departmentRepository.existsByDepartmentName(departmentName);
    }

    // Helper methods for mapping
    private Department mapToEntity(DepartmentDTO dto) {
        Department entity = new Department();
        entity.setDepartmentId(dto.getDepartmentId());
        entity.setDepartmentName(dto.getDepartmentName());
        entity.setDepartmentHead(dto.getDepartmentHead());
        entity.setDescription(dto.getDescription());

        if (dto.getIsActive() != null) {
            entity.setIsActive(dto.getIsActive());
        }

        return entity;
    }

    private DepartmentDTO mapToDTO(Department entity) {
        DepartmentDTO dto = new DepartmentDTO();
        dto.setDepartmentId(entity.getDepartmentId());
        dto.setDepartmentName(entity.getDepartmentName());
        dto.setDepartmentHead(entity.getDepartmentHead());
        dto.setDescription(entity.getDescription());
        dto.setIsActive(entity.getIsActive());

        return dto;
    }
}
