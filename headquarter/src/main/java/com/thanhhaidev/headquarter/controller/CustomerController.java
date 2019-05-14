package com.thanhhaidev.headquarter.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.thanhhaidev.headquarter.exception.ResourceNotFoundException;
import com.thanhhaidev.headquarter.model.Customer;
import com.thanhhaidev.headquarter.repository.CustomerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping("/customers")
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @GetMapping("/customers/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable(value = "id") Long customerId)
            throws ResourceNotFoundException {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found :: " + customerId));
        return ResponseEntity.ok().body(customer);
    }

    @GetMapping("/customers/phone/{phone}")
    public ResponseEntity<Customer> getCustomerByPhone(@PathVariable(value = "phone") String phone)
            throws ResourceNotFoundException {
        Customer customer = customerRepository.findCustomerByPhone(phone);
        return ResponseEntity.ok().body(customer);
    }

    @PostMapping("/customers")
    public Customer createUser(@Valid @RequestBody Customer customer) {
        return customerRepository.save(customer);
    }

    @PutMapping("/customers/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable(value = "id") Long customerId,
            @Valid @RequestBody Customer cutsomerDetails) throws ResourceNotFoundException {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found :: " + customerId));

        customer.setName(cutsomerDetails.getName());
        customer.setPhone(cutsomerDetails.getPhone());
        customer.setLastModifiedDate(new Date());
        final Customer updatedCustomer = customerRepository.save(customer);
        return ResponseEntity.ok(updatedCustomer);
    }

    @PutMapping("/customers/phone/{phone}")
    public ResponseEntity<Customer> updateCustomerByPhone(@PathVariable(value = "phone") String phone,
            @Valid @RequestBody Customer cutsomerDetails) throws ResourceNotFoundException {
        Customer customer = customerRepository.findCustomerByPhone(phone);
        customer.setName(cutsomerDetails.getName());
        customer.setPoint(cutsomerDetails.getPoint());
        customer.setLastModifiedDate(new Date());
        final Customer updatedCustomer = customerRepository.save(customer);
        return ResponseEntity.ok(updatedCustomer);
    }

    @DeleteMapping("/customers/{id}")
    public Map<String, Boolean> deleteCustomer(@PathVariable(value = "id") Long customerId)
            throws ResourceNotFoundException {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found :: " + customerId));

        customerRepository.delete(customer);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}