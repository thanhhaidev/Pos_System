package com.thanhhaidev.pos.controller;

import java.net.URI;

import com.thanhhaidev.pos.model.OrderDetail;
import com.thanhhaidev.pos.repository.OrderDetailRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1")
public class OrderDetailController {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @PostMapping("/orderdetail")
    public ResponseEntity<Object> createOrder(@RequestBody OrderDetail detail) {
        orderDetailRepository.save(detail);

        // Set the location header for the newly created resource
        HttpHeaders responseHeaders = new HttpHeaders();
        URI newPollUri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(detail.getId())
                .toUri();
        responseHeaders.setLocation(newPollUri);
        return new ResponseEntity<>("Order is created successfully", HttpStatus.CREATED);
    }
}