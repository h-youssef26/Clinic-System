package com.clinic.system.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "online_payments")
public class OnlinePayment extends Payment {
    private String provider; // e.g. Stripe, PayPal
    private LocalDateTime processedAt;

    public OnlinePayment() {}

    public OnlinePayment(String provider, Invoice invoice, String status) {
        super(invoice.getTotalAmount(), "ONLINE", status, null, invoice); // set fields in parent
        this.provider = provider;
        this.processedAt = LocalDateTime.now();
    }

    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }

    public LocalDateTime getProcessedAt() { return processedAt; }
    public void setProcessedAt(LocalDateTime processedAt) { this.processedAt = processedAt; }
}
