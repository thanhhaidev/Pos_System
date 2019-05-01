package com.thanhhaidev.headquarter.repository;

import java.util.List;

import com.thanhhaidev.headquarter.model.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "SELECT * FROM product p WHERE p.brand_id = ?1 AND (Date(p.created_at) = DATE_ADD(CURDATE(), INTERVAL -1 DAY) OR Date(p.updated_at) = DATE_ADD(CURDATE(), INTERVAL -1 DAY))", nativeQuery = true)
    List<Product> findProductByBrandId(Long brandId);
}