package com.clinic.system.service.impl;

import com.clinic.system.model.Appointment;
import com.clinic.system.model.Doctor;
import com.clinic.system.model.Patient;
import com.clinic.system.model.Prescription;
import com.clinic.system.repository.AppointmentRepository;
import com.clinic.system.repository.DoctorRepository;
import com.clinic.system.repository.PatientRepository;
import com.clinic.system.repository.PrescriptionRepository;
import com.clinic.system.service.PrescriptionService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;

    public PrescriptionServiceImpl(PrescriptionRepository prescriptionRepository,
                                   DoctorRepository doctorRepository,
                                   PatientRepository patientRepository,
                                   AppointmentRepository appointmentRepository) {
        this.prescriptionRepository = prescriptionRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public Prescription createPrescription(Prescription prescription) {
        // Ensure doctor / patient / appointment exist & attach managed entities
        if (prescription.getDoctor() != null && prescription.getDoctor().getId() != null) {
            Doctor doctor = doctorRepository.findById(prescription.getDoctor().getId())
                    .orElseThrow(() -> new RuntimeException("Doctor not found"));
            prescription.setDoctor(doctor);
        }

        if (prescription.getPatient() != null && prescription.getPatient().getId() != null) {
            Patient patient = patientRepository.findById(prescription.getPatient().getId())
                    .orElseThrow(() -> new RuntimeException("Patient not found"));
            prescription.setPatient(patient);
        }

        if (prescription.getAppointment() != null && prescription.getAppointment().getId() != null) {
            Appointment appointment = appointmentRepository.findById(prescription.getAppointment().getId())
                    .orElseThrow(() -> new RuntimeException("Appointment not found"));
            prescription.setAppointment(appointment);
        }

        return prescriptionRepository.save(prescription);
    }

    @Override
    public Prescription getPrescriptionById(Long id) {
        return prescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found with id: " + id));
    }

    @Override
    public List<Prescription> getAllPrescriptions() {
        return prescriptionRepository.findAll();
    }

    @Override
    public List<Prescription> getPrescriptionsByPatient(Long patientId) {
        return prescriptionRepository.findByPatientId(patientId);
    }

    @Override
    public List<Prescription> getPrescriptionsByDoctor(Long doctorId) {
        return prescriptionRepository.findByDoctorId(doctorId);
    }

    @Override
    public List<Prescription> getPrescriptionsByAppointment(Long appointmentId) {
        return prescriptionRepository.findByAppointmentId(appointmentId);
    }

    @Transactional
    @Override
    public Prescription updatePrescription(Long id, String medications) {
        Prescription existing = getPrescriptionById(id);
        existing.setMedications(medications);
        return prescriptionRepository.save(existing);
    }

    @Transactional
    @Override
    public void deletePrescription(Long id) {
        Prescription existing = getPrescriptionById(id);
        prescriptionRepository.delete(existing);
    }
}
