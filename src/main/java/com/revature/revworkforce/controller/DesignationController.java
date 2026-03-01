package com.revature.revworkforce.controller;

import com.revature.revworkforce.dto.DesignationDTO;
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
 * Designation Controller.
 * 
 * Handles designation management endpoints for Admins.
 * 
 * @author RevWorkForce Team
 */
@Controller
@RequestMapping("/admin/designations")
@PreAuthorize("hasRole('ADMIN')")
public class DesignationController {

    private final DesignationService designationService;

    @Autowired
    public DesignationController(DesignationService designationService) {
        this.designationService = designationService;
    }

    /**
     * List all designations.
     */
    @GetMapping
    public String listDesignations(Model model) {
        List<DesignationDTO> designations = designationService.getAllDesignations();

        model.addAttribute("designations", designations);
        model.addAttribute("pageTitle", "Designation Management");

        return "admin/designations/list";
    }

    /**
     * Show add designation form.
     */
    @GetMapping("/add")
    public String showAddDesignationForm(Model model) {
        model.addAttribute("designationDTO", new DesignationDTO());
        model.addAttribute("pageTitle", "Add Designation");
        model.addAttribute("isEdit", false);

        return "admin/designations/form";
    }

    /**
     * Process add designation form.
     */
    @PostMapping("/add")
    public String addDesignation(
            @Valid @ModelAttribute("designationDTO") DesignationDTO designationDTO,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("pageTitle", "Add Designation");
            model.addAttribute("isEdit", false);
            return "admin/designations/form";
        }

        try {
            designationService.createDesignation(designationDTO);
            redirectAttributes.addFlashAttribute("success",
                    "Designation '" + designationDTO.getDesignationName() + "' added successfully!");
            return "redirect:/admin/designations";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("pageTitle", "Add Designation");
            model.addAttribute("isEdit", false);
            return "admin/designations/form";
        }
    }

    /**
     * Show edit designation form.
     */
    @GetMapping("/edit/{designationId}")
    public String showEditDesignationForm(@PathVariable Long designationId, Model model) {
        DesignationDTO designationDTO = designationService.getDesignationById(designationId);

        model.addAttribute("designationDTO", designationDTO);
        model.addAttribute("pageTitle", "Edit Designation");
        model.addAttribute("isEdit", true);

        return "admin/designations/form";
    }

    /**
     * Process edit designation form.
     */
    @PostMapping("/edit/{designationId}")
    public String editDesignation(
            @PathVariable Long designationId,
            @Valid @ModelAttribute("designationDTO") DesignationDTO designationDTO,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("pageTitle", "Edit Designation");
            model.addAttribute("isEdit", true);
            return "admin/designations/form";
        }

        try {
            designationService.updateDesignation(designationId, designationDTO);
            redirectAttributes.addFlashAttribute("success",
                    "Designation '" + designationDTO.getDesignationName() + "' updated successfully!");
            return "redirect:/admin/designations";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("pageTitle", "Edit Designation");
            model.addAttribute("isEdit", true);
            return "admin/designations/form";
        }
    }

    /**
     * Deactivate/Delete designation.
     */
    @PostMapping("/delete/{designationId}")
    public String deleteDesignation(
            @PathVariable Long designationId,
            RedirectAttributes redirectAttributes) {

        try {
            DesignationDTO designation = designationService.getDesignationById(designationId);
            designationService.deleteDesignation(designationId);
            redirectAttributes.addFlashAttribute("success",
                    "Designation '" + designation.getDesignationName() + "' deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/admin/designations";
    }
}
