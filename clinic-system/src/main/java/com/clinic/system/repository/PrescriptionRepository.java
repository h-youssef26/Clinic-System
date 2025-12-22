package com.clinic.system.repository;

import com.clinic.system.model.Prescription;
import com.clinic.system.model.User;
import com.clinic.system.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    List<Prescription> findByPatient(User patient);
    List<Prescription> findByDoctor(Doctor doctor);
    List<Prescription> findByPatientOrderByCreatedAtDesc(User patient);
    List<Prescription> findByDoctorOrderByCreatedAtDesc(Doctor doctor);
}

