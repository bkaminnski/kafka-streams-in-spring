package com.hclc.kafkastreamsinspring.customers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController("/customers")
public class CustomersEndpoint {

    @Autowired
    private KafkaTemplate<String, Customer> template;

    @PostMapping
    @Transactional
    public ResponseEntity<Customer> create(@RequestBody Customer customer) {
        customer.setCustomerId(UUID.randomUUID().toString());
        template.send("customers", customer.getCustomerId(), customer);
        return ResponseEntity.ok(customer);
    }
}
