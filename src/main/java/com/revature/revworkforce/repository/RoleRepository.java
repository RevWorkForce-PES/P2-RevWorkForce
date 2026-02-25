package com.revature.revworkforce.repository;

import com.revature.revworkforce.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for Role entity.
 * 
 * Provides CRUD operations and custom query methods for Role.
 * 
 * @author RevWorkForce Team
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    
    /**
     * Find role by name.
     * 
     * @param roleName the role name (ADMIN, MANAGER, EMPLOYEE)
     * @return Optional containing the role if found
     */
    Optional<Role> findByRoleName(String roleName);
    
    /**
     * Check if role exists by name.
     * 
     * @param roleName the role name
     * @return true if exists
     */
    boolean existsByRoleName(String roleName);
}