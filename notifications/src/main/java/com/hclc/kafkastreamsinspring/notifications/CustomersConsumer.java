package com.hclc.kafkastreamsinspring.notifications;

import com.hclc.kafkastreamsinspring.customers.Customer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CustomersConsumer {

    @Transactional
    @KafkaListener(topics = "customers")
    public void consume(ConsumerRecord<String, Customer> record) {
        System.out.println("record.value() = " + record.value());
    }
}
