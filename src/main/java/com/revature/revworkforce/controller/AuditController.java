package com.revature.revworkforce.controller;

import com.revature.revworkforce.dto.AuditLogDTO;
import com.revature.revworkforce.service.AuditService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/audit")
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
    
}