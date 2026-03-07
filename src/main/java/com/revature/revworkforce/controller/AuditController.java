package com.revature.revworkforce.controller;

import com.revature.revworkforce.dto.AuditLogDTO;
import com.revature.revworkforce.service.AuditService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Audit Controller.
 * 
 * Handles audit log viewing endpoints (Admin only).
 * 
 * @author RevWorkForce Team
 */
@RestController
@RequestMapping("/admin/audit")
@Controller


@PreAuthorize("hasRole('ADMIN')")
public class AuditController {

    @Autowired
    private AuditService auditService;


    @GetMapping
    public List<AuditLogDTO> getAuditLogs(
            @RequestParam(defaultValue = "100") int limit) {
        return auditService.getRecentAuditLogs(limit);
    }

    @GetMapping("/reports")
    public String showAuditReportsPage() {
        return "redirect:/audit-reports.html";
    }
    @GetMapping("/logins")
    public List<AuditLogDTO> getLoginLogs() {
        return auditService.getLoginActivities();
    }
    

   
    @GetMapping("/audit-reports")
    public String auditReports(Model model) {
        List<AuditLogDTO> auditLogs = auditService.getRecentAuditLogsAsDTO(100);
        model.addAttribute("auditLogs", auditLogs);
        model.addAttribute("pageTitle", "Audit & Reports");
        return "frontend/pages/admin/audit-reports";
    }

    @GetMapping("/audit-logins")
    public String loginActivities(Model model) {
        List<AuditLogDTO> loginLogs = auditService.getLoginActivities()
            .stream()
            .map(auditService::convertToDTO)
            .collect(Collectors.toList());
        model.addAttribute("auditLogs", loginLogs);
        model.addAttribute("pageTitle", "Login Activities");
        return "frontend/pages/admin/login-activities";
    }

}