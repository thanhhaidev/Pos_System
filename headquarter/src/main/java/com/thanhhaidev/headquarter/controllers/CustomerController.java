package com.thanhhaidev.headquarter.controllers;

import java.util.List;

import com.thanhhaidev.headquarter.models.Customer;
import com.thanhhaidev.headquarter.services.CustomerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @GetMapping("/customers")
    public List<Customer> all() {
        return customerService.getAllCustomer();
    }
}