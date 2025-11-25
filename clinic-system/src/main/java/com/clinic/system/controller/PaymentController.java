package com.clinic.system.controller;

import com.clinic.system.model.OnlinePayment;
import com.clinic.system.model.Payment;
import com.clinic.system.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/cash")
    public ResponseEntity<Payment> createCashPayment(
            @RequestParam Long invoiceId,
            @RequestParam BigDecimal amount
    ) {
        return ResponseEntity.ok(paymentService.createCashPayment(invoiceId, amount));
    }

    @PostMapping("/online")
    public ResponseEntity<OnlinePayment> createOnlinePayment(
            @RequestParam Long invoiceId,
            @RequestParam BigDecimal amount,
            @RequestParam String provider,
            @RequestParam String transactionId
    ) {
        return ResponseEntity.ok(paymentService.createOnlinePayment(invoiceId, amount, provider, transactionId));
    }

    @GetMapping("/by-invoice/{invoiceId}")
    public ResponseEntity<List<Payment>> getPaymentsForInvoice(@PathVariable Long invoiceId) {
        return ResponseEntity.ok(paymentService.getPaymentsForInvoice(invoiceId));
    }
}
