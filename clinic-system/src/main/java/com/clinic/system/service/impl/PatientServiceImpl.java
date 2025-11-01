package com.clinic.system.service.impl;

import com.clinic.system.model.Patient;
import com.clinic.system.repository.PatientRepository;
import com.clinic.system.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Override
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    @Override
    public Patient getPatientById(Long id) {
        Optional<Patient> patient = patientRepository.findById(id);
        return patient.orElse(null);
    }

    @Override
    public Patient savePatient(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    public Patient updatePatient(Long id, Patient patientDetails) {
        Patient existingPatient = patientRepository.findById(id).orElse(null);
        if (existingPatient != null) {
            existingPatient.setName(patientDetails.getName());
            existingPatient.setPhoneNumber(patientDetails.getPhoneNumber());
            existingPatient.setGender(patientDetails.getGender());
            existingPatient.setDateOfBirth(patientDetails.getDateOfBirth());
            existingPatient.setAddress(patientDetails.getAddress());
            existingPatient.setEmail(patientDetails.getEmail());
            existingPatient.setMedicalHistory(patientDetails.getMedicalHistory());
            return patientRepository.save(existingPatient);
        } else {
            return null;
        }
    }

    @Override
    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }
}
