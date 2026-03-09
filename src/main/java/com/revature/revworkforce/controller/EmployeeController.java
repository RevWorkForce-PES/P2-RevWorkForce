package com.revature.revworkforce.controller;

import com.revature.revworkforce.dto.EmployeeDTO;
import com.revature.revworkforce.dto.EmployeeSearchCriteria;
import com.revature.revworkforce.enums.EmployeeStatus;
import com.revature.revworkforce.model.Employee;
import com.revature.revworkforce.repository.DepartmentRepository;
import com.revature.revworkforce.repository.DesignationRepository;
import com.revature.revworkforce.repository.EmployeeRepository;
import com.revature.revworkforce.repository.RoleRepository;
import com.revature.revworkforce.security.SecurityUtils;
import com.revature.revworkforce.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Employee Controller.
 *
 * Handles employee management endpoints.
 *
 * @author RevWorkForce Team
 */
@Controller
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DesignationRepository designationRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private RoleRepository roleRepository;

    /**
     * Role names allowed to appear in the Reporting Manager dropdown.
     * Both MANAGER and ADMIN employees can be assigned as reporting managers.
     */
    private static final List<String> MANAGER_ROLE_NAMES = Arrays.asList("MANAGER", "ADMIN");

    // ============================================
    // ADMIN API ENDPOINTS
    // ============================================

    /**
     * Returns the next auto-generated Employee ID for a given prefix.
     * Called by the Add Employee form JS when a role is selected.
     */
    @GetMapping("/admin/api/next-employee-id")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public ResponseEntity<String> getNextEmployeeId(
            @RequestParam(defaultValue = "EMP") String prefix) {
        try {
            String nextId = employeeService.generateEmployeeId(prefix.toUpperCase());
            return ResponseEntity.ok(nextId);
        } catch (Exception e) {
            return ResponseEntity.ok(prefix.toUpperCase() + "001");
        }
    }

    // ============================================
    // ADMIN ENDPOINTS - Employee Management
    // ============================================

    /**
     * List all employees with search/filter.
     */
    @GetMapping("/admin/employees")
    @PreAuthorize("hasRole('ADMIN')")
    public String listEmployees(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) Long designationId,
            @RequestParam(required = false) EmployeeStatus status,
            Model model) {

        // Build search criteria
        EmployeeSearchCriteria criteria = new EmployeeSearchCriteria();
        criteria.setKeyword(keyword);
        criteria.setDepartmentId(departmentId);
        criteria.setDesignationId(designationId);
        criteria.setStatus(status);

        // Search and convert to DTOs in service
        List<EmployeeDTO> employeeDTOs = employeeService.searchEmployeesAsDTO(criteria);

        model.addAttribute("employees", employeeDTOs);
        model.addAttribute("criteria", criteria);
        model.addAttribute("departments", departmentRepository.findAllByOrderByDepartmentNameAsc());
        model.addAttribute("designations", designationRepository.findAllByOrderByDesignationNameAsc());
        model.addAttribute("statuses", EmployeeStatus.values());
        model.addAttribute("pageTitle", "Employee Management");

        return "pages/admin/employee-management";
    }

    /**
     * Show add employee form.
     */
    @GetMapping("/admin/employees/add")
    @PreAuthorize("hasRole('ADMIN')")
    public String showAddEmployeeForm(Model model) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setEmployeeId("");

        model.addAttribute("employeeDTO", dto);
        model.addAttribute("departments", departmentRepository.findByIsActive('Y'));
        model.addAttribute("designations", designationRepository.findByIsActive('Y'));
        // Show both MANAGER and ADMIN employees in the Reporting Manager dropdown
        model.addAttribute("managers",
                employeeRepository.findActiveEmployeesByRoleNames(MANAGER_ROLE_NAMES));
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("statuses", EmployeeStatus.values());
        model.addAttribute("pageTitle", "Add Employee");
        model.addAttribute("isEdit", false);

        return "pages/admin/employee-form";
    }

    /**
     * Process add employee form.
     */
    @PostMapping("/admin/employees/add")
    @PreAuthorize("hasRole('ADMIN')")
    public String addEmployee(
            @Valid @ModelAttribute("employeeDTO") EmployeeDTO dto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("departments", departmentRepository.findByIsActive('Y'));
            model.addAttribute("designations", designationRepository.findByIsActive('Y'));
            model.addAttribute("managers",
                    employeeRepository.findActiveEmployeesByRoleNames(MANAGER_ROLE_NAMES));
            model.addAttribute("roles", roleRepository.findAll());
            model.addAttribute("statuses", EmployeeStatus.values());
            model.addAttribute("isEdit", false);
            // Build a readable error summary from binding result
            String errorSummary = result.getAllErrors().stream()
                    .map(e -> e.getDefaultMessage())
                    .collect(Collectors.joining("; "));
            model.addAttribute("error", "Please fix the following errors: " + errorSummary);
            return "pages/admin/employee-form";
        }

        try {
            Employee employee = employeeService.createEmployee(dto);
            redirectAttributes.addFlashAttribute("success",
                    "Employee " + employee.getFullName() + " (ID: " + employee.getEmployeeId()
                            + ") added successfully!");
            return "redirect:/admin/employees";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage() != null ? e.getMessage()
                    : "An unexpected error occurred while saving the employee. Please try again.");
            model.addAttribute("departments", departmentRepository.findByIsActive('Y'));
            model.addAttribute("designations", designationRepository.findByIsActive('Y'));
            model.addAttribute("managers",
                    employeeRepository.findActiveEmployeesByRoleNames(MANAGER_ROLE_NAMES));
            model.addAttribute("roles", roleRepository.findAll());
            model.addAttribute("statuses", EmployeeStatus.values());
            model.addAttribute("isEdit", false);
            return "pages/admin/employee-form";
        }
    }

    /**
     * Show edit employee form.
     */
    @GetMapping("/admin/employees/edit/{employeeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public String showEditEmployeeForm(@PathVariable String employeeId, Model model) {
        EmployeeDTO dto = employeeService.getEmployeeDTOById(employeeId);

        model.addAttribute("employeeDTO", dto);
        model.addAttribute("departments", departmentRepository.findByIsActive('Y'));
        model.addAttribute("designations", designationRepository.findByIsActive('Y'));
        // Show both MANAGER and ADMIN employees in the Reporting Manager dropdown
        model.addAttribute("managers",
                employeeRepository.findActiveEmployeesByRoleNames(MANAGER_ROLE_NAMES));
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("statuses", EmployeeStatus.values());
        model.addAttribute("pageTitle", "Edit Employee");
        model.addAttribute("isEdit", true);

        return "pages/admin/employee-form";
    }

    /**
     * Process edit employee form.
     */
    @PostMapping("/admin/employees/edit/{employeeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public String editEmployee(
            @PathVariable String employeeId,
            @Valid @ModelAttribute("employeeDTO") EmployeeDTO dto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("departments", departmentRepository.findByIsActive('Y'));
            model.addAttribute("designations", designationRepository.findByIsActive('Y'));
            model.addAttribute("managers",
                    employeeRepository.findActiveEmployeesByRoleNames(MANAGER_ROLE_NAMES));
            model.addAttribute("roles", roleRepository.findAll());
            model.addAttribute("statuses", EmployeeStatus.values());
            model.addAttribute("isEdit", true);
            String errorSummary = result.getAllErrors().stream()
                    .map(e -> e.getDefaultMessage())
                    .collect(Collectors.joining("; "));
            model.addAttribute("error", "Please fix the following errors: " + errorSummary);
            return "pages/admin/employee-form";
        }

        try {
            Employee employee = employeeService.updateEmployee(employeeId, dto);
            redirectAttributes.addFlashAttribute("success",
                    "Employee " + employee.getFullName() + " updated successfully!");
            return "redirect:/admin/employees";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage() != null ? e.getMessage()
                    : "An unexpected error occurred while updating the employee. Please try again.");
            model.addAttribute("departments", departmentRepository.findByIsActive('Y'));
            model.addAttribute("designations", designationRepository.findByIsActive('Y'));
            model.addAttribute("managers",
                    employeeRepository.findActiveEmployeesByRoleNames(MANAGER_ROLE_NAMES));
            model.addAttribute("roles", roleRepository.findAll());
            model.addAttribute("statuses", EmployeeStatus.values());
            model.addAttribute("isEdit", true);
            return "pages/admin/employee-form";
        }
    }

    /**
     * View employee details.
     */
    @GetMapping("/admin/employees/view/{employeeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public String viewEmployee(@PathVariable String employeeId, Model model) {
        EmployeeDTO dto = employeeService.getEmployeeDTOById(employeeId);

        List<Employee> teamMembers = employeeService.getTeamMembers(employeeId);

        model.addAttribute("employee", dto);
        model.addAttribute("teamMembers", teamMembers);
        model.addAttribute("pageTitle", "Employee Details");

        return "pages/admin/employee-view";
    }
    

    /**
     * Deactivate employee.
     */
    @PostMapping("/admin/employees/deactivate/{employeeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deactivateEmployee(
            @PathVariable String employeeId,
            RedirectAttributes redirectAttributes) {

        try {
            Employee employee = employeeService.getEmployeeById(employeeId);
            employeeService.deactivateEmployee(employeeId);
            redirectAttributes.addFlashAttribute("success",
                    "Employee " + employee.getFullName() + " deactivated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/admin/employees";
    }

    /**
     * Reactivate employee
     */
    @PostMapping("/admin/employees/reactivate/{employeeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public String reactivateEmployee(
            @PathVariable String employeeId,
            RedirectAttributes redirectAttributes) {

        try {
            Employee employee = employeeService.getEmployeeById(employeeId);

            // Set status to ACTIVE via service
            employeeService.reactivateEmployee(employeeId);

            redirectAttributes.addFlashAttribute("success",
                    "Employee " + employee.getFullName() + " reactivated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/admin/employees";
    }

    /**
     * Delete employee.
     */
    @PostMapping("/admin/employees/delete/{employeeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteEmployee(
            @PathVariable String employeeId,
            RedirectAttributes redirectAttributes) {

        try {
            Employee employee = employeeService.getEmployeeById(employeeId);
            employeeService.deleteEmployee(employeeId);
            redirectAttributes.addFlashAttribute("success",
                    "Employee " + employee.getFullName() + " deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/admin/employees";
    }

    // ============================================
    // EMPLOYEE ENDPOINTS - Profile Management
    // ============================================

    @GetMapping("/employee/profile")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'ADMIN')")
    public String viewProfile(Model model) {
        String employeeId = SecurityUtils.getCurrentUsername();
        EmployeeDTO dto = employeeService.getEmployeeDTOById(employeeId);

        model.addAttribute("currentUser", dto);
        model.addAttribute("manager", dto.getManagerName()); // DTO has managerName
        model.addAttribute("employees", employeeService.getActiveEmployeesAsDTO());
        model.addAttribute("departments", departmentRepository.findByIsActive('Y'));
        model.addAttribute("designations", designationRepository.findByIsActive('Y'));
        model.addAttribute("pageTitle", "My Profile");

        return "pages/employee/profile-directory";
    }

    /**
     * Show edit profile form.
     */
    @GetMapping("/employee/profile/edit")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'ADMIN')")
    public String showEditProfileForm(Model model) {
        String employeeId = SecurityUtils.getCurrentUsername();
        Employee employee = employeeService.getEmployeeById(employeeId);
        EmployeeDTO dto = employeeService.convertToDTO(employee);

        model.addAttribute("employeeDTO", dto);
        model.addAttribute("pageTitle", "Edit Profile");

        return "pages/employee/profile-edit";
    }

    /**
     * Process edit profile form.
     */
    @PostMapping("/employee/profile/update")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'ADMIN')")
    public String updateProfile(
            @ModelAttribute("employeeDTO") EmployeeDTO dto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        String employeeId = SecurityUtils.getCurrentUsername();

        // Manual validation for allowed profile fields
        if (dto.getPhone() != null && !dto.getPhone().isEmpty() && !dto.getPhone().matches("^[6-9]\\d{9}$")) {
            result.rejectValue("phone", "error.phone", "Invalid phone number format (10 digits starting with 6-9)");
        }
        if (dto.getPostalCode() != null && !dto.getPostalCode().isEmpty()
                && !dto.getPostalCode().matches("^[1-9][0-9]{5}$")) {
            result.rejectValue("postalCode", "error.postalCode", "Invalid postal code format (6 digits)");
        }
        if (dto.getEmergencyContactPhone() != null && !dto.getEmergencyContactPhone().isEmpty()
                && !dto.getEmergencyContactPhone().matches("^[6-9]\\d{9}$")) {
            result.rejectValue("emergencyContactPhone", "error.emergencyContactPhone",
                    "Invalid emergency contact phone (10 digits starting with 6-9)");
        }
        if (dto.getAddress() != null && dto.getAddress().length() > 500) {
            result.rejectValue("address", "error.address", "Address cannot exceed 500 characters");
        }

        if (result.hasErrors()) {
            String errorSummary = result.getAllErrors().stream()
                    .map(e -> e.getDefaultMessage())
                    .collect(Collectors.joining("; "));
            model.addAttribute("error", "Please fix the following errors: " + errorSummary);
            model.addAttribute("pageTitle", "Edit Profile");
            return "pages/employee/profile-edit";
        }

        try {
            Employee employee = employeeService.getEmployeeById(employeeId);
            employee.setPhone(dto.getPhone());
            employee.setAddress(dto.getAddress());
            employee.setCity(dto.getCity());
            employee.setState(dto.getState());
            employee.setPostalCode(dto.getPostalCode());
            employee.setCountry(dto.getCountry());
            employee.setEmergencyContactName(dto.getEmergencyContactName());
            employee.setEmergencyContactPhone(dto.getEmergencyContactPhone());

            EmployeeDTO fullDto = employeeService.convertToDTO(employee);
            fullDto.setPhone(dto.getPhone());
            fullDto.setAddress(dto.getAddress());
            fullDto.setCity(dto.getCity());
            fullDto.setState(dto.getState());
            fullDto.setPostalCode(dto.getPostalCode());
            fullDto.setCountry(dto.getCountry());
            fullDto.setEmergencyContactName(dto.getEmergencyContactName());
            fullDto.setEmergencyContactPhone(dto.getEmergencyContactPhone());

            employeeService.updateEmployee(employeeId, fullDto);

            redirectAttributes.addFlashAttribute("success", "Profile updated successfully!");
            return "redirect:/employee/profile";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("pageTitle", "Edit Profile");
            return "pages/employee/profile-edit";
        }
    }
    

    // ============================================
    // EMPLOYEE ENDPOINTS - Employee Directory
    // ============================================

    /**
     * View employee directory with search.
     */
    @GetMapping("/employee/directory")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'ADMIN')")
    public String viewDirectory(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) Long designationId,
            Model model) {

        List<EmployeeDTO> employeeDTOs;

        if (search != null || departmentId != null || designationId != null) {
            EmployeeSearchCriteria criteria = new EmployeeSearchCriteria();
            criteria.setKeyword(search);
            criteria.setDepartmentId(departmentId);
            criteria.setDesignationId(designationId);
            criteria.setStatus(com.revature.revworkforce.enums.EmployeeStatus.ACTIVE);

            employeeDTOs = employeeService.searchEmployeesAsDTO(criteria);
        } else {
            employeeDTOs = employeeService.getActiveEmployeesAsDTO();
        }

        String employeeId = SecurityUtils.getCurrentUsername();
        EmployeeDTO currentUserDTO = null;
        Employee manager = null;

        try {
            Employee currentUser = employeeService.getEmployeeById(employeeId);
            currentUserDTO = employeeService.convertToDTO(currentUser);
            manager = currentUser.getManager();
        } catch (com.revature.revworkforce.exception.ResourceNotFoundException e) {
            // User might be a system admin without an employee record
        }

        model.addAttribute("currentUser", currentUserDTO);
        model.addAttribute("manager", manager);
        model.addAttribute("employees", employeeDTOs);
        model.addAttribute("departments", departmentRepository.findByIsActive('Y'));
        model.addAttribute("designations", designationRepository.findByIsActive('Y'));
        model.addAttribute("search", search);
        model.addAttribute("selectedDepartmentId", departmentId);
        model.addAttribute("selectedDesignationId", designationId);
        model.addAttribute("pageTitle", "Employee Directory");

        return "pages/employee/profile-directory";
    }

    @GetMapping("/admin/employees/search")
    @PreAuthorize("hasRole('ADMIN')")
    public String searchEmployeesAlias(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) Long designationId,
            @RequestParam(required = false) EmployeeStatus status,
            Model model) {
        return listEmployees(keyword, departmentId, designationId, status, model);
    }

    @GetMapping("/admin/employees/department/{departmentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public String filterByDepartment(@PathVariable Long departmentId, Model model) {
        return listEmployees(null, departmentId, null, null, model);
    }

}