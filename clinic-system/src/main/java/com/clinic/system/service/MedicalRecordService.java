package com.clinic.system.service;

import com.clinic.system.model.MedicalRecord;

import java.util.List;

public interface MedicalRecordService {

    MedicalRecord createRecordForPatient(Long patientId);

    MedicalRecord getRecordById(Long id);

    List<MedicalRecord> getRecordsForPatient(Long patientId);

    void deleteRecord(Long id);
}
