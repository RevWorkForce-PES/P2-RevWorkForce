package com.revature.revworkforce.controller;

import com.revature.revworkforce.dto.EmployeeDTO;
import com.revature.revworkforce.dto.EmployeeSearchCriteria;
import com.revature.revworkforce.enums.EmployeeStatus;
import com.revature.revworkforce.model.Employee;
import com.revature.revworkforce.repository.DepartmentRepository;
import com.revature.revworkforce.repository.DesignationRepository;
import com.revature.revworkforce.repository.RoleRepository;
import com.revature.revworkforce.security.SecurityUtils;
import com.revature.revworkforce.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    private RoleRepository roleRepository;
    
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
        
        // Search employees
        List<Employee> employees = employeeService.searchEmployees(criteria);
        
        // Convert to DTOs
        List<EmployeeDTO> employeeDTOs = employees.stream()
            .map(employeeService::convertToDTO)
            .collect(Collectors.toList());
        
        model.addAttribute("employees", employeeDTOs);
        model.addAttribute("criteria", criteria);
        model.addAttribute("departments", departmentRepository.findAllByOrderByDepartmentNameAsc());
        model.addAttribute("designations", designationRepository.findAllByOrderByDesignationNameAsc());
        model.addAttribute("statuses", EmployeeStatus.values());
        model.addAttribute("pageTitle", "Employee Management");
        
        return "admin/employees/list";
    }
    
    /**
     * Show add employee form.
     */
    @GetMapping("/admin/employees/add")
    @PreAuthorize("hasRole('ADMIN')")
    public String showAddEmployeeForm(Model model) {
        EmployeeDTO dto = new EmployeeDTO();
        
        // Generate employee ID
        dto.setEmployeeId(employeeService.generateEmployeeId("EMP"));
        
        model.addAttribute("employeeDTO", dto);
        model.addAttribute("departments", departmentRepository.findByIsActive('Y'));
        model.addAttribute("designations", designationRepository.findByIsActive('Y'));
        model.addAttribute("managers", employeeService.getActiveEmployees());
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("statuses", EmployeeStatus.values());
        model.addAttribute("pageTitle", "Add Employee");
        model.addAttribute("isEdit", false);
        
        return "admin/employees/form";
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
            model.addAttribute("managers", employeeService.getActiveEmployees());
            model.addAttribute("roles", roleRepository.findAll());
            model.addAttribute("statuses", EmployeeStatus.values());
            model.addAttribute("isEdit", false);
            return "admin/employees/form";
        }
        
        try {
            Employee employee = employeeService.createEmployee(dto);
            redirectAttributes.addFlashAttribute("success", 
                "Employee " + employee.getFullName() + " (ID: " + employee.getEmployeeId() + ") added successfully!");
            return "redirect:/admin/employees";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("departments", departmentRepository.findByIsActive('Y'));
            model.addAttribute("designations", designationRepository.findByIsActive('Y'));
            model.addAttribute("managers", employeeService.getActiveEmployees());
            model.addAttribute("roles", roleRepository.findAll());
            model.addAttribute("statuses", EmployeeStatus.values());
            model.addAttribute("isEdit", false);
            return "admin/employees/form";
        }
    }
    
    /**
     * Show edit employee form.
     */
    @GetMapping("/admin/employees/edit/{employeeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public String showEditEmployeeForm(@PathVariable String employeeId, Model model) {
        Employee employee = employeeService.getEmployeeById(employeeId);
        EmployeeDTO dto = employeeService.convertToDTO(employee);
        
        model.addAttribute("employeeDTO", dto);
        model.addAttribute("departments", departmentRepository.findByIsActive('Y'));
        model.addAttribute("designations", designationRepository.findByIsActive('Y'));
        model.addAttribute("managers", employeeService.getActiveEmployees());
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("statuses", EmployeeStatus.values());
        model.addAttribute("pageTitle", "Edit Employee");
        model.addAttribute("isEdit", true);
        
        return "admin/employees/form";
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
            model.addAttribute("managers", employeeService.getActiveEmployees());
            model.addAttribute("roles", roleRepository.findAll());
            model.addAttribute("statuses", EmployeeStatus.values());
            model.addAttribute("isEdit", true);
            return "admin/employees/form";
        }
        
        try {
            Employee employee = employeeService.updateEmployee(employeeId, dto);
            redirectAttributes.addFlashAttribute("success", 
                "Employee " + employee.getFullName() + " updated successfully!");
            return "redirect:/admin/employees";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("departments", departmentRepository.findByIsActive('Y'));
            model.addAttribute("designations", designationRepository.findByIsActive('Y'));
            model.addAttribute("managers", employeeService.getActiveEmployees());
            model.addAttribute("roles", roleRepository.findAll());
            model.addAttribute("statuses", EmployeeStatus.values());
            model.addAttribute("isEdit", true);
            return "admin/employees/form";
        }
    }
    
    /**
     * View employee details.
     */
    @GetMapping("/admin/employees/view/{employeeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public String viewEmployee(@PathVariable String employeeId, Model model) {
        Employee employee = employeeService.getEmployeeById(employeeId);
        EmployeeDTO dto = employeeService.convertToDTO(employee);
        
        // Get team members if this is a manager
        List<Employee> teamMembers = employeeService.getTeamMembers(employeeId);
        
        model.addAttribute("employee", dto);
        model.addAttribute("teamMembers", teamMembers);
        model.addAttribute("pageTitle", "Employee Details");
        
        return "admin/employees/view";
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
    
    /**
     * View own profile.
     */
    @GetMapping("/employee/profile")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'ADMIN')")
    public String viewProfile(Model model) {
        String employeeId = SecurityUtils.getCurrentUsername();
        Employee employee = employeeService.getEmployeeById(employeeId);
        EmployeeDTO dto = employeeService.convertToDTO(employee);
        
        model.addAttribute("employee", dto);
        model.addAttribute("pageTitle", "My Profile");
        
        return "employee/profile";
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
        
        return "employee/profile-edit";
    }
    
    /**
     * Process edit profile form.
     */
    @PostMapping("/employee/profile/update")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'ADMIN')")
    public String updateProfile(
            @Valid @ModelAttribute("employeeDTO") EmployeeDTO dto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        String employeeId = SecurityUtils.getCurrentUsername();
        
        if (result.hasErrors()) {
            return "employee/profile-edit";
        }
        
        try {
            // Only allow updating specific fields
            Employee employee = employeeService.getEmployeeById(employeeId);
            employee.setPhone(dto.getPhone());
            employee.setAddress(dto.getAddress());
            employee.setCity(dto.getCity());
            employee.setState(dto.getState());
            employee.setPostalCode(dto.getPostalCode());
            employee.setEmergencyContactName(dto.getEmergencyContactName());
            employee.setEmergencyContactPhone(dto.getEmergencyContactPhone());
            
            // Note: Convert to DTO and back to use the existing update method
            EmployeeDTO fullDto = employeeService.convertToDTO(employee);
            fullDto.setPhone(dto.getPhone());
            fullDto.setAddress(dto.getAddress());
            fullDto.setCity(dto.getCity());
            fullDto.setState(dto.getState());
            fullDto.setPostalCode(dto.getPostalCode());
            fullDto.setEmergencyContactName(dto.getEmergencyContactName());
            fullDto.setEmergencyContactPhone(dto.getEmergencyContactPhone());
            
            employeeService.updateEmployee(employeeId, fullDto);
            
            redirectAttributes.addFlashAttribute("success", "Profile updated successfully!");
            return "redirect:/employee/profile";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "employee/profile-edit";
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
        
        List<Employee> employees;
        
        if (search != null || departmentId != null || designationId != null) {
            // Build search criteria
            EmployeeSearchCriteria criteria = new EmployeeSearchCriteria();
            criteria.setKeyword(search);
            criteria.setDepartmentId(departmentId);
            criteria.setDesignationId(designationId);
            criteria.setStatus(com.revature.revworkforce.enums.EmployeeStatus.ACTIVE);
            
            employees = employeeService.searchEmployees(criteria);
        } else {
            // Show only active employees
            employees = employeeService.getActiveEmployees();
        }
        
        List<EmployeeDTO> employeeDTOs = employees.stream()
            .map(employeeService::convertToDTO)
            .collect(Collectors.toList());
        
        model.addAttribute("employees", employeeDTOs);
        model.addAttribute("departments", departmentRepository.findByIsActive('Y'));
        model.addAttribute("designations", designationRepository.findByIsActive('Y'));
        model.addAttribute("search", search);
        model.addAttribute("selectedDepartmentId", departmentId);
        model.addAttribute("selectedDesignationId", designationId);
        model.addAttribute("pageTitle", "Employee Directory");
        
        return "employee/directory";
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