package com.thanhhaidev.headquarter.repositories;

import com.thanhhaidev.headquarter.models.Customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("customerRepository")
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findById(int id);
}