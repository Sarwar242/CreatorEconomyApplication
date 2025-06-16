package com.CreatorEconomyApplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.CreatorEconomyApplication.model.entity.Role;
import com.CreatorEconomyApplication.model.enums.RoleName;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    
    Optional<Role> findByName(RoleName name);
    
    boolean existsByName(RoleName name);
}