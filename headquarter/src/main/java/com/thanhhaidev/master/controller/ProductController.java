package com.thanhhaidev.master.controller;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.google.gson.Gson;
import com.thanhhaidev.master.model.Product;
import com.thanhhaidev.master.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import net.minidev.json.parser.ParseException;

@RestController
@RequestMapping("/api/v1")
public class ProductController {
    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/products")
    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

    @GetMapping("/products/{id}")
    public Optional<Product> getProductById(@PathVariable Long id) {
        return productRepository.findById(id);
    }

    @GetMapping("products/brand/{brandId}")
    public String getProductByBrandId(@PathVariable Long brandId) throws ParseException {
        List<Product> lstProduct = productRepository.findProductByBrandId(brandId);
        try (FileWriter file = new FileWriter(
                "E:/POS_System/headquarter/src/main/resources/product_" + brandId + ".json")) {
            String json = new Gson().toJson(lstProduct);
            file.write(json);
            file.flush();
            return "Save Data Successfully";
        } catch (IOException e) {
            e.printStackTrace();
            return "Save Data Error: " + e.toString();
        }
    }

}