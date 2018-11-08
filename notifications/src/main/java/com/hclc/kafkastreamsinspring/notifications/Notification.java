package com.hclc.kafkastreamsinspring.notifications;

import com.hclc.kafkastreamsinspring.customers.Customer;
import com.hclc.kafkastreamsinspring.orders.Order;
import com.hclc.kafkastreamsinspring.payments.Payment;

public class Notification {

    private final Order order;
    private final Payment payment;
    private Customer customer;

    public Notification(Order order, Payment payment) {
        this.order = order;
        this.payment = payment;
    }

    public Order getOrder() {
        return order;
    }

    public Payment getPayment() {
        return payment;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Notification setCustomer(Customer customer) {
        this.customer = customer;
        return this;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "order=" + order +
                ", payment=" + payment +
                ", customer=" + customer +
                '}';
    }
}
