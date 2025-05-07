package com.julia.authapi.repository;

import com.julia.authapi.model.entity.Role;
import com.julia.authapi.model.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}