package com.thanhhaidev.headquarter.controller;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import com.google.gson.Gson;
import com.thanhhaidev.headquarter.model.Product;
import com.thanhhaidev.headquarter.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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

    @GetMapping("/products/brand/{brandId}")
    public String getProductByBrandId(@PathVariable Long brandId) throws ParseException {
        List<Product> lstProduct = productRepository.findProductByBrandId(brandId);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDateTime now = LocalDateTime.now();
        // System.out.println(dtf.format(now)); //2016/11/16 12:08:43
        try (FileWriter file = new FileWriter("E:/POS_System/headquarter/src/main/resources/database/product_"
                + dtf.format(now) + "_" + brandId + ".json")) {
            String json = new Gson().toJson(lstProduct);
            file.write(json);
            file.flush();
            return "Save Data Successfully";
        } catch (IOException e) {
            e.printStackTrace();
            return "Save Data Error: " + e.toString();
        }
    }

    @PostMapping("/products")
    public Product createProduct(@RequestBody Product product) {
        return productRepository.save(product);
    }
}