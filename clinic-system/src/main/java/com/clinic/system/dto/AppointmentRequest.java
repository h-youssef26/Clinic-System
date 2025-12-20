package com.clinic.system.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
public class AppointmentRequest {
    private Long doctorId;
    private LocalDateTime appointmentTime; // matches your entity
    private String reason;
}
