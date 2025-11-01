package com.clinic.system.service.impl;

import com.clinic.system.model.Appointment;
import com.clinic.system.model.Doctor;
import com.clinic.system.model.Patient;
import com.clinic.system.repository.AppointmentRepository;
import com.clinic.system.repository.DoctorRepository;
import com.clinic.system.repository.PatientRepository;
import com.clinic.system.service.AppointmentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository,
                                  DoctorRepository doctorRepository,
                                  PatientRepository patientRepository) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    public List<Appointment> getAllAppointments() {
        return (List<Appointment>) appointmentRepository.findAll();
    }

    @Override
    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
    }

    @Override
    public Appointment saveAppointment(Appointment appointment) {
        // Fetch full doctor and patient entities from DB
        Doctor doctor = doctorRepository.findById(appointment.getDoctor().getId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        Patient patient = patientRepository.findById(appointment.getPatient().getId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        appointment.setDoctor(doctor);
        appointment.setPatient(patient);

        return appointmentRepository.save(appointment);
    }

    @Override
    public Appointment updateAppointment(Long id, Appointment updatedAppointment) {
        Appointment existing = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        existing.setDate(updatedAppointment.getDate());
        existing.setTime(updatedAppointment.getTime());
        existing.setNotes(updatedAppointment.getNotes());

        Doctor doctor = doctorRepository.findById(updatedAppointment.getDoctor().getId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        Patient patient = patientRepository.findById(updatedAppointment.getPatient().getId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        existing.setDoctor(doctor);
        existing.setPatient(patient);

        return appointmentRepository.save(existing);
    }

    @Override
    public void deleteAppointment(Long id) {
        Appointment existing = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        appointmentRepository.delete(existing);
    }
}
