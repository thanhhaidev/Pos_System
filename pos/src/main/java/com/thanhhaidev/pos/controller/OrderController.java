package com.thanhhaidev.pos.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.thanhhaidev.pos.model.Customer;
import com.thanhhaidev.pos.model.Order;
import com.thanhhaidev.pos.model.OrderDetail;
import com.thanhhaidev.pos.model.Product;
import com.thanhhaidev.pos.repository.CustomerRepository;
import com.thanhhaidev.pos.repository.OrderDetailRepository;
import com.thanhhaidev.pos.repository.OrderRepository;
import com.thanhhaidev.pos.repository.ProductRepository;
import com.thanhhaidev.pos.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository detailRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/users/{userId}/orders")
    public List<Order> getAllOrdersByUserId(@PathVariable Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @GetMapping("/orders")
    public List<Order> getListOrder() {
        return orderRepository.findAll();
    }

    @GetMapping("/orders/{id}")
    public Optional<Order> getOrderById(@PathVariable Long id) {
        return orderRepository.findById(id);
    }

    @PostMapping("/orders")
    public ResponseEntity<Order> createUser(@Valid @RequestBody Order order) {
        Order order2 = orderService.calculatorPointOfCustomer(order);

        Collection<OrderDetail> details = order2.getDetails();
        Optional<Customer> customer = customerRepository.findById(order2.getCustomer().getId());
        List<OrderDetail> lstDetail = new ArrayList<>(details);

        int count = 0;

        for (int i = 0; i < lstDetail.size(); i++) {
            Optional<Product> product = productRepository.findById(lstDetail.get(i).getProduct().getId());

            OrderDetail detail = new OrderDetail();

            int quantity = lstDetail.get(i).getQuantity();
            int total = quantity * product.get().getPrice();

            detail.setQuantity(lstDetail.get(i).getQuantity());
            detail.setProduct(product.get());
            detail.setTotal(total);
            detail.setMOrder(order2);

            detailRepository.save(detail);

            count += total;
        }

        int pointCustomerCurrent = customer.get().getPoint() * 1000;

        if (pointCustomerCurrent <= count) {
            count = count - pointCustomerCurrent;
            customer.get().setPoint(0);
            order2.setTotal(count);
        } else {
            pointCustomerCurrent = pointCustomerCurrent - count;
            order2.setTotal(0);
            customer.get().setPoint(pointCustomerCurrent / 1000);
        }
        customerRepository.save(customer.get());

        order2.setLastModifiedDate(new Date());

        final Order updatedOrder = orderRepository.save(order2);
        return ResponseEntity.ok(updatedOrder);
    }

}