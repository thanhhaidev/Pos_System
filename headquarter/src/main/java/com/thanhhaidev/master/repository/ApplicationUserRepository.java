package com.thanhhaidev.master.repository;

import com.thanhhaidev.master.model.ApplicationUser;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Long> {
    ApplicationUser findByUsername(String username);
}