package com.thanhhaidev.headquarter.services;

import java.util.List;

import com.thanhhaidev.headquarter.models.Customer;
import com.thanhhaidev.headquarter.repositories.CustomerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("customerService")
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public Customer findById(int id) {
        return customerRepository.findById(id);
    }

    public List<Customer> getAllCustomer() {
        return customerRepository.findAll();
    }
}