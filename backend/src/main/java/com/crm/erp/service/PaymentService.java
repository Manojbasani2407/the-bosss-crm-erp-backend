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

    public Map<String, Object> createPayment(Long amount, String currency) throws StripeException {
        Map<String, Object> params = new HashMap<>();
        params.put("amount", amount);
        params.put("currency", currency);

        PaymentIntent paymentIntent = PaymentIntent.create(params);

        Map<String, Object> response = new HashMap<>();
        response.put("clientSecret", paymentIntent.getClientSecret());
        return response;
    }

    public Map<String, Object> processInvoicePayment(Long invoiceId, Long amount, String currency) throws StripeException {
        Map<String, Object> params = new HashMap<>();
        params.put("amount", amount);
        params.put("currency", currency);

        PaymentIntent paymentIntent = PaymentIntent.create(params);

        Map<String, Object> response = new HashMap<>();
        response.put("invoiceId", invoiceId);
        response.put("amount", amount);
        response.put("currency", currency);
        response.put("clientSecret", paymentIntent.getClientSecret());
        response.put("status", "Payment Initiated");
        return response;
    }
}
