package com.thanhhaidev.headquarter.repository;

import com.thanhhaidev.headquarter.model.Customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query(value = "SELECT * FROM customer c WHERE c.phone = ?1", nativeQuery = true)
    Customer findCustomerByPhone(String phone);
}