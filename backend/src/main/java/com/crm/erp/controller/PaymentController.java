package com.crm.erp.controller;

import com.crm.erp.model.payment;
import com.crm.erp.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class PaymentController {

    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentController(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    // GET /api/payments?clientId=123
    @GetMapping("/payments")
    public List<payment> getPayments(@RequestParam(required = false) Long clientId) {
        if (clientId != null) {
            log.info("Fetching payments for client: {}", clientId);
            return paymentRepository.findByClientId(clientId);
        }
        log.info("Fetching all payments");
        return paymentRepository.findAll();
    }

    // POST /api/payments - to create a new payment record
    @PostMapping("/payments")
    public payment createPayment(@RequestBody payment payment) {
        payment.setPaymentDate(LocalDateTime.now());
        payment.setStatus("Completed");
        log.info("Creating payment for client: {}", payment.getClientId());
        return paymentRepository.save(payment);
    }
}
