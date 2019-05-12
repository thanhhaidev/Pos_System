package com.thanhhaidev.pos.service;

import java.util.Optional;

import com.thanhhaidev.pos.model.Order;
import com.thanhhaidev.pos.repository.OrderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order calculatorPointOfCustomer(Order order) {
        return this.orderRepository.save(order);
    }

}