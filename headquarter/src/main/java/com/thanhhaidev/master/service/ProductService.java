package com.thanhhaidev.master.service;

import java.util.List;
import java.util.Optional;

import com.thanhhaidev.master.model.Product;
import com.thanhhaidev.master.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public List<Product> getListProduct() {
        return productRepository.findAll();
    }
}