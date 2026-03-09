package com.revature.revworkforce.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import com.revature.revworkforce.enums.EmployeeStatus;
import com.revature.revworkforce.model.Employee;
import com.revature.revworkforce.model.Role;

/**
 * Unit tests for UserPrincipal.
 */
class UserPrincipalTest {

    /**
     * Test building UserPrincipal from Employee entity.
     */
    @Test
    void testBuildUserPrincipal() {

        Role role = new Role();
        role.setRoleName("ADMIN");

        Employee employee = new Employee();
        employee.setEmployeeId("E1001");
        employee.setEmail("admin@test.com");

        // FIX: set first and last name instead of fullName
        employee.setFirstName("Admin");
        employee.setLastName("User");

        employee.setPasswordHash("hashedPassword");
        employee.setAccountLocked('N');
        employee.setStatus(EmployeeStatus.ACTIVE);
        employee.setRoles(Set.of(role));

        UserPrincipal principal = UserPrincipal.build(employee);

        assertEquals("E1001", principal.getUsername());
        assertEquals("hashedPassword", principal.getPassword());
        assertEquals("admin@test.com", principal.getEmail());
        assertEquals("Admin User", principal.getFullName());
    }

    /**
     * Test authorities mapping.
     */
    @Test
    void testAuthoritiesMapping() {

        Role role = new Role();
        role.setRoleName("MANAGER");

        Employee employee = new Employee();
        employee.setEmployeeId("E2001");
        employee.setFirstName("Manager");
        employee.setLastName("User");
        employee.setPasswordHash("password");
        employee.setAccountLocked('N');
        employee.setStatus(EmployeeStatus.ACTIVE);
        employee.setRoles(Set.of(role));

        UserPrincipal principal = UserPrincipal.build(employee);

        Set<? extends GrantedAuthority> authorities =
                (Set<? extends GrantedAuthority>) principal.getAuthorities();

        assertTrue(authorities.stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_MANAGER")));
    }

    /**
     * Test account locked behavior.
     */
    @Test
    void testAccountLocked() {

        Role role = new Role();
        role.setRoleName("EMPLOYEE");

        Employee employee = new Employee();
        employee.setEmployeeId("E3001");
        employee.setFirstName("Test");
        employee.setLastName("User");
        employee.setPasswordHash("password");
        employee.setAccountLocked('Y');
        employee.setStatus(EmployeeStatus.ACTIVE);
        employee.setRoles(Set.of(role));

        UserPrincipal principal = UserPrincipal.build(employee);

        assertFalse(principal.isAccountNonLocked());
    }

    /**
     * Test account enabled status.
     */
    @Test
    void testAccountEnabled() {

        Role role = new Role();
        role.setRoleName("EMPLOYEE");

        Employee employee = new Employee();
        employee.setEmployeeId("E4001");
        employee.setFirstName("Active");
        employee.setLastName("User");
        employee.setPasswordHash("password");
        employee.setAccountLocked('N');
        employee.setStatus(EmployeeStatus.ACTIVE);
        employee.setRoles(Set.of(role));

        UserPrincipal principal = UserPrincipal.build(employee);

        assertTrue(principal.isEnabled());
    }

    /**
     * Test hasRole helper method.
     */
    @Test
    void testHasRole() {

        Role role = new Role();
        role.setRoleName("ADMIN");

        Employee employee = new Employee();
        employee.setEmployeeId("E5001");
        employee.setFirstName("Role");
        employee.setLastName("Tester");
        employee.setPasswordHash("password");
        employee.setAccountLocked('N');
        employee.setStatus(EmployeeStatus.ACTIVE);
        employee.setRoles(Set.of(role));

        UserPrincipal principal = UserPrincipal.build(employee);

        assertTrue(principal.hasRole("ADMIN"));
        assertFalse(principal.hasRole("MANAGER"));
    }
}