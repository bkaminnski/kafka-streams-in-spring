package com.hclc.kafkastreamsinspring.integrationtests.orders;

import java.util.concurrent.ThreadLocalRandom;

public class OrderIdProvider {

    private OrderIdProvider() {
        throw new AssertionError();
    }

    public static String randomOrderIdForSuccessfulPayment() {
        return String.valueOf(randomOddNumber());
    }

    public static String randomOrderIdForMismatchedPayment() {
        return String.valueOf(randomEvenNumber());
    }

    private static long randomOddNumber() {
        return ThreadLocalRandom.current().nextInt() * 2L - 1;
    }

    private static long randomEvenNumber() {
        return ThreadLocalRandom.current().nextInt() * 2L;
    }
}
