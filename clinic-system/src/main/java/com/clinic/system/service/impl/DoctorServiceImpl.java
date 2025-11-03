package com.clinic.system.service.impl;

import com.clinic.system.exception.DoctorNotFoundException;
import com.clinic.system.model.Doctor;
import com.clinic.system.repository.DoctorRepository;
import com.clinic.system.service.DoctorService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorServiceImpl(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @Override
    public Doctor saveDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    @Override
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    @Override
    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException(id));
    }

    @Transactional
    @Override
    public Doctor updateDoctor(Long id, Doctor doctor) {
        Doctor existingDoctor = doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException(id));

        existingDoctor.setName(doctor.getName());
        existingDoctor.setPassword(doctor.getPassword());
        existingDoctor.setPhoneNumber(doctor.getPhoneNumber());
        existingDoctor.setSpecialization(doctor.getSpecialization());
        existingDoctor.setGender(doctor.getGender());
        existingDoctor.setExperienceYears(doctor.getExperienceYears());
        existingDoctor.setAddress(doctor.getAddress());
        existingDoctor.setDateOfBirth(doctor.getDateOfBirth());

        return doctorRepository.save(existingDoctor);
    }

    @Transactional
    @Override
    public void deleteDoctor(Long id) {
        Doctor existingDoctor = doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException(id));
        doctorRepository.delete(existingDoctor);
    }
}
