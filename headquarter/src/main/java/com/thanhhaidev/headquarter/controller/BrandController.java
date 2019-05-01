package com.thanhhaidev.headquarter.controller;

import java.util.List;

import com.thanhhaidev.headquarter.model.Brand;
import com.thanhhaidev.headquarter.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class BrandController {
    @Autowired
    private BrandRepository brandRepository;

    @GetMapping("/products/brand")
    public List<Brand> getAllProductByBrandId() {
        return brandRepository.findAll();
    }
}