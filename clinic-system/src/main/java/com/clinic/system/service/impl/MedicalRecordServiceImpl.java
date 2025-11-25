package com.clinic.system.service.impl;

import com.clinic.system.model.MedicalRecord;
import com.clinic.system.model.Patient;
import com.clinic.system.repository.MedicalRecordRepository;
import com.clinic.system.repository.PatientRepository;
import com.clinic.system.service.MedicalRecordService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final PatientRepository patientRepository;

    public MedicalRecordServiceImpl(MedicalRecordRepository medicalRecordRepository,
                                    PatientRepository patientRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    public MedicalRecord createRecordForPatient(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + patientId));

        MedicalRecord record = new MedicalRecord(patient);
        return medicalRecordRepository.save(record);
    }

    @Override
    public MedicalRecord getRecordById(Long id) {
        return medicalRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medical record not found with id: " + id));
    }

    @Override
    public List<MedicalRecord> getRecordsForPatient(Long patientId) {
        return medicalRecordRepository.findByPatientId(patientId);
    }

    @Transactional
    @Override
    public void deleteRecord(Long id) {
        MedicalRecord existing = getRecordById(id);
        medicalRecordRepository.delete(existing);
    }
}
