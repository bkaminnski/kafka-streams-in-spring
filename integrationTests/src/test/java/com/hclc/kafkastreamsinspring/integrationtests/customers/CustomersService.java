package com.hclc.kafkastreamsinspring.integrationtests.customers;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;

public class CustomersService {

    private static final String ENDPOINT = "http://localhost:8083";
    private static final String CUSTOMERS = "/customers";

    private final WebTarget target;

    public CustomersService() {
        Client client = ClientBuilder.newClient();
        target = client.target(ENDPOINT);
    }

    public Customer createCustomer(Customer customer) {
        return target.path(CUSTOMERS)
                .request()
                .post(Entity.json(customer))
                .readEntity(Customer.class);
    }
}
