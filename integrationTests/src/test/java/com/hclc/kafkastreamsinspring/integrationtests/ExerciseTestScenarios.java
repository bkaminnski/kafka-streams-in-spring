package com.hclc.kafkastreamsinspring.integrationtests;

import com.hclc.kafkastreamsinspring.integrationtests.customers.Customer;
import com.hclc.kafkastreamsinspring.integrationtests.customers.CustomersService;
import com.hclc.kafkastreamsinspring.integrationtests.payments.Payment;
import com.hclc.kafkastreamsinspring.integrationtests.payments.PaymentsService;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetup;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.math.BigDecimal;

import static com.hclc.kafkastreamsinspring.integrationtests.payments.PaymentAmountProvider.randomAmountForMismatchedPayment;
import static com.hclc.kafkastreamsinspring.integrationtests.payments.PaymentAmountProvider.randomAmountForSuccessfulPayment;
import static com.hclc.kafkastreamsinspring.integrationtests.payments.PaymentType.PayPal;
import static com.icegreen.greenmail.util.ServerSetup.PROTOCOL_SMTP;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;

public class ExerciseTestScenarios {

    private static final int SECOND = 1000;
    private GreenMail greenMail;
    private Customer customer;
    private Payment payment;

    @BeforeEach
    public void beforeEach() {
        greenMail = new GreenMail(new ServerSetup(2525, "localhost", PROTOCOL_SMTP));
        greenMail.start();
        customer = new CustomersService().createCustomer(new Customer("Bartosz Kaminski", "bartosz.kaminski@zoho.com"));
    }

    @AfterEach
    public void afterAll() {
        greenMail.stop();
    }

    @Test
    public void whenPaymentIsCompletedSuccessfully_shouldSendPositiveEmailNotification() throws MessagingException {
        payment = new PaymentsService().createPayment(
                new Payment(randomUUID().toString(), customer.getCustomerId(), new BigDecimal(randomAmountForSuccessfulPayment()), PayPal)
        );

        assertThatEmailNotificationIsPositive();
    }

    @Test
    public void whenPaymentIsCompletedWithMismatch_shouldSendNegativeEmailNotification() throws MessagingException {
        payment = new PaymentsService().createPayment(
                new Payment(randomUUID().toString(), customer.getCustomerId(), new BigDecimal(randomAmountForMismatchedPayment()), PayPal)
        );

        assertThatEmailNotificationIsNegative();
    }

    private void assertThatEmailNotificationIsPositive() throws MessagingException {
        MimeMessage receivedMessage = readFirstEmail();

        String body = GreenMailUtil.getBody(receivedMessage);
        assertThat(body).contains("Dear " + customer.getName() + ",");
        assertThat(body).contains("Payment for your order was successfully processed.");
        assertThat(body).contains("Order ID: " + payment.getOrderId());
        assertThat(body).contains("Total order value: " + payment.getAmountReceived());
        assertThat(body).contains("Payment type: " + payment.getPaymentType());

        String recipients = GreenMailUtil.getAddressList(receivedMessage.getAllRecipients());
        assertThat(recipients).isEqualTo(customer.getEmail());
    }

    private void assertThatEmailNotificationIsNegative() throws MessagingException {
        MimeMessage receivedMessage = readFirstEmail();

        String body = GreenMailUtil.getBody(receivedMessage);
        assertThat(body).contains("Dear " + customer.getName() + ",");
        assertThat(body).contains("Unfortunately there was a problem processing your payment. Our support will contact you.");
        assertThat(body).contains("Order ID: " + payment.getOrderId());
        assertThat(body).contains("Amount received: " + payment.getAmountReceived());
        assertThat(body).contains("Payment type: " + payment.getPaymentType());

        String recipients = GreenMailUtil.getAddressList(receivedMessage.getAllRecipients());
        assertThat(recipients).isEqualTo(customer.getEmail());
    }

    private MimeMessage readFirstEmail() {
        greenMail.waitForIncomingEmail(60 * SECOND, 1);
        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        assertThat(receivedMessages).hasSize(1);
        return receivedMessages[0];
    }
}
