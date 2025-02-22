package com.crm.erp.controller;

import com.crm.erp.service.PaymentService;
import com.crm.erp.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * CRM Controller for handling system-wide functionalities.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CrmController {

    private final PaymentService paymentService;

    /**
     * Welcome message for the API.
     */
    @GetMapping("/")
    public ResponseEntity<String> home() {
        return ResponseEntity.ok("Welcome to the CRM ERP System!");
    }

    // ==============================
    // 🔹 PAYMENT MANAGEMENT
    // ==============================

    /**
     * Create a payment intent.
     */
    @PostMapping("/payment")
    public ResponseEntity<Map<String, Object>> createPaymentIntent(@RequestBody Map<String, Object> paymentData) {
        try {
            Long amount = ((Number) paymentData.get("amount")).longValue();
            String currency = (String) paymentData.get("currency");
            return ResponseEntity.ok(paymentService.createPayment(amount, currency));
        } catch (Exception e) {
            throw new ResourceNotFoundException("Invalid payment data: " + e.getMessage());
        }
    }
}
