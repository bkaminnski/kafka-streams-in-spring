package com.hclc.kafkastreamsinspring.integrationtests.payments;

import java.math.BigDecimal;
import java.util.Date;

public class Payment {

    private String paymentId, orderId, customerId;

    private Date paymentDate;

    private BigDecimal amountReceived;

    private PaymentType paymentType;

    public Payment() {
    }

    public Payment(String orderId, String customerId, BigDecimal amountReceived, PaymentType paymentType) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.amountReceived = amountReceived;
        this.paymentType = paymentType;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
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

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public BigDecimal getAmountReceived() {
        return amountReceived;
    }

    public void setAmountReceived(BigDecimal amountReceived) {
        this.amountReceived = amountReceived;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId='" + paymentId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", paymentDate=" + paymentDate +
                ", amountReceived=" + amountReceived +
                ", paymentType=" + paymentType +
                '}';
    }
}
