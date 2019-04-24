package com.thanhhaidev.pos.controller;

import java.util.List;
import com.thanhhaidev.pos.model.Order;
import com.thanhhaidev.pos.repository.OrderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;;

    @GetMapping("/users/{userId}/orders")
    public List<Order> getAllOrdersByUserId(@PathVariable Long userId) {
        return orderRepository.findByUserId(userId);
    }

    // @PostMapping("/order/{customerId}/{userId}")
    // public Order createOrder(@PathVariable Long customerId, @PathVariable Long
    // userId,
    // @Valid @RequestBody Order order) {

    // }

}