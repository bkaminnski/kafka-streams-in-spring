package com.hclc.kafkastreamsinspring.integrationtests.payments;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;

public class PaymentsService {

    private static final String ENDPOINT = "http://localhost:8082";
    private static final String PAYMENTS = "/payments";

    private final WebTarget target;

    public PaymentsService() {
        Client client = ClientBuilder.newClient();
        target = client.target(ENDPOINT);
    }

    public Payment createPayment(Payment payment) {
        return target.path(PAYMENTS)
                .request()
                .post(Entity.json(payment))
                .readEntity(Payment.class);
    }
}
