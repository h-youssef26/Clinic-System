package com.clinic.system.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentRequest {
    private Long doctorId;
    private LocalDateTime appointmentTime; // matches your entity
    private String reason;
    private String paymentMethod; // "CASH" or "VISA"
    private Double amount; // Appointment fee (default 100.0 if not provided)
}
