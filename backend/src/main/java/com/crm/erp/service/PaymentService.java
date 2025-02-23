package com.crm.erp.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class PaymentService {

    @PostConstruct
    public void init() {
        Stripe.apiKey = System.getenv("STRIPE_SECRET");
        log.info("Stripe API Key initialized.");
    }

    /**
     * Creates a generic payment intent.
     *
     * @param amount   Payment amount in the smallest currency unit.
     * @param currency Currency code, e.g., "usd".
     * @return A map containing the client secret.
     * @throws StripeException if payment creation fails.
     */
    public Map<String, Object> createPayment(Long amount, String currency) throws StripeException {
        Map<String, Object> params = new HashMap<>();
        params.put("amount", amount);
        params.put("currency", currency);

        PaymentIntent paymentIntent = PaymentIntent.create(params);

        Map<String, Object> response = new HashMap<>();
        response.put("clientSecret", paymentIntent.getClientSecret());
        return response;
    }

    /**
     * Processes an invoice payment and includes the projectId.
     *
     * @param invoiceId The ID of the invoice being paid.
     * @param projectId The ID of the project associated with this payment.
     * @param amount    Payment amount in the smallest currency unit.
     * @param currency  Currency code, e.g., "usd".
     * @return A map containing payment details.
     * @throws StripeException if payment creation fails.
     */
    public Map<String, Object> processInvoicePayment(Long invoiceId, Long projectId, Long amount, String currency) throws StripeException {
        Map<String, Object> params = new HashMap<>();
        params.put("amount", amount);
        params.put("currency", currency);

        PaymentIntent paymentIntent = PaymentIntent.create(params);

        Map<String, Object> response = new HashMap<>();
        response.put("invoiceId", invoiceId);
        response.put("projectId", projectId); // Include the projectId in the response.
        response.put("amount", amount);
        response.put("currency", currency);
        response.put("clientSecret", paymentIntent.getClientSecret());
        response.put("status", "Payment Initiated");
        return response;
    }
}
