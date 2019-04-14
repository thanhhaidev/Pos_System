package com.thanhhaidev.headquarter.repositories;

import java.util.Optional;

import com.thanhhaidev.headquarter.models.Role;
import com.thanhhaidev.headquarter.models.RoleName;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleName roleName);
}