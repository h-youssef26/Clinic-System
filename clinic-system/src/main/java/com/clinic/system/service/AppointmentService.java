package com.clinic.system.service;

import com.clinic.system.model.Appointment;
import java.util.List;

public interface AppointmentService {

    List<Appointment> getAllAppointments();
    Appointment getAppointmentById(Long id);
    Appointment saveAppointment(Appointment appointment);
    Appointment updateAppointment(Long id, Appointment appointment);
    void deleteAppointment(Long id);
}
