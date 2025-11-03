package com.clinic.system.service.impl;

import com.clinic.system.exception.AppointmentNotFoundException;
import com.clinic.system.exception.DoctorNotFoundException;
import com.clinic.system.exception.PatientNotFoundException;
import com.clinic.system.model.Appointment;
import com.clinic.system.model.Doctor;
import com.clinic.system.model.Patient;
import com.clinic.system.repository.AppointmentRepository;
import com.clinic.system.repository.DoctorRepository;
import com.clinic.system.repository.PatientRepository;
import com.clinic.system.service.AppointmentService;
import jakarta.transaction.Transactional;
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
        return appointmentRepository.findAll();
    }

    @Override
    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException(id));
    }

    @Override
    public Appointment saveAppointment(Appointment appointment) {
        Doctor doctor = doctorRepository.findById(appointment.getDoctor().getId())
                .orElseThrow(() -> new DoctorNotFoundException(appointment.getDoctor().getId()));
        Patient patient = patientRepository.findById(appointment.getPatient().getId())
                .orElseThrow(() -> new PatientNotFoundException(appointment.getPatient().getId()));

        appointment.setDoctor(doctor);
        appointment.setPatient(patient);

        return appointmentRepository.save(appointment);
    }

    @Transactional
    @Override
    public Appointment updateAppointment(Long id, Appointment updatedAppointment) {
        Appointment existing = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException(id));

        existing.setDate(updatedAppointment.getDate());
        existing.setTime(updatedAppointment.getTime());
        existing.setNotes(updatedAppointment.getNotes());

        if (updatedAppointment.getDoctor() != null) {
            Doctor doctor = doctorRepository.findById(updatedAppointment.getDoctor().getId())
                    .orElseThrow(() -> new DoctorNotFoundException(updatedAppointment.getDoctor().getId()));
            existing.setDoctor(doctor);
        }

        if (updatedAppointment.getPatient() != null) {
            Patient patient = patientRepository.findById(updatedAppointment.getPatient().getId())
                    .orElseThrow(() -> new PatientNotFoundException(updatedAppointment.getPatient().getId()));
            existing.setPatient(patient);
        }

        return appointmentRepository.save(existing);
    }

    @Transactional
    @Override
    public void deleteAppointment(Long id) {
        Appointment existing = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException(id));
        appointmentRepository.delete(existing);
    }
}
