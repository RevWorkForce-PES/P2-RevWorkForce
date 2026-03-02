package com.revature.revworkforce.controller;

import com.revature.revworkforce.dto.ReportDTO;
import com.revature.revworkforce.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for generated Reports & Analytics.
 * Exposed to ADMIN only.
 * 
 * @author RevWorkForce Team
 */
@RestController
@RequestMapping("/admin/reports")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class ReportController {

    private final ReportService reportService;

    @GetMapping
    public ResponseEntity<ReportDTO> getDashboardStatistics() {
        return ResponseEntity.ok(reportService.getDashboardStatistics());
    }

    @GetMapping("/employees")
    public ResponseEntity<ReportDTO> getEmployeeReport() {
        return ResponseEntity.ok(reportService.getEmployeeReport());
    }

    @GetMapping("/leaves")
    public ResponseEntity<ReportDTO> getLeaveReport(@RequestParam(required = false) Integer year) {
        return ResponseEntity.ok(reportService.getLeaveReport(year));
    }

    @GetMapping("/departments")
    public ResponseEntity<ReportDTO> getDepartmentReport(@RequestParam(required = false) Long departmentId) {
        return ResponseEntity.ok(reportService.getDepartmentReport(departmentId));
    }

    @GetMapping("/performance")
    public ResponseEntity<ReportDTO> getPerformanceReport(@RequestParam(required = false) Integer year) {
        return ResponseEntity.ok(reportService.getPerformanceReport(year));
    }

    @GetMapping("/goals")
    public ResponseEntity<ReportDTO> getGoalReport() {
        return ResponseEntity.ok(reportService.getGoalReport());
    }

    @GetMapping("/attendance")
    public ResponseEntity<ReportDTO> getAttendanceReport(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month) {
        return ResponseEntity.ok(reportService.getAttendanceReport(year, month));
    }
}
