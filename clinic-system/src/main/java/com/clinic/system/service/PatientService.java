package com.clinic.system.service;

import com.clinic.system.model.Patient;
import com.clinic.system.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {
    private final PatientRepository repository;

    public PatientService(PatientRepository repository) {
        this.repository = repository;
    }

    public List<Patient> getAllPatients() {
        return repository.findAll();
    }

    public Patient addPatient(Patient patient) {
        return repository.save(patient);
    }
    public void deletePatient(Long id) {
        repository.deleteById(id);
    }
}
