package com.clinic.system.service.impl;

import com.clinic.system.exception.DoctorNotFoundException;
import com.clinic.system.model.Doctor;
import com.clinic.system.repository.DoctorRepository;
import com.clinic.system.service.DoctorService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;

    public DoctorServiceImpl(DoctorRepository doctorRepository, PasswordEncoder passwordEncoder) {
        this.doctorRepository = doctorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Doctor saveDoctor(Doctor doctor) {
        doctor.setPassword(passwordEncoder.encode(doctor.getPassword()));
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

        if (doctor.getPassword() != null && !doctor.getPassword().isEmpty()) {
            existingDoctor.setPassword(passwordEncoder.encode(doctor.getPassword()));
        }

        return doctorRepository.save(existingDoctor);
    }

    @Transactional
    @Override
    public boolean deleteDoctor(Long id) {
        Optional<Doctor> doctorOpt = doctorRepository.findById(id);
        if (doctorOpt.isPresent()) {
            doctorRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
