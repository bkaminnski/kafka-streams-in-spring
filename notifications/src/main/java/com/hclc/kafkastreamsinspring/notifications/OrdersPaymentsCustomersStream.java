package com.hclc.kafkastreamsinspring.notifications;

import com.hclc.kafkastreamsinspring.customers.Customer;
import com.hclc.kafkastreamsinspring.orders.Order;
import com.hclc.kafkastreamsinspring.payments.Payment;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

import static com.hclc.kafkastreamsinspring.notifications.SerdeFactory.serdeForType;
import static org.apache.kafka.common.serialization.Serdes.String;

@Component
public class OrdersPaymentsCustomersStream {

    private static final Logger log = Logger.getLogger(OrdersPaymentsCustomersStream.class.getName());

    private static final int MINUTE = 60 * 1000;

    @Autowired
    public OrdersPaymentsCustomersStream(StreamsBuilder streamBuilder) {
        initializeStream(streamBuilder);
    }

    private void initializeStream(StreamsBuilder streamBuilder) {
        KStream<String, Order> orders = streamBuilder
                .stream("orders", Consumed.with(String(), serdeForType(Order.class)));

        KStream<String, Payment> payments = streamBuilder
                .stream("payments", Consumed.with(String(), serdeForType(Payment.class)));

        // "local GlobalKTable instance of every application instance will be populated with data from all input topic partitions"
        // as opposed to KTable: "the local KTable instance of every application instance will be populated with data from only
        // a subset of the partitions of the input topic"
        // "If possible, consider using global tables (GlobalKTable) for joining because they do not require data co-partitioning."
        // (https://docs.confluent.io/current/streams/developer-guide/dsl-api.html#streams-developer-guide-dsl-transformations-stateless)
        GlobalKTable<String, Customer> customers = streamBuilder
                .globalTable("customers", Consumed.with(String(), serdeForType(Customer.class)), Materialized.as("customers-store"));

        orders
                .peek((k, v) -> log.fine("order received: " + v))
                .join(payments, Notification::new, JoinWindows.of(5 * MINUTE), Joined.with(String(), serdeForType(Order.class), serdeForType(Payment.class)))
                .selectKey((k, v) -> v.getOrder().getCustomerId())
                .peek((k, v) -> log.fine("key = " + k + "; notification (order, payment) = " + v))
                .join(customers, (k, v) -> k, (n, c) -> n.setCustomer(c))
                .selectKey((k, v) -> v.getOrder().getOrderId())
                .peek((k, v) -> log.fine("key = " + k + "; notification (order, payment, customer) = " + v))
                .foreach((k, v) -> log.fine("key = " + k + "; notification = " + v));
    }

}
