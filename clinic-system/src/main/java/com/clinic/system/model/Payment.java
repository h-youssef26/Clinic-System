package com.clinic.system.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "appointment_id", nullable = false)
    @JsonIgnoreProperties({"paymentMethod", "amount", "paymentStatus", "createdAt", "status", "reason"}) // Only ignore specific fields to avoid circular reference, but keep patient and doctor
    private Appointment appointment;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private String paymentMethod; // "CASH" or "VISA"

    @Column(nullable = false)
    private String status; // "pending", "paid", "failed"

    @Column(name = "card_last4")
    private String cardLast4; // Last 4 digits of card (for VISA payments)

    @Column(name = "payment_date")
    private LocalDateTime paymentDate; // When payment was completed

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null) {
            status = "pending";
        }
    }


}


