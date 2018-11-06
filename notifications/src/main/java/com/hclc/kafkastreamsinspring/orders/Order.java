package com.hclc.kafkastreamsinspring.orders;

import java.math.BigDecimal;

public class Order {

    private String orderId, customerId;
    private BigDecimal totalValue;
    private OrderStatus orderStatus;

    public Order(String orderId, String customerId, BigDecimal totalValue, OrderStatus orderStatus) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.totalValue = totalValue;
        this.orderStatus = orderStatus;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
