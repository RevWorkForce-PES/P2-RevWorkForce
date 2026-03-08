package com.revature.revworkforce.exception;

import com.revature.revworkforce.model.Employee;
import com.revature.revworkforce.repository.EmployeeRepository;
import com.revature.revworkforce.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Optional;

/**
 * Global Controller Advice to provide common model attributes to all views.
 * This ensures that a session's user info is available for the sidebar
 * component
 * across all pages.
 * 
 * @author RevWorkForce Team
 */
@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * Adds common user attributes to the model for all authenticated requests.
     * 
     * @param model the Spring UI model
     */
    @ModelAttribute
    public void addUserAttributes(Model model) {
        if (SecurityUtils.isAuthenticated()) {
            String employeeId = SecurityUtils.getCurrentUsername();
            if (employeeId != null) {
                Optional<Employee> employeeOpt = employeeRepository.findById(employeeId);
                if (employeeOpt.isPresent()) {
                    Employee employee = employeeOpt.get();
                    model.addAttribute("fullName", employee.getFullName());
                } else {
                    // Fallback for admin users without an employee record
                    model.addAttribute("fullName", "System Administrator");
                }

                // Determine the primary display role
                Authentication auth = SecurityUtils.getCurrentAuthentication();
                if (auth != null) {
                    String primaryRole = auth.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .map(r -> r.replace("ROLE_", ""))
                            .sorted((r1, r2) -> {
                                // Priority order: ADMIN > MANAGER > EMPLOYEE
                                int p1 = r1.equals("ADMIN") ? 0 : (r1.equals("MANAGER") ? 1 : 2);
                                int p2 = r2.equals("ADMIN") ? 0 : (r2.equals("MANAGER") ? 1 : 2);
                                return Integer.compare(p1, p2);
                            })
                            .findFirst()
                            .orElse("User");

                    // Format role for display (e.g., "ADMIN" -> "Admin")
                    String formattedRole = primaryRole.substring(0, 1).toUpperCase() +
                            primaryRole.substring(1).toLowerCase();
                    model.addAttribute("userRole", formattedRole);
                }
            }
        }
    }
}
