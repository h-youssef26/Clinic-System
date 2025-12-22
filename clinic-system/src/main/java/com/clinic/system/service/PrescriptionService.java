package com.clinic.system.service;

import com.clinic.system.model.Prescription;
import com.clinic.system.model.User;
import com.clinic.system.model.Doctor;
import com.clinic.system.repository.PrescriptionRepository;
import com.clinic.system.repository.UserRepository;
import com.clinic.system.repository.DoctorRepository;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.util.List;

@Service
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;

    public PrescriptionService(
            PrescriptionRepository prescriptionRepository,
            UserRepository userRepository,
            DoctorRepository doctorRepository) {
        this.prescriptionRepository = prescriptionRepository;
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
    }

    @Transactional
    public Prescription createPrescription(Prescription prescription) {
        // Validate doctor exists
        if (prescription.getDoctor() == null || prescription.getDoctor().getId() == null) {
            throw new RuntimeException("Doctor is required");
        }

        // Validate patient exists
        if (prescription.getPatient() == null || prescription.getPatient().getId() == null) {
            throw new RuntimeException("Patient is required");
        }

        // Load doctor and patient from database to ensure they exist
        Doctor doctor = doctorRepository.findById(prescription.getDoctor().getId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        
        User patient = userRepository.findById(prescription.getPatient().getId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        prescription.setDoctor(doctor);
        prescription.setPatient(patient);

        return prescriptionRepository.save(prescription);
    }

    @Transactional
    public Prescription updatePrescription(Long id, Prescription updatedPrescription) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));

        prescription.setMedications(updatedPrescription.getMedications());
        prescription.setInstructions(updatedPrescription.getInstructions());
        prescription.setDiagnosis(updatedPrescription.getDiagnosis());
        prescription.setNotes(updatedPrescription.getNotes());

        return prescriptionRepository.save(prescription);
    }

    public void deletePrescription(Long id) {
        prescriptionRepository.deleteById(id);
    }

    public List<Prescription> getAllPrescriptions() {
        return prescriptionRepository.findAll();
    }

    public List<Prescription> getPrescriptionsByPatient(User patient) {
        return prescriptionRepository.findByPatientOrderByCreatedAtDesc(patient);
    }

    public List<Prescription> getPrescriptionsByDoctor(Doctor doctor) {
        return prescriptionRepository.findByDoctorOrderByCreatedAtDesc(doctor);
    }

    public Prescription getPrescriptionById(Long id) {
        return prescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));
    }
}

