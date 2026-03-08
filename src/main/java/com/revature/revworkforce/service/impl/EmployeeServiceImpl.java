package com.revature.revworkforce.service.impl;

import com.revature.revworkforce.dto.EmployeeDTO;
import com.revature.revworkforce.dto.EmployeeSearchCriteria;
import com.revature.revworkforce.enums.EmployeeStatus;
import com.revature.revworkforce.exception.DuplicateResourceException;
import com.revature.revworkforce.exception.ResourceNotFoundException;
import com.revature.revworkforce.exception.ValidationException;
import com.revature.revworkforce.model.*;
import com.revature.revworkforce.repository.*;
import com.revature.revworkforce.service.EmployeeService;
import com.revature.revworkforce.util.ValidationUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Employee Service Implementation.
 *
 * <p>
 * Implements all business logic for EmployeeService interface including CRUD
 * operations, search, role management, and DTO conversion.
 * </p>
 */
@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

	private final EmployeeRepository employeeRepository;
	private final DepartmentRepository departmentRepository;
	private final DesignationRepository designationRepository;
	private final RoleRepository roleRepository;
	private final EmployeeRoleRepository employeeRoleRepository;
	private final PasswordEncoder passwordEncoder;

	/**
	 * Constructor-based dependency injection.
	 */
	public EmployeeServiceImpl(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository,
			DesignationRepository designationRepository, RoleRepository roleRepository,
			EmployeeRoleRepository employeeRoleRepository, PasswordEncoder passwordEncoder) {
		this.employeeRepository = employeeRepository;
		this.departmentRepository = departmentRepository;
		this.designationRepository = designationRepository;
		this.roleRepository = roleRepository;
		this.employeeRoleRepository = employeeRoleRepository;
		this.passwordEncoder = passwordEncoder;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Employee createEmployee(EmployeeDTO dto) {

		// Check for duplicate email
		if (employeeRepository.existsByEmail(dto.getEmail())) {
			throw new DuplicateResourceException("Employee", "email", dto.getEmail());
		}

		// Generate employee ID if missing
		if (dto.getEmployeeId() == null || dto.getEmployeeId().isEmpty()) {
			dto.setEmployeeId(generateEmployeeId("EMP"));
		}

		// Check for duplicate employee ID
		if (employeeRepository.existsById(dto.getEmployeeId())) {
			throw new DuplicateResourceException("Employee", "employeeId", dto.getEmployeeId());
		}

		// Validate age
		if (!ValidationUtil.isValidAge(dto.getDateOfBirth())) {
			throw new ValidationException("Employee must be at least 18 years old");
		}

		// Convert DTO to entity
		Employee employee = convertToEntity(dto);

		// Encode password
		String password = dto.getPassword() != null && !dto.getPassword().isEmpty() ? dto.getPassword() : "password123";
		employee.setPasswordHash(passwordEncoder.encode(password));
		employee.setFirstLogin('Y');

		// Set defaults
		employee.setStatus(EmployeeStatus.ACTIVE);
		employee.setFailedLoginAttempts(0);
		employee.setAccountLocked('N');
		employee.setCreatedAt(LocalDateTime.now());
		employee.setUpdatedAt(LocalDateTime.now());

		// Save employee
		Employee savedEmployee = employeeRepository.save(employee);

		// Assign roles
		if (dto.getRoleIds() != null && !dto.getRoleIds().isEmpty()) {
			for (Long roleId : dto.getRoleIds()) {
				assignRole(savedEmployee.getEmployeeId(), roleId);
			}
		} else {
			// Assign default EMPLOYEE role
			Role defaultRole = roleRepository.findByRoleName("EMPLOYEE")
					.orElseThrow(() -> new ResourceNotFoundException("Role", "name", "EMPLOYEE"));
			assignRole(savedEmployee.getEmployeeId(), defaultRole.getRoleId());
		}

		return savedEmployee;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Employee updateEmployee(String employeeId, EmployeeDTO dto) {
		Employee employee = getEmployeeById(employeeId);

		// Check email uniqueness
		if (!employee.getEmail().equals(dto.getEmail()) && employeeRepository.existsByEmail(dto.getEmail())) {
			throw new DuplicateResourceException("Employee", "email", dto.getEmail());
		}

		// Update basic info
		employee.setFirstName(dto.getFirstName());
		employee.setLastName(dto.getLastName());
		employee.setEmail(dto.getEmail());
		employee.setPhone(dto.getPhone());
		employee.setDateOfBirth(dto.getDateOfBirth());
		employee.setGender(dto.getGender());
		employee.setAddress(dto.getAddress());
		employee.setCity(dto.getCity());
		employee.setState(dto.getState());
		employee.setPostalCode(dto.getPostalCode());
		employee.setCountry(dto.getCountry());
		employee.setEmergencyContactName(dto.getEmergencyContactName());
		employee.setEmergencyContactPhone(dto.getEmergencyContactPhone());

		// Update department
		if (dto.getDepartmentId() != null) {
			Department department = departmentRepository.findById(dto.getDepartmentId())
					.orElseThrow(() -> new ResourceNotFoundException("Department", "id", dto.getDepartmentId()));
			employee.setDepartment(department);
		}

		// Update designation
		if (dto.getDesignationId() != null) {
			Designation designation = designationRepository.findById(dto.getDesignationId())
					.orElseThrow(() -> new ResourceNotFoundException("Designation", "id", dto.getDesignationId()));
			employee.setDesignation(designation);
		}

		// Update manager
		if (dto.getManagerId() != null && !dto.getManagerId().isEmpty()) {
			Employee manager = employeeRepository.findById(dto.getManagerId())
					.orElseThrow(() -> new ResourceNotFoundException("Manager", "employeeId", dto.getManagerId()));
			employee.setManager(manager);
		}

		// Update employment details
		employee.setJoiningDate(dto.getJoiningDate());
		employee.setLeavingDate(dto.getLeavingDate());
		employee.setSalary(dto.getSalary());
		employee.setStatus(dto.getStatus());
		employee.setUpdatedAt(LocalDateTime.now());

		// Update roles
		if (dto.getRoleIds() != null) {
			employeeRoleRepository.deleteByEmployeeId(employeeId);
			for (Long roleId : dto.getRoleIds()) {
				assignRole(employeeId, roleId);
			}
		}

		return employeeRepository.save(employee);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public Employee getEmployeeById(String employeeId) {
		return employeeRepository.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee", "employeeId", employeeId));
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public List<Employee> getAllEmployees() {
		return employeeRepository.findAll();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public List<Employee> getActiveEmployees() {
		return employeeRepository.findByStatus(EmployeeStatus.ACTIVE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public List<Employee> searchEmployees(EmployeeSearchCriteria criteria) {
		List<Employee> results;

		if (criteria.getKeyword() != null && !criteria.getKeyword().isEmpty()) {
			results = employeeRepository.searchByKeyword(criteria.getKeyword());
		} else {
			results = employeeRepository.findAll();
		}

		if (criteria.getDepartmentId() != null) {
			results = results.stream()
					.filter(e -> e.getDepartment() != null
							&& e.getDepartment().getDepartmentId().equals(criteria.getDepartmentId()))
					.collect(Collectors.toList());
		}

		if (criteria.getDesignationId() != null) {
			results = results.stream()
					.filter(e -> e.getDesignation() != null
							&& e.getDesignation().getDesignationId().equals(criteria.getDesignationId()))
					.collect(Collectors.toList());
		}

		if (criteria.getStatus() != null) {
			results = results.stream().filter(e -> e.getStatus() == criteria.getStatus()).collect(Collectors.toList());
		}

		if (criteria.getManagerId() != null && !criteria.getManagerId().isEmpty()) {
			results = results.stream().filter(
					e -> e.getManager() != null && e.getManager().getEmployeeId().equals(criteria.getManagerId()))
					.collect(Collectors.toList());
		}

		return results;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public List<Employee> getEmployeesByDepartment(Long departmentId) {
		Department department = departmentRepository.findById(departmentId)
				.orElseThrow(() -> new ResourceNotFoundException("Department", "id", departmentId));
		return employeeRepository.findByDepartment(department);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public List<Employee> getEmployeesByDesignation(Long designationId) {
		Designation designation = designationRepository.findById(designationId)
				.orElseThrow(() -> new ResourceNotFoundException("Designation", "id", designationId));
		return employeeRepository.findByDesignation(designation);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	
    public List<Employee> getTeamMembers(String managerId) {
        return employeeRepository.findByManagerEmployeeId(managerId);
    }
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deactivateEmployee(String employeeId) {
		Employee employee = getEmployeeById(employeeId);
		employee.setStatus(EmployeeStatus.INACTIVE);
		employee.setLeavingDate(LocalDate.now());
		employee.setUpdatedAt(LocalDateTime.now());
		employeeRepository.save(employee);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteEmployee(String employeeId) {
		Employee employee = getEmployeeById(employeeId);
		employee.setStatus(EmployeeStatus.TERMINATED);
		employee.setLeavingDate(LocalDate.now());
		employee.setUpdatedAt(LocalDateTime.now());
		employeeRepository.save(employee);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void assignRole(String employeeId, Long roleId) {
		Employee employee = getEmployeeById(employeeId);
		Role role = roleRepository.findById(roleId)
				.orElseThrow(() -> new ResourceNotFoundException("Role", "id", roleId));

		if (!employeeRoleRepository.existsByEmployeeIdAndRoleId(employeeId, roleId)) {
			EmployeeRole employeeRole = new EmployeeRole();
			employeeRole.setEmployeeId(employeeId);
			employeeRole.setRoleId(roleId);
			employeeRole.setEmployee(employee);
			employeeRole.setRole(role);
			employeeRole.setAssignedAt(LocalDateTime.now());
			employeeRoleRepository.save(employeeRole);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeRole(String employeeId, Long roleId) {
		employeeRoleRepository.deleteByEmployeeIdAndRoleId(employeeId, roleId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public Set<Role> getEmployeeRoles(String employeeId) {
		Employee employee = getEmployeeById(employeeId);
		return employee.getRoles();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String generateEmployeeId(String prefix) {
		List<Employee> allEmployees = employeeRepository.findAll();
		int maxNumber = 0;
		for (Employee emp : allEmployees) {
			if (emp.getEmployeeId().startsWith(prefix)) {
				try {
					int number = Integer.parseInt(emp.getEmployeeId().substring(prefix.length()));
					if (number > maxNumber)
						maxNumber = number;
				} catch (NumberFormatException ignored) {
				}
			}
		}
		return String.format("%s%03d", prefix, maxNumber + 1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public EmployeeDTO convertToDTO(Employee employee) {
		EmployeeDTO dto = new EmployeeDTO();
		dto.setEmployeeId(employee.getEmployeeId());
		dto.setFirstName(employee.getFirstName());
		dto.setLastName(employee.getLastName());
		dto.setEmail(employee.getEmail());
		dto.setStatus(employee.getStatus());

		if (employee.getDepartment() != null) {
			dto.setDepartmentId(employee.getDepartment().getDepartmentId());
			dto.setDepartmentName(employee.getDepartment().getDepartmentName());
		}

		if (employee.getDesignation() != null) {
			dto.setDesignationId(employee.getDesignation().getDesignationId());
			dto.setDesignationName(employee.getDesignation().getDesignationName());
		}

		if (employee.getManager() != null) {
			dto.setManagerId(employee.getManager().getEmployeeId());
			dto.setManagerName(employee.getManager().getFullName());
		}

		dto.setRoleIds(employee.getRoles().stream().map(Role::getRoleId).collect(Collectors.toSet()));
		dto.setRoleNames(employee.getRoles().stream().map(Role::getRoleName).collect(Collectors.toSet()));

		return dto;
	}

	/**
	 * Convert DTO to Employee entity.
	 */
	private Employee convertToEntity(EmployeeDTO dto) {
		Employee employee = new Employee();
		employee.setEmployeeId(dto.getEmployeeId());
		employee.setFirstName(dto.getFirstName());
		employee.setLastName(dto.getLastName());
		employee.setEmail(dto.getEmail());
		employee.setPhone(dto.getPhone());
		employee.setDateOfBirth(dto.getDateOfBirth());
		employee.setGender(dto.getGender());
		employee.setAddress(dto.getAddress());
		employee.setCity(dto.getCity());
		employee.setState(dto.getState());
		employee.setPostalCode(dto.getPostalCode());
		employee.setCountry(dto.getCountry());
		employee.setEmergencyContactName(dto.getEmergencyContactName());
		employee.setEmergencyContactPhone(dto.getEmergencyContactPhone());

		if (dto.getDepartmentId() != null) {
			Department department = departmentRepository.findById(dto.getDepartmentId())
					.orElseThrow(() -> new ResourceNotFoundException("Department", "id", dto.getDepartmentId()));
			employee.setDepartment(department);
		}

		if (dto.getDesignationId() != null) {
			Designation designation = designationRepository.findById(dto.getDesignationId())
					.orElseThrow(() -> new ResourceNotFoundException("Designation", "id", dto.getDesignationId()));
			employee.setDesignation(designation);
		}

		if (dto.getManagerId() != null && !dto.getManagerId().isEmpty()) {
			Employee manager = employeeRepository.findById(dto.getManagerId())
					.orElseThrow(() -> new ResourceNotFoundException("Manager", "employeeId", dto.getManagerId()));
			employee.setManager(manager);
		}

		employee.setJoiningDate(dto.getJoiningDate());
		employee.setLeavingDate(dto.getLeavingDate());
		employee.setSalary(dto.getSalary());
		employee.setStatus(dto.getStatus() != null ? dto.getStatus() : EmployeeStatus.ACTIVE);

		return employee;
	}

	@Override
	public Object getEmployeeGoals(String employeeId) {
		// TODO Auto-generated method stub
		return null;
	}
}