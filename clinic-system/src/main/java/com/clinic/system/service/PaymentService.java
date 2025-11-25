package com.clinic.system.service;

import com.clinic.system.model.OnlinePayment;
import com.clinic.system.model.Payment;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentService {

    Payment createCashPayment(Long invoiceId, BigDecimal amount);

    OnlinePayment createOnlinePayment(Long invoiceId, BigDecimal amount, String provider, String transactionId);

    List<Payment> getPaymentsForInvoice(Long invoiceId);
}
