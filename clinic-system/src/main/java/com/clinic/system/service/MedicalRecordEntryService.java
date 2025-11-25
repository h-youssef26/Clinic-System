package com.clinic.system.service;

import com.clinic.system.model.MedicalRecordEntry;

import java.util.List;

public interface MedicalRecordEntryService {

    MedicalRecordEntry createEntry(Long recordId, Long doctorId, Long nurseId, MedicalRecordEntry entryData);

    MedicalRecordEntry updateEntry(Long id, MedicalRecordEntry entryData);

    MedicalRecordEntry getEntryById(Long id);

    List<MedicalRecordEntry> getEntriesByRecord(Long recordId);

    void deleteEntry(Long id);
}
