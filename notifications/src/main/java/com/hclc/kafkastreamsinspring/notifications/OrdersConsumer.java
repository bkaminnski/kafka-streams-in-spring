package com.hclc.kafkastreamsinspring.notifications;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OrdersConsumer {

    @Transactional
    @KafkaListener(topics = "orders")
    public void consume(ConsumerRecord<String, String> record) {
        System.out.println("order = " + record.value());
    }
}
