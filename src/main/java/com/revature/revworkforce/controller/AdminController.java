package com.revature.revworkforce.controller;

import com.revature.revworkforce.model.Employee;
import com.revature.revworkforce.model.LeaveBalance;
import com.revature.revworkforce.model.LeaveType;
import com.revature.revworkforce.repository.EmployeeRepository;
import com.revature.revworkforce.repository.LeaveBalanceRepository;
import com.revature.revworkforce.repository.LeaveTypeRepository;
import com.revature.revworkforce.service.AdminService;
import com.revature.revworkforce.service.LeaveTypeService;

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
	    private LeaveTypeService leaveTypeService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private LeaveTypeRepository leaveTypeRepository;

    @Autowired
    private LeaveBalanceRepository leaveBalanceRepository;
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
    @GetMapping("/leave-quota")
    public String showLeaveQuotaPage(Model model){

        model.addAttribute("employees", employeeRepository.findAll());
        model.addAttribute("leaveTypes", leaveTypeRepository.findAll());

        return "pages/admin/leave-quota";
    }
    @PostMapping("/leave-quota/assign")
    public String assignLeaveQuota(@RequestParam String employeeId,
                                   @RequestParam Long leaveTypeId,
                                   @RequestParam Integer year,
                                   @RequestParam Integer days){

        Employee employee = employeeRepository.findById(employeeId).orElseThrow();
        LeaveType leaveType = leaveTypeRepository.findById(leaveTypeId).orElseThrow();

        LeaveBalance balance =
                leaveBalanceRepository
                .findByEmployeeAndLeaveTypeAndYear(employee, leaveType, year)
                .orElse(null);

        if(balance == null){

            balance = new LeaveBalance(
                    employee,
                    leaveType,
                    year,
                    days,
                    0
            );

        } else {

            balance.setTotalAllocated(days);

        }

        leaveBalanceRepository.save(balance);

        return "redirect:/admin/system-config?tab=leave-config";
    }
    @GetMapping("/leave-types/add")
    public String showAddLeaveTypePage(Model model) {

        model.addAttribute("leaveType", new LeaveType());

        return "pages/admin/leave-type-form";
    }
    @GetMapping("/leave-types/edit/{id}")
    public String editLeaveType(@PathVariable Long id, Model model){

        LeaveType leaveType = leaveTypeRepository.findById(id).orElseThrow();

        model.addAttribute("leaveType", leaveType);

        return "pages/admin/leave-type-form";
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
    @PostMapping("/leave-types/save")
    public String saveLeaveType(@ModelAttribute LeaveType leaveType){

        leaveTypeRepository.save(leaveType);

        return "redirect:/admin/system-config?tab=leave-config";
    }
    @GetMapping("/leave-types/delete/{id}")
    public String deleteLeaveType(@PathVariable Long id) {

        leaveTypeRepository.deleteById(id);

        return "redirect:/admin/system-config?tab=leave-config";
    }
}
