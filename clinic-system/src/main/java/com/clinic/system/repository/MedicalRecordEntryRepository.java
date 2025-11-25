package com.clinic.system.repository;

import com.clinic.system.model.MedicalRecordEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalRecordEntryRepository extends JpaRepository<MedicalRecordEntry, Long> {
    List<MedicalRecordEntry> findByMedicalRecordId(Long medicalRecordId);
}
