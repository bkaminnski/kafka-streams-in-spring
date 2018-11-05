package com.hclc.kafkastreamsinspring.payments;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.UUID;

@RestController("/payments")
public class PaymentsEndpoint {

    @PostMapping
    public ResponseEntity<Payment> accept(@RequestBody Payment payment) {
        payment.setPaymentId(UUID.randomUUID().toString());
        payment.setPaymentDate(Instant.now());
        return ResponseEntity.ok(payment);
    }
}
