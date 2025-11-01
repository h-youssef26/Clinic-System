package com.clinic.system.service.impl;

import com.clinic.system.model.Doctor;
import com.clinic.system.repository.DoctorRepository;
import com.clinic.system.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Override
    public Doctor saveDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    @Override
    public List<Doctor> getAllDoctors() {
        return (List<Doctor>) doctorRepository.findAll();
    }

    @Override
    public Optional<Doctor> getDoctorById(Long id) {
        return doctorRepository.findById(id);
    }

    @Override
    public Doctor updateDoctor(Long id, Doctor doctor) {
        Doctor existingDoctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id));

        existingDoctor.setName(doctor.getName());
        existingDoctor.setEmail(doctor.getEmail());
        existingDoctor.setPassword(doctor.getPassword());
        existingDoctor.setPhoneNumber(doctor.getPhoneNumber());
        existingDoctor.setSpecialization(doctor.getSpecialization());
        existingDoctor.setGender(doctor.getGender());
        existingDoctor.setExperienceYears(doctor.getExperienceYears());
        existingDoctor.setAddress(doctor.getAddress());
        existingDoctor.setDateOfBirth(doctor.getDateOfBirth());

        return doctorRepository.save(existingDoctor);
    }

    @Override
    public void deleteDoctor(Long id) {
        doctorRepository.deleteById(id);
    }
}
