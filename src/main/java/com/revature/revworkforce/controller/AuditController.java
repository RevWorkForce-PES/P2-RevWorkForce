package com.revature.revworkforce.controller;

import com.revature.revworkforce.dto.AuditLogDTO;
import com.revature.revworkforce.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.revature.revworkforce.enums.LeaveStatus;
import com.revature.revworkforce.model.LeaveApplication;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Audit Controller.
 * 
 * Handles audit log viewing endpoints (Admin only).
 * 
 * @author RevWorkForce Team
 */
@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AuditController {

    @Autowired
    private AuditService auditService;

    @Autowired
    private com.revature.revworkforce.repository.LeaveApplicationRepository leaveApplicationRepository;

    @GetMapping("/audit-reports")
    public String auditReports(
            @RequestParam(required = false) String module,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String leaveEmployeeId,
            @RequestParam(required = false) LeaveStatus leaveStatus,
            Model model) {

        LocalDateTime start = startDate != null ? startDate.atStartOfDay() : null;
        LocalDateTime end = endDate != null ? endDate.atTime(LocalTime.MAX) : null;

        List<AuditLogDTO> auditLogs = auditService.searchAuditLogs(module, start, end, keyword);
        List<LeaveApplication> leaveReports = (leaveEmployeeId == null && leaveStatus == null)
                ? leaveApplicationRepository.findAll()
                : leaveApplicationRepository.searchLeaves(
                        (leaveEmployeeId != null && !leaveEmployeeId.isEmpty()) ? leaveEmployeeId : null,
                        leaveStatus);

        model.addAttribute("auditLogs", auditLogs);
        model.addAttribute("leaveReports", leaveReports);
        model.addAttribute("pageTitle", "Audit & Reports");

        // Pass back filters for form state
        model.addAttribute("selectedModule", module);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("keyword", keyword);
        model.addAttribute("leaveEmployeeId", leaveEmployeeId);
        model.addAttribute("leaveStatus", leaveStatus);

        return "pages/admin/audit-reports";
    }

    @GetMapping("/audit/export")
    @ResponseBody
    public ResponseEntity<byte[]> exportAuditLogs(
            @RequestParam(required = false) String module,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String keyword) {

        LocalDateTime start = startDate != null ? startDate.atStartOfDay() : null;
        LocalDateTime end = endDate != null ? endDate.atTime(LocalTime.MAX) : null;

        List<AuditLogDTO> auditLogs = auditService.searchAuditLogs(module, start, end, keyword);
        byte[] csvData = auditService.exportAuditLogsToCSV(auditLogs);

        String filename = "audit_log_" + LocalDate.now() + ".csv";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csvData);
    }

    @GetMapping("/audit-logins")
    public String loginActivities(Model model) {
        List<AuditLogDTO> loginLogs = auditService.getLoginActivities()
                .stream()
                .map(auditService::convertToDTO)
                .collect(Collectors.toList());
        model.addAttribute("auditLogs", loginLogs);
        model.addAttribute("pageTitle", "Login Activities");
        return "pages/admin/login-activities";
    }
}