package com.hclc.kafkastreamsinspring.orders;

import com.hclc.kafkastreamsinspring.payments.Payment;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class PaymentsConsumer {

    @Transactional
    @KafkaListener(topics = "payments")
    public void consume(ConsumerRecord<String, Payment> record) {
        System.out.println("record.value() = " + record.value());
    }
}
