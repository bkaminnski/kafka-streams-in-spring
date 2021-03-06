package com.hclc.kafkastreamsinspring.customers;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.transaction.KafkaTransactionManager;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CustomersProducerConfig {

    @Value("${kafka.bootstrap-servers}")
    private String kafkaBootstrapServers;

    @Value("${kafka.client-id}")
    private String clientId;

    @Bean
    public ProducerFactory<String, Customer> producerFactory() {
        DefaultKafkaProducerFactory<String, Customer> kafkaProducerFactory = new DefaultKafkaProducerFactory<>(producerConfigs());
        // required for the producer to be transactional
        kafkaProducerFactory.setTransactionIdPrefix("customersTX");
        return kafkaProducerFactory;
    }

    @Bean
    public KafkaTransactionManager<String, Customer> transactionManager(ProducerFactory<String, Customer> producerFactory) {
        return new KafkaTransactionManager<>(producerFactory);
    }

    private Map<String, Object> producerConfigs() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServers);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        properties.put(ProducerConfig.ACKS_CONFIG, "all");
        properties.put(ProducerConfig.CLIENT_ID_CONFIG, clientId);
        // highest guarantee of ordering; anything > 1 risks reordering in case of failed delivery
        properties.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 1);
        return properties;
    }

    @Bean
    public KafkaTemplate<String, Customer> kafkaTemplate(ProducerFactory<String, Customer> producerFactory) {
        KafkaTemplate<String, Customer> kafkaTemplate = new KafkaTemplate<>(producerFactory);
        return kafkaTemplate;
    }
}