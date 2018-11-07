package com.hclc.kafkastreamsinspring.orders;

import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.processor.WallclockTimestampExtractor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;

import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.common.serialization.Serdes.String;

@Configuration
@EnableKafka
@EnableKafkaStreams
public class OrdersStreamsConfig {

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${kafka.streams.state-dir}")
    private String stateDir;

    @Value("${kafka.bootstrap-servers}")
    private String kafkaBootstrapServers;

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public KafkaStreamsConfiguration kafkaStreamsConfig() {
        Map<String, Object> props = new HashMap<>();
        // used as client.id prefix and group.id in regular consumer
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, applicationName);
        props.put(StreamsConfig.STATE_DIR_CONFIG, stateDir);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServers);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, WallclockTimestampExtractor.class.getName());
        props.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, "exactly_once");
        return new KafkaStreamsConfiguration(props);
    }
}