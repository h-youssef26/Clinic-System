package com.clinic.system.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "appointments")
@Getter
@Setter
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reason;

    private LocalDateTime appointmentTime;
    private LocalDateTime dateTime; // <-- this is required

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id")
    @JsonIgnoreProperties({"user"}) // ignore nested user to avoid skipping status
    private User patient; // The patient creating the appointment

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "doctor_id")
    @JsonIgnoreProperties({"user"}) // ignore nested user to avoid skipping status
    private Doctor doctor; // Doctor for this appointment

    public enum AppointmentStatus {
        PENDING,
        APPROVED,
        DENIED,
        CANCELLED,
        COMPLETED,
        SCHEDULED
    }
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
    private Double amount; // Add this to store payment amount
    private String paymentStatus;
}
