package com.clinic.system.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;

@Entity
@Table(name = "prescriptions")
@Getter
@Setter
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    @JsonIgnoreProperties({"user", "password"})
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    @JsonIgnoreProperties({"doctor", "password", "verificationCode", "verificationCodeExpiresAt", "authorities", "accountNonExpired", "accountNonLocked", "credentialsNonExpired"})
    private User patient;

    @Column(columnDefinition = "TEXT")
    private String medications; // List of medications

    @Column(columnDefinition = "TEXT")
    private String instructions; // Instructions for taking medications

    @Column(columnDefinition = "TEXT")
    private String diagnosis; // Diagnosis/condition

    @Column(columnDefinition = "TEXT")
    private String notes; // Additional notes

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}

