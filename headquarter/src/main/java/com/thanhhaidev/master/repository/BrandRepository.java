package com.thanhhaidev.master.repository;

import com.thanhhaidev.master.model.Brand;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Long> {
}