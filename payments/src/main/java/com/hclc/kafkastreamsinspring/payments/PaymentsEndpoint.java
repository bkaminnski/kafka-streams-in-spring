package com.hclc.kafkastreamsinspring.payments;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private KafkaTemplate<String, String> template;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping
    @Transactional
    public ResponseEntity<Payment> accept(@RequestBody Payment payment) throws JsonProcessingException {
        payment.setPaymentId(UUID.randomUUID().toString());
        payment.setPaymentDate(new Date());
        template.send("payments", payment.getOrderId(), objectMapper.writeValueAsString(payment));
        return ResponseEntity.ok(payment);
    }
}
