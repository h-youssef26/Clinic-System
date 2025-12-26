package com.clinic.system.service;

import com.clinic.system.model.Appointment;
import com.clinic.system.model.User;
import com.clinic.system.repository.AppointmentRepository;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.util.List;
import com.clinic.system.model.Doctor;
import com.clinic.system.model.Appointment.AppointmentStatus;
import java.time.LocalDateTime;
import java.util.Optional;
@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public Appointment createAppointment(Appointment appointment) {

        return appointmentRepository.save(appointment);
    }

    @Transactional
    public Appointment updateAppointment(Long id, Appointment updatedAppointment) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        appointment.setReason(updatedAppointment.getReason());
        appointment.setAppointmentTime(updatedAppointment.getAppointmentTime());
        appointment.setStatus(updatedAppointment.getStatus());
        appointment.setDoctor(updatedAppointment.getDoctor());

        return appointment;
    }

    public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public List<Appointment> getAppointmentsByPatient(User patient) {
        return appointmentRepository.findByPatient(patient);
    }

    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
    }
    public List<Appointment> getAppointmentsByDoctor(Doctor doctor) {
        return appointmentRepository.findByDoctorId(doctor.getId());
    }

    // Cancel appointment (only if APPROVED)
    public Appointment cancelAppointment(Long id, User patient) {
        Appointment appointment = getAppointmentById(id);

        if (!appointment.getPatient().getId().equals(patient.getId())) {
            throw new RuntimeException("You are not allowed to cancel this appointment.");
        }

        if (appointment.getStatus() != Appointment.AppointmentStatus.APPROVED) {
            // Instead of cancelling, inform patient to wait
            throw new RuntimeException("Your appointment is still pending. Please wait for admin approval.");
        }

        appointment.setStatus(Appointment.AppointmentStatus.CANCELLED);
        return appointmentRepository.save(appointment);
    }

    // ------------------- Admin Methods -------------------

    // Approve appointment
    public Appointment approveAppointment(Long id) {
        Appointment appointment = getAppointmentById(id);
        appointment.setStatus(AppointmentStatus.APPROVED);

        // Ensure it has some date/time so frontend can display it
        // Set appointmentTime if null
        if (appointment.getAppointmentTime() == null) {
            appointment.setAppointmentTime(LocalDateTime.now());
        }

        // Set dateTime if null (optional, depending on frontend needs)
        if (appointment.getDateTime() == null) {
            appointment.setDateTime(appointment.getAppointmentTime());
        }
        return appointmentRepository.save(appointment);
    }

    // Deny appointment
    public Appointment denyAppointment(Long id) {
        Appointment appointment = getAppointmentById(id);
        appointment.setStatus(AppointmentStatus.DENIED);
        // Set appointmentTime if null
        if (appointment.getAppointmentTime() == null) {
            appointment.setAppointmentTime(LocalDateTime.now());
        }

        // Set dateTime if null (optional, depending on frontend needs)
        if (appointment.getDateTime() == null) {
            appointment.setDateTime(appointment.getAppointmentTime());
        }
        return appointmentRepository.save(appointment);
    }

    // Get appointments by status
    public List<Appointment> getAppointmentsByStatus(AppointmentStatus status) {
        return appointmentRepository.findByStatus(status);
    }




}
