package com.clinic.system.service.impl;

import com.clinic.system.model.Invoice;
import com.clinic.system.model.OnlinePayment;
import com.clinic.system.model.Payment;
import com.clinic.system.repository.InvoiceRepository;
import com.clinic.system.repository.PaymentRepository;
import com.clinic.system.service.PaymentService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository,
                              InvoiceRepository invoiceRepository) {
        this.paymentRepository = paymentRepository;
        this.invoiceRepository = invoiceRepository;
    }

    @Transactional
    @Override
    public Payment createCashPayment(Long invoiceId, BigDecimal amount) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + invoiceId));

        Payment payment = new Payment(amount, "CASH", "COMPLETED", null, invoice);
        Payment saved = paymentRepository.save(payment);

        // Optional: mark invoice as PAID if fully covered
        invoice.setStatus("PAID");
        invoiceRepository.save(invoice);

        return saved;
    }

    @Transactional
    @Override
    public OnlinePayment createOnlinePayment(Long invoiceId, BigDecimal amount, String provider, String transactionId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + invoiceId));

        OnlinePayment payment = new OnlinePayment(provider, invoice, "COMPLETED");
        payment.setAmount(amount);
        payment.setTransactionId(transactionId);

        OnlinePayment saved = (OnlinePayment) paymentRepository.save(payment);

        invoice.setStatus("PAID");
        invoiceRepository.save(invoice);

        return saved;
    }

    @Override
    public List<Payment> getPaymentsForInvoice(Long invoiceId) {
        return paymentRepository.findByInvoiceId(invoiceId);
    }
}
