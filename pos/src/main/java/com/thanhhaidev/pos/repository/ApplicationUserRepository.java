package com.thanhhaidev.pos.repository;

import com.thanhhaidev.pos.model.ApplicationUser;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Long> {
    ApplicationUser findByUsername(String username);
}