package com.hclc.kafkastreamsinspring.integrationtests;

import com.hclc.kafkastreamsinspring.integrationtests.customers.Customer;
import com.hclc.kafkastreamsinspring.integrationtests.customers.CustomersService;
import com.hclc.kafkastreamsinspring.integrationtests.orders.OrderIdProvider;
import com.hclc.kafkastreamsinspring.integrationtests.payments.Payment;
import com.hclc.kafkastreamsinspring.integrationtests.payments.PaymentsService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static com.hclc.kafkastreamsinspring.integrationtests.payments.PaymentType.PayPal;

public class ExerciseTestScenarios {

    private String customerId;

    @BeforeEach
    public void beforeEach() {
        customerId = new CustomersService()
                .createCustomer(new Customer("Bartosz Kaminski", "bartosz.kaminski@zoho.com"))
                .getCustomerId();
    }

    @Test
    public void whenPaymentIsCompletedSuccessfully_shouldSendPositiveEmailNotification() {
        System.out.println("customerId = " + customerId);
        Payment payment = new PaymentsService().createPayment(new Payment(OrderIdProvider.randomOrderIdForSuccessfulPayment(), customerId, new BigDecimal("123.56"), PayPal));
        System.out.println("payment = " + payment);
        Assertions.fail("not implemented yet");
    }

    @Test
    public void whenPaymentIsCompletedWithMismatch_shouldSendNegativeEmailNotification() {
        System.out.println("customerId = " + customerId);
        Payment payment = new PaymentsService().createPayment(new Payment(OrderIdProvider.randomOrderIdForMismatchedPayment(), customerId, new BigDecimal("123.56"), PayPal));
        System.out.println("payment = " + payment);
        Assertions.fail("not implemented yet");
    }
}
