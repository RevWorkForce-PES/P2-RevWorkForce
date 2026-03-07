package com.revature.revworkforce.controller;

import com.revature.revworkforce.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller handling admin dashboard, user management, account control, and
 * health APIs.
 */
@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private AdminService adminService;

    /**
     * View for Admin settings dashboard.
     */
    @GetMapping("/settings")
    public String getAdminSettings() {
    	return "pages/auth/settings";
    }

    /**
     * View for User management page.
     */
    @GetMapping("/user-management")
    public String getUserManagement(Model model) {
    	return "pages/admin/user-management";
    }

    /**
     * Reset an employee password.
     */
    @PostMapping("/reset-password/{id}")
    @ResponseBody
    public ResponseEntity<String> resetPassword(
            @PathVariable("id") String employeeId,
            @RequestParam("newPassword") String newPassword) {
        adminService.resetPassword(employeeId, newPassword);
        return ResponseEntity.ok("Password reset successfully. User must change it on first login.");
    }

    /**
     * Unlock a locked employee account.
     */
    @PostMapping("/unlock-account/{id}")
    @ResponseBody
    public ResponseEntity<String> unlockAccount(@PathVariable("id") String employeeId) {
        adminService.unlockAccount(employeeId);
        return ResponseEntity.ok("Account unlocked successfully.");
    }

    /**
     * Lock an employee account.
     */
    @PostMapping("/lock-account/{id}")
    @ResponseBody
    public ResponseEntity<String> lockAccount(@PathVariable("id") String employeeId) {
        adminService.lockAccount(employeeId);
        return ResponseEntity.ok("Account locked successfully.");
    }

    /**
     * Database health check API.
     */
    @GetMapping("/health")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getDatabaseHealth() {
        return ResponseEntity.ok(adminService.getDatabaseHealth());
    }

    /**
     * System statistics dashboard API.
     */
    @GetMapping("/api/stats")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getSystemStats() {
        return ResponseEntity.ok(adminService.getSystemStatistics());
    }

    /**
     * Bulk activate employees.
     */
    @PostMapping("/api/bulk-activate")
    @ResponseBody
    public ResponseEntity<String> bulkActivateEmployees(@RequestBody List<String> employeeIds) {
        int count = adminService.bulkActivateEmployees(employeeIds);
        return ResponseEntity.ok(count + " employees activated successfully.");
    }

    /**
     * Bulk deactivate employees.
     */
    @PostMapping("/api/bulk-deactivate")
    @ResponseBody
    public ResponseEntity<String> bulkDeactivateEmployees(@RequestBody List<String> employeeIds) {
        int count = adminService.bulkDeactivateEmployees(employeeIds);
        return ResponseEntity.ok(count + " employees deactivated successfully.");
    }
}
