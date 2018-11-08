package com.hclc.kafkastreamsinspring.orders;

import com.hclc.kafkastreamsinspring.payments.Payment;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static com.hclc.kafkastreamsinspring.orders.OrderStatus.PAID;
import static com.hclc.kafkastreamsinspring.orders.OrderStatus.PAYMENT_MISMATCH;
import static org.apache.kafka.common.serialization.Serdes.String;

/**
 * In this case, a stream is defined when PaymentsToOrdersStream bean is created. The official Spring
 * documentation presents another way to declare a @Bean factory in configuration and define stream processing here.
 * "@Bean public KStream<Integer, String> kStream(StreamsBuilder kStreamBuilder) { ..."
 * (https://docs.spring.io/spring-kafka/docs/2.1.10.RELEASE/reference/html/_reference.html#_kafka_streams_example).
 * <p>
 * To me it feels little hackish, since the bean created this way is not really important, and just keeping
 * the definition in a configuration class might be a risk for violating single responsibility principle as
 * the code evolves. In my case, I declare PaymentsToOrdersStream component which also is not important on its own,
 * but at least it gives a chance to put all dependencies for a single stream in a single place, and to build/expand from here.
 * <p>
 * What's important, is that stream has to be defined using the injected streamBuilder BEFORE StreamsBuilderFactoryBean finishes
 * its lifecycle (which happens when start() method is called on StreamsBuilderFactoryBean). When start() is called on
 * StreamsBuilderFactoryBean, a KafkaStream object is created using all the stream definitions already prepared using
 * streamBuilder singleton and KafkaStream is started ("this.kafkaStreams.start();").
 */
@Component
public class PaymentsToOrdersStream {

    private static final Logger log = Logger.getLogger(OrdersStreamsConfig.class.getName());

    @Autowired
    public PaymentsToOrdersStream(StreamsBuilder streamBuilder) {
        initializeStream(streamBuilder);
    }

    private void initializeStream(StreamsBuilder streamBuilder) {
        streamBuilder
                .stream("payments", Consumed.with(String(), paymentsSerde()))
                .peek((k, v) -> log.fine("key = " + k + "; payment = " + v))
                .map((k, v) -> new KeyValue<>(v.getOrderId(), new Order(
                        v.getOrderId(),
                        v.getCustomerId(),
                        v.getAmountReceived(),
                        v.getAmountReceived().intValue() % 2 == 0 ? PAYMENT_MISMATCH : PAID
                )))
                .peek((k, v) -> log.fine("key = " + k + "; order = " + v))
                .to("orders", Produced.with(String(), ordersSerde()));
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
