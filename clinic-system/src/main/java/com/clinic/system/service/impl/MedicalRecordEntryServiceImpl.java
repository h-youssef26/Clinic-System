package com.clinic.system.service.impl;

import com.clinic.system.exception.DoctorNotFoundException;
import com.clinic.system.exception.PatientNotFoundException;
import com.clinic.system.model.Doctor;
import com.clinic.system.model.MedicalRecord;
import com.clinic.system.model.MedicalRecordEntry;
import com.clinic.system.model.Nurse;
import com.clinic.system.repository.DoctorRepository;
import com.clinic.system.repository.MedicalRecordEntryRepository;
import com.clinic.system.repository.MedicalRecordRepository;
import com.clinic.system.repository.NurseRepository;
import com.clinic.system.service.MedicalRecordEntryService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalRecordEntryServiceImpl implements MedicalRecordEntryService {

    private final MedicalRecordEntryRepository entryRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final DoctorRepository doctorRepository;
    private final NurseRepository nurseRepository;

    public MedicalRecordEntryServiceImpl(MedicalRecordEntryRepository entryRepository,
                                         MedicalRecordRepository medicalRecordRepository,
                                         DoctorRepository doctorRepository,
                                         NurseRepository nurseRepository) {
        this.entryRepository = entryRepository;
        this.medicalRecordRepository = medicalRecordRepository;
        this.doctorRepository = doctorRepository;
        this.nurseRepository = nurseRepository;
    }

    @Override
    public MedicalRecordEntry createEntry(Long recordId, Long doctorId, Long nurseId, MedicalRecordEntry entryData) {
        MedicalRecord record = medicalRecordRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Medical record not found with id: " + recordId));

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new DoctorNotFoundException(doctorId));

        Nurse nurse = null;
        if (nurseId != null) {
            nurse = nurseRepository.findById(nurseId)
                    .orElseThrow(() -> new RuntimeException("Nurse not found with id: " + nurseId));
        }

        entryData.setMedicalRecord(record);
        entryData.setDoctor(doctor);
        entryData.setNurse(nurse);

        return entryRepository.save(entryData);
    }

    @Override
    public MedicalRecordEntry getEntryById(Long id) {
        return entryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medical record entry not found with id: " + id));
    }

    @Override
    public List<MedicalRecordEntry> getEntriesByRecord(Long recordId) {
        return entryRepository.findByMedicalRecordId(recordId);
    }

    @Transactional
    @Override
    public MedicalRecordEntry updateEntry(Long id, MedicalRecordEntry entryData) {
        MedicalRecordEntry existing = getEntryById(id);

        existing.setTitle(entryData.getTitle());
        existing.setNotes(entryData.getNotes());

        return entryRepository.save(existing);
    }

    @Transactional
    @Override
    public void deleteEntry(Long id) {
        MedicalRecordEntry existing = getEntryById(id);
        entryRepository.delete(existing);
    }
}
