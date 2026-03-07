package com.revature.revworkforce.controller;

import com.revature.revworkforce.dto.DepartmentDTO;
import com.revature.revworkforce.dto.DesignationDTO;
import com.revature.revworkforce.service.DepartmentService;
import com.revature.revworkforce.service.DesignationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Department Controller.
 * 
 * Handles department management endpoints for Admins.
 * 
 * @author RevWorkForce Team
 */
@Controller
@RequestMapping("/admin/departments")
@PreAuthorize("hasRole('ADMIN')")
public class DepartmentController {

    private final DepartmentService departmentService;
    private final DesignationService designationService;

    @Autowired
    public DepartmentController(DepartmentService departmentService, DesignationService designationService) {
        this.departmentService = departmentService;
        this.designationService = designationService;
    }

    /**
     * List all departments.
     */
    @GetMapping
    public String listDepartments(Model model) {
        List<DepartmentDTO> departments = departmentService.getAllDepartments();

        model.addAttribute("departments", departments);
        model.addAttribute("pageTitle", "Department Management");

        return "admin/departments/list";
    }

    /**
     * Show add department form.
     */
    @GetMapping("/add")
    public String showAddDepartmentForm(Model model) {
        model.addAttribute("departmentDTO", new DepartmentDTO());
        model.addAttribute("pageTitle", "Add Department");
        model.addAttribute("isEdit", false);

        return "admin/departments/form";
    }

    /**
     * Process add department form.
     */
    @PostMapping("/add")
    public String addDepartment(
            @Valid @ModelAttribute("departmentDTO") DepartmentDTO departmentDTO,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("pageTitle", "Add Department");
            model.addAttribute("isEdit", false);
            return "admin/departments/form";
        }

        try {
            departmentService.createDepartment(departmentDTO);
            redirectAttributes.addFlashAttribute("success",
                    "Department '" + departmentDTO.getDepartmentName() + "' added successfully!");
            return "redirect:/admin/departments";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("pageTitle", "Add Department");
            model.addAttribute("isEdit", false);
            return "admin/departments/form";
        }
    }

    /**
     * Show edit department form.
     */
    @GetMapping("/edit/{departmentId}")
    public String showEditDepartmentForm(@PathVariable Long departmentId, Model model) {
        DepartmentDTO departmentDTO = departmentService.getDepartmentById(departmentId);

        model.addAttribute("departmentDTO", departmentDTO);
        model.addAttribute("pageTitle", "Edit Department");
        model.addAttribute("isEdit", true);

        return "admin/departments/form";
    }

    /**
     * Process edit department form.
     */
    @PostMapping("/edit/{departmentId}")
    public String editDepartment(
            @PathVariable Long departmentId,
            @Valid @ModelAttribute("departmentDTO") DepartmentDTO departmentDTO,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("pageTitle", "Edit Department");
            model.addAttribute("isEdit", true);
            return "admin/departments/form";
        }

        try {
            departmentService.updateDepartment(departmentId, departmentDTO);
            redirectAttributes.addFlashAttribute("success",
                    "Department '" + departmentDTO.getDepartmentName() + "' updated successfully!");
            return "redirect:/admin/departments";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("pageTitle", "Edit Department");
            model.addAttribute("isEdit", true);
            return "admin/departments/form";
        }
    }

    /**
     * Deactivate/Delete department.
     */
    @PostMapping("/delete/{departmentId}")
    public String deleteDepartment(
            @PathVariable Long departmentId,
            RedirectAttributes redirectAttributes) {

        try {
            DepartmentDTO department = departmentService.getDepartmentById(departmentId);
            departmentService.deleteDepartment(departmentId);
            redirectAttributes.addFlashAttribute("success",
                    "Department '" + department.getDepartmentName() + "' deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/admin/departments";
    }
}
