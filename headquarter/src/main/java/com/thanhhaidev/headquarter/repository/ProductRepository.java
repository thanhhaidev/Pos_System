package com.thanhhaidev.headquarter.repository;

import java.util.List;

import com.thanhhaidev.headquarter.model.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "SELECT * FROM product p WHERE p.brand_id = ?1 AND (Date(p.created_date) = CURDATE() OR Date(p.last_modified_date) = CURDATE())", nativeQuery = true)
    List<Product> findProductByBrandId(Long brandId);
}