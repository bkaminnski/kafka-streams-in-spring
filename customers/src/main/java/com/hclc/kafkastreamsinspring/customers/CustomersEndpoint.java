package com.hclc.kafkastreamsinspring.customers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController("/customers")
public class CustomersEndpoint {

    @PostMapping
    public ResponseEntity<Customer> create(@RequestBody Customer customer) {
        customer.setCustomerId(UUID.randomUUID().toString());
        return ResponseEntity.ok(customer);
    }
}
