package com.revature.revworkforce.repository;

import com.revature.revworkforce.model.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    @DisplayName("Save role and find by role name")
    void findByRoleName_Success() {

        // Arrange
        Role role = new Role();
        role.setRoleName("ADMIN");

        roleRepository.save(role);

        // Act
        Optional<Role> result = roleRepository.findByRoleName("ADMIN");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("ADMIN", result.get().getRoleName());
    }

    @Test
    @DisplayName("findByRoleName returns empty when role not found")
    void findByRoleName_NotFound() {

        // Act
        Optional<Role> result = roleRepository.findByRoleName("MANAGER");

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("existsByRoleName returns true when role exists")
    void existsByRoleName_True() {

        // Arrange
        Role role = new Role();
        role.setRoleName("EMPLOYEE");

        roleRepository.save(role);

        // Act
        boolean exists = roleRepository.existsByRoleName("EMPLOYEE");

        // Assert
        assertTrue(exists);
    }

    @Test
    @DisplayName("existsByRoleName returns false when role does not exist")
    void existsByRoleName_False() {

        // Act
        boolean exists = roleRepository.existsByRoleName("ADMIN");

        // Assert
        assertFalse(exists);
    }
}