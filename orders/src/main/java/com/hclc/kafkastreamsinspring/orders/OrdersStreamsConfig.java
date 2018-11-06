package com.hclc.kafkastreamsinspring.orders;

import com.hclc.kafkastreamsinspring.payments.Payment;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.processor.WallclockTimestampExtractor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static com.hclc.kafkastreamsinspring.orders.OrderStatus.PAID;
import static com.hclc.kafkastreamsinspring.orders.OrderStatus.PAYMENT_MISMATCH;
import static org.apache.kafka.common.serialization.Serdes.String;

@Configuration
@EnableKafka
@EnableKafkaStreams
public class OrdersStreamsConfig {

    private static final Logger log = Logger.getLogger(OrdersStreamsConfig.class.getName());

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${kafka.streams.state-dir}")
    private String stateDir;

    @Value("${kafka.bootstrap-servers}")
    private String kafkaBootstrapServers;

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public org.springframework.kafka.config.KafkaStreamsConfiguration kStreamsConfigs() {
        Map<String, Object> props = new HashMap<>();
        // used as client.id prefix and group.id in regular consumer
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, applicationName);
        props.put(StreamsConfig.STATE_DIR_CONFIG, stateDir);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServers);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, WallclockTimestampExtractor.class.getName());
        props.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, "exactly_once");
        return new org.springframework.kafka.config.KafkaStreamsConfiguration(props);
    }

    @Bean
    public KStream<String, Payment> paymentsToOrders(StreamsBuilder streamBuilder) {
        KStream<String, Payment> stream = streamBuilder
                .stream("payments", Consumed.with(String(), paymentsSerde()));
        stream
                .map((k, v) -> new KeyValue<>(v.getOrderId(), new Order(
                        v.getOrderId(),
                        v.getCustomerId(),
                        v.getAmountReceived(),
                        v.getAmountReceived().intValue() % 2 == 0 ? PAYMENT_MISMATCH : PAID
                )))
                .peek((k, v) -> log.fine("order = " + v))
                .to("orders", Produced.with(String(), ordersSerde()));
        return stream;
    }

    private Serde<Payment> paymentsSerde() {
        Map<String, Object> serdeProps = new HashMap<>();

        final Serializer<Payment> paymentsSerializer = new JsonPOJOSerializer<>();
        serdeProps.put("JsonPOJOClass", Payment.class);
        paymentsSerializer.configure(serdeProps, false);

        final Deserializer<Payment> paymentsDeserializer = new JsonPOJODeserializer<>();
        serdeProps.put("JsonPOJOClass", Payment.class);
        paymentsDeserializer.configure(serdeProps, false);

        return Serdes.serdeFrom(paymentsSerializer, paymentsDeserializer);
    }

    private Serde<Order> ordersSerde() {
        Map<String, Object> serdeProps = new HashMap<>();

        final Serializer<Order> ordersSerializer = new JsonPOJOSerializer<>();
        serdeProps.put("JsonPOJOClass", Order.class);
        ordersSerializer.configure(serdeProps, false);

        final Deserializer<Order> ordersDeserializer = new JsonPOJODeserializer<>();
        serdeProps.put("JsonPOJOClass", Order.class);
        ordersDeserializer.configure(serdeProps, false);

        return Serdes.serdeFrom(ordersSerializer, ordersDeserializer);
    }
}