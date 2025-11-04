package com.clinic.system.service;

import com.clinic.system.model.Appointment;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;
public interface AppointmentService {

    List<Appointment> getAllAppointments();
    Appointment getAppointmentById(Long id);
    Appointment saveAppointment(Appointment appointment);
    Appointment updateAppointment(Long id, Appointment appointment);
    void deleteAppointment(Long id);
    Appointment bookAppointment(Long patientId, Long doctorId, LocalDate date, LocalTime time, String notes);
    Appointment cancelAppointment(Long appointmentId, Long patientId);
    List<Appointment> getDoctorSchedule(Long doctorId);
    List<Appointment> getDoctorScheduleByDate(Long doctorId, LocalDate date);
    Appointment addPrescription(Long appointmentId, Long doctorId, String prescriptionText);
    List<Appointment> getPatientAppointments(Long patientId);
    Appointment rescheduleAppointment(Long appointmentId, Long patientId, LocalDate newDate, LocalTime newTime);

}
