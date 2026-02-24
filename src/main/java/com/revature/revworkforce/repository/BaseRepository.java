package com.revature.revworkforce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;


import com.revature.revworkforce.model.Role;

@NoRepositoryBean
public interface BaseRepository<T extends Role>
        extends JpaRepository<T, Long> {
	
	// Demo repository, for git tracking
}