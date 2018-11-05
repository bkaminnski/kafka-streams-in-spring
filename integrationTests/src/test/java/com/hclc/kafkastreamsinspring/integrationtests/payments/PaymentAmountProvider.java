package com.hclc.kafkastreamsinspring.integrationtests.payments;

import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Math.abs;

public class PaymentAmountProvider {

    private PaymentAmountProvider() {
        throw new AssertionError();
    }

    public static long randomAmountForSuccessfulPayment() {
        return randomOddNumber();
    }

    public static long randomAmountForMismatchedPayment() {
        return randomEvenNumber();
    }

    private static long randomOddNumber() {
        return abs(ThreadLocalRandom.current().nextInt()) * 2L - 1;
    }

    private static long randomEvenNumber() {
        return abs(ThreadLocalRandom.current().nextInt()) * 2L;
    }
}
