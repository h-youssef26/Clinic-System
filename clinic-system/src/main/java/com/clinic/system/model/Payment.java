package com.clinic.system.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;
    private String method; // CASH, CARD, ONLINE
    private String status; // COMPLETED, PENDING, FAILED
    private LocalDateTime paidAt;
    private String transactionId; // for online payments

    @ManyToOne
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;

    public Payment() {}

    @PrePersist
    protected void onCreate() {
        if(this.paidAt == null) this.paidAt = LocalDateTime.now();
    }

    public Payment(BigDecimal amount, String method, String status, String transactionId, Invoice invoice) {
        this.amount = amount;
        this.method = method;
        this.status = status;
        this.transactionId = transactionId;
        this.invoice = invoice;
        this.paidAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getPaidAt() { return paidAt; }
    public void setPaidAt(LocalDateTime paidAt) { this.paidAt = paidAt; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public Invoice getInvoice() { return invoice; }
    public void setInvoice(Invoice invoice) { this.invoice = invoice; }
}
