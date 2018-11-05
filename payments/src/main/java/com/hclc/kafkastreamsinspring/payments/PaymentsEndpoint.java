package com.hclc.kafkastreamsinspring.payments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

@RestController("/payments")
public class PaymentsEndpoint {

    @Autowired
    private KafkaTemplate<String, Payment> template;

    @PostMapping
    @Transactional
    public ResponseEntity<Payment> accept(@RequestBody Payment payment) {
        payment.setPaymentId(UUID.randomUUID().toString());
        payment.setPaymentDate(new Date());
        template.send("payments", payment.getOrderId(), payment);
        return ResponseEntity.ok(payment);
    }
}
