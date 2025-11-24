// package com.clinic.system.service.impl;

// import com.clinic.system.exception.DoctorNotFoundException;
// import com.clinic.system.model.Doctor;
// import com.clinic.system.repository.DoctorRepository;
// import com.clinic.system.service.DoctorService;
// import jakarta.transaction.Transactional;
// import org.springframework.stereotype.Service;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import java.util.List;

// @Service
// public class DoctorServiceImpl implements DoctorService {

//     private final DoctorRepository doctorRepository;
//     private final PasswordEncoder passwordEncoder;

//     public DoctorServiceImpl(DoctorRepository doctorRepository, PasswordEncoder passwordEncoder) {
//         this.doctorRepository = doctorRepository;
//         this.passwordEncoder = passwordEncoder;
//     }

//     @Override
//     public Doctor saveDoctor(Doctor doctor) {
//         doctor.setPassword(passwordEncoder.encode(doctor.getPassword()));
//         return doctorRepository.save(doctor);
//     }

//     @Override
//     public List<Doctor> getAllDoctors() {
//         return doctorRepository.findAll();
//     }

//     @Override
//     public Doctor getDoctorById(Long id) {
//         return doctorRepository.findById(id)
//                 .orElseThrow(() -> new DoctorNotFoundException(id));
//     }

//     @Transactional
//     @Override
//     public Doctor updateDoctor(Long id, Doctor doctor) {
//         Doctor existingDoctor = doctorRepository.findById(id)
//                 .orElseThrow(() -> new DoctorNotFoundException(id));

//         existingDoctor.setName(doctor.getName());
//         existingDoctor.setPassword(doctor.getPassword());
//         existingDoctor.setPhoneNumber(doctor.getPhoneNumber());
//         existingDoctor.setSpecialization(doctor.getSpecialization());
//         existingDoctor.setGender(doctor.getGender());
//         existingDoctor.setExperienceYears(doctor.getExperienceYears());
//         existingDoctor.setAddress(doctor.getAddress());
//         existingDoctor.setDateOfBirth(doctor.getDateOfBirth());

//         if (doctor.getPassword() != null && !doctor.getPassword().isEmpty()) {
//             existingDoctor.setPassword(passwordEncoder.encode(doctor.getPassword()));
//         }

//         return doctorRepository.save(existingDoctor);
//     }

//     @Transactional
//     @Override
//     public void deleteDoctor(Long id) {
//         Doctor existingDoctor = doctorRepository.findById(id)
//                 .orElseThrow(() -> new DoctorNotFoundException(id));
//         doctorRepository.delete(existingDoctor);
//     }
// }



package com.clinic.system.service.impl;

import com.clinic.system.exception.DoctorNotFoundException;
import com.clinic.system.model.Doctor;
import com.clinic.system.repository.DoctorRepository;
import com.clinic.system.service.DoctorService;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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
        // If username is empty, use email as username
        if (doctor.getUsername() == null || doctor.getUsername().isEmpty()) {
            doctor.setUsername(doctor.getEmail());
        }

        // Encode password if provided
        if (doctor.getPassword() != null && !doctor.getPassword().isEmpty()) {
            doctor.setPassword(passwordEncoder.encode(doctor.getPassword()));
        }

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

        // Update basic info
        existingDoctor.setName(doctor.getName());
        existingDoctor.setEmail(doctor.getEmail());
        existingDoctor.setPhoneNumber(doctor.getPhoneNumber());
        existingDoctor.setSpecialization(doctor.getSpecialization());
        existingDoctor.setGender(doctor.getGender());
        existingDoctor.setExperienceYears(doctor.getExperienceYears());
        existingDoctor.setAddress(doctor.getAddress());
        existingDoctor.setDateOfBirth(doctor.getDateOfBirth());

        // Update username if you want email change to affect username
        if (doctor.getUsername() != null && !doctor.getUsername().isEmpty()) {
            existingDoctor.setUsername(doctor.getUsername());
        } else if (doctor.getEmail() != null && !doctor.getEmail().isEmpty()) {
            existingDoctor.setUsername(doctor.getEmail());
        }

        // Only update password if a new one is provided
        if (doctor.getPassword() != null && !doctor.getPassword().isEmpty()) {
            existingDoctor.setPassword(passwordEncoder.encode(doctor.getPassword()));
        }

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
