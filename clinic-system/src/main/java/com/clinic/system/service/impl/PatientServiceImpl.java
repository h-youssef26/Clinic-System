// package com.clinic.system.service.impl;

// import com.clinic.system.exception.PatientNotFoundException;
// import com.clinic.system.model.Patient;
// import com.clinic.system.repository.PatientRepository;
// import com.clinic.system.service.PatientService;
// import jakarta.transaction.Transactional;
// import org.springframework.stereotype.Service;

// import java.util.List;

// @Service
// public class PatientServiceImpl implements PatientService {

//     private final PatientRepository patientRepository;

//     public PatientServiceImpl(PatientRepository patientRepository) {
//         this.patientRepository = patientRepository;
//     }

//     @Override
//     public Patient savePatient(Patient patient) {
//         return patientRepository.save(patient);
//     }

//     @Override
//     public List<Patient> getAllPatients() {
//         return patientRepository.findAll();
//     }

//     @Override
//     public Patient getPatientById(Long id) {
//         return patientRepository.findById(id)
//                 .orElseThrow(() -> new PatientNotFoundException(id));
//     }

//     @Transactional
//     @Override
//     public Patient updatePatient(Long id, Patient patient) {
//         Patient existingPatient = patientRepository.findById(id)
//                 .orElseThrow(() -> new PatientNotFoundException(id));

//         existingPatient.setName(patient.getName());
//         existingPatient.setAddress(patient.getAddress());
//         existingPatient.setDateOfBirth(patient.getDateOfBirth());
//         existingPatient.setGender(patient.getGender());

//         return patientRepository.save(existingPatient);
//     }

//     @Transactional
//     @Override
//     public void deletePatient(Long id) {
//         Patient existingPatient = patientRepository.findById(id)
//                 .orElseThrow(() -> new PatientNotFoundException(id));
//         patientRepository.delete(existingPatient);
//     }
// }




package com.clinic.system.service.impl;

import com.clinic.system.exception.PatientNotFoundException;
import com.clinic.system.model.Patient;
import com.clinic.system.repository.PatientRepository;
import com.clinic.system.service.PatientService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public Patient savePatient(Patient patient) {
        // If username is empty, use email as username (if available)
        if (patient.getUsername() == null || patient.getUsername().isEmpty()) {
            patient.setUsername(patient.getEmail());
        }

        // No password required for patient in your current design
        return patientRepository.save(patient);
    }

    @Override
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    @Override
    public Patient getPatientById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException(id));
    }

    @Transactional
    @Override
    public Patient updatePatient(Long id, Patient patient) {
        Patient existingPatient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException(id));

        existingPatient.setName(patient.getName());
        existingPatient.setEmail(patient.getEmail());
        existingPatient.setPhoneNumber(patient.getPhoneNumber());
        existingPatient.setAddress(patient.getAddress());
        existingPatient.setDateOfBirth(patient.getDateOfBirth());
        existingPatient.setGender(patient.getGender());
        existingPatient.setMedicalHistory(patient.getMedicalHistory());

        // Optional: update username if needed
        if (patient.getUsername() != null && !patient.getUsername().isEmpty()) {
            existingPatient.setUsername(patient.getUsername());
        } else if (patient.getEmail() != null && !patient.getEmail().isEmpty()) {
            existingPatient.setUsername(patient.getEmail());
        }

        return patientRepository.save(existingPatient);
    }

    @Transactional
    @Override
    public void deletePatient(Long id) {
        Patient existingPatient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException(id));
        patientRepository.delete(existingPatient);
    }
}

