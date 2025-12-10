package com.clinic.system.service;

import com.clinic.system.model.Doctor;
import com.clinic.system.model.Patient;

import java.util.List;
import java.util.Optional;

public interface DoctorService {
    Doctor saveDoctor(Doctor doctor);
    List<Doctor> getAllDoctors();
    Doctor getDoctorById(Long id);
    Doctor updateDoctor(Long id, Doctor doctor);
    boolean deleteDoctor(Long id);  // just the signature, no body

}
