package com.thanhhaidev.pos.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import net.minidev.json.JSONObject;

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

    private static final String URL_CUSTOMER = "http://localhost:4100/api/v1/customers/phone/";

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
    public ResponseEntity<Order> createUser(@Valid @RequestBody Order order,
            @RequestHeader(name = "Authorization") String token) {
        Order order2 = orderService.calculatorPointOfCustomer(order);
        Collection<OrderDetail> details = order2.getDetails();
        Optional<Customer> customer = customerRepository.findById(order2.getCustomer().getId());

        String authToken = token.split(" ")[1];

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("User-Agent", "Spring's RestTemplate"); // value can be whatever
        headers.add("Authorization", "Bearer " + authToken);

        HttpEntity<String> entityGetCustomer = new HttpEntity<String>(null, headers);

        ResponseEntity<String> responseGetCustomer = restTemplate.exchange(URL_CUSTOMER + customer.get().getPhone(),
                HttpMethod.GET, entityGetCustomer, String.class);
        if (responseGetCustomer.getStatusCode() == HttpStatus.OK) {
            try {
                Customer customer2 = new ObjectMapper().readValue(responseGetCustomer.getBody(), Customer.class);
                customer.get().setPoint(customer2.getPoint());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (responseGetCustomer.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            System.out.println("fail");
        }

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
            customer.get().setPoint(count / 1000);
            order2.setTotal(count);
        } else {
            pointCustomerCurrent = pointCustomerCurrent - count;
            order2.setTotal(0);
            customer.get().setPoint(pointCustomerCurrent / 1000);
        }
        customerRepository.save(customer.get());

        order2.setLastModifiedDate(new Date());

        System.out.println(token.split(" ")[1]);

        final Order updatedOrder = orderRepository.save(order2);

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("point", customer.get().getPoint());
        jsonObject.put("name", customer.get().getName());

        System.out.println(URL_CUSTOMER + customer.get().getPhone());

        HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(URL_CUSTOMER + customer.get().getPhone(),
                HttpMethod.PUT, entity, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            System.out.println(response);
            System.out.println("success");
        } else if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            System.out.println("fail");
        }

        return ResponseEntity.ok(updatedOrder);
    }

}