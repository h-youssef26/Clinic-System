package com.clinic.system.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

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

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private User patient; // The patient creating the appointment

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor; // Doctor for this appointment

    private String status; // Optional: Scheduled, Cancelled, Completed
}
