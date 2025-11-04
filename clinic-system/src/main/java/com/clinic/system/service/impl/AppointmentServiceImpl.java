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

import java.time.LocalDate;
import java.time.LocalTime;
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

    @Override
    public Appointment bookAppointment(Long patientId, Long doctorId, LocalDate date, LocalTime time, String notes) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException(patientId));

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new DoctorNotFoundException(doctorId));

        boolean doctorBusy = appointmentRepository
                .existsByDoctorIdAndDateAndTimeAndStatusNot(doctorId, date, time, "Cancelled");

        if (doctorBusy) {
            throw new RuntimeException("Doctor is already booked at this time.");
        }

        Appointment appointment = new Appointment(doctor, patient, date, time, notes, "Scheduled");
        return appointmentRepository.save(appointment);
    }

    @Override
    @Transactional
    public Appointment cancelAppointment(Long appointmentId, Long patientId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException(appointmentId));

        if (!appointment.getPatient().getId().equals(patientId)) {
            throw new RuntimeException("You are not allowed to cancel this appointment.");
        }

        appointment.setStatus("Cancelled");

        return appointmentRepository.save(appointment);
    }

    @Override
    public List<Appointment> getDoctorSchedule(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new DoctorNotFoundException(doctorId));
        return appointmentRepository.findByDoctorId(doctor.getId());
    }

    @Override
    public List<Appointment> getDoctorScheduleByDate(Long doctorId, LocalDate date) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new DoctorNotFoundException(doctorId));
        return appointmentRepository.findByDoctorIdAndDate(doctor.getId(), date);
    }

    @Override
    @Transactional
    public Appointment addPrescription(Long appointmentId, Long doctorId, String prescriptionText) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException(appointmentId));

        if (!appointment.getDoctor().getId().equals(doctorId)) {
            throw new RuntimeException("You are not authorized to add a prescription for this appointment.");
        }

        appointment.setPrescription(prescriptionText);
        return appointmentRepository.save(appointment);
    }

    @Override
    public List<Appointment> getPatientAppointments(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException(patientId));
        return appointmentRepository.findByPatientId(patient.getId());
    }

    @Override
    @Transactional
    public Appointment rescheduleAppointment(Long appointmentId, Long patientId, LocalDate newDate, LocalTime newTime) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException(appointmentId));

        if (!appointment.getPatient().getId().equals(patientId)) {
            throw new RuntimeException("You can only reschedule your own appointments.");
        }

        boolean doctorBusy = appointmentRepository
                .existsByDoctorIdAndDateAndTimeAndStatusNot(
                        appointment.getDoctor().getId(), newDate, newTime, "Cancelled");

        if (doctorBusy) {
            throw new RuntimeException("Doctor is already booked at the new time.");
        }

        appointment.setDate(newDate);
        appointment.setTime(newTime);
        appointment.setStatus("Rescheduled");

        return appointmentRepository.save(appointment);
    }

}
