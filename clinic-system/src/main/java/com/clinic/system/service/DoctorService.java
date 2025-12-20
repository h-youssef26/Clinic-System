package com.clinic.system.service;

import com.clinic.system.model.Doctor;
import com.clinic.system.model.User;
import com.clinic.system.repository.DoctorRepository;
import com.clinic.system.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;

    public DoctorService(DoctorRepository doctorRepository,
                         UserRepository userRepository,
                         BCryptPasswordEncoder passwordEncoder) {
        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.restTemplate = new RestTemplate();
    }

    @Transactional
    public void fetchAndSaveDoctors() {

        String apiUrl = "https://6945b703ed253f51719c2461.mockapi.io/api/v1/admin/import-doctors/doctors";
        Doctor[] doctors = restTemplate.getForObject(apiUrl, Doctor[].class);

        if (doctors == null) return;

        for (Doctor d : doctors) {

            if (userRepository.existsByEmail(d.getEmail())) {
                continue; // skip duplicates
            }

            String rawPassword = d.getPassword() != null
                    ? d.getPassword()
                    : UUID.randomUUID().toString();

            String encoded = passwordEncoder.encode(rawPassword);

            // Create USER
            User user = new User();
            user.setUsername(d.getName());
            user.setEmail(d.getEmail());
            user.setPassword(encoded);
            user.setEnabled(true);
            user.setRole(User.Role.DOCTOR);

            User savedUser = userRepository.save(user);

            // Create DOCTOR and LINK
            Doctor doctor = new Doctor();
            doctor.setName(d.getName());
            doctor.setEmail(d.getEmail());
            doctor.setSpecialty(d.getSpecialty());
            doctor.setPassword(encoded);
            doctor.setCreatedAt(LocalDateTime.now());
            doctor.setUser(savedUser);

            doctorRepository.save(doctor);
        }
    }


    // Save a new doctor and create corresponding User
    public Doctor saveDoctor(Doctor doctor) {

        if (userRepository.existsByEmail(doctor.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        // Create User first
        User user = new User();
        user.setUsername(doctor.getName());
        user.setEmail(doctor.getEmail());
        user.setPassword(passwordEncoder.encode(doctor.getPassword()));
        user.setEnabled(true);
        user.setRole(User.Role.DOCTOR);

        User savedUser = userRepository.save(user);

        // Link doctor to user
        doctor.setUser(savedUser);
        doctor.setPassword(savedUser.getPassword());
        doctor.setCreatedAt(LocalDateTime.now());

        return doctorRepository.save(doctor);
    }

    @Transactional
    public void deleteDoctor(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        // Delete linked User first
        User user = doctor.getUser();
        if (user != null) {
            userRepository.delete(user);
        }

        // Delete doctor
        doctorRepository.delete(doctor);
    }


    @Transactional
    public Doctor updateDoctor(Long id, Doctor updatedDoctor) {

        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        User user = doctor.getUser();

        doctor.setName(updatedDoctor.getName());
        doctor.setSpecialty(updatedDoctor.getSpecialty());

        user.setUsername(updatedDoctor.getName());
        user.setEmail(updatedDoctor.getEmail());

        if (updatedDoctor.getPassword() != null && !updatedDoctor.getPassword().isBlank()) {
            String encoded = passwordEncoder.encode(updatedDoctor.getPassword());
            user.setPassword(encoded);
            doctor.setPassword(encoded);
        }

        return doctorRepository.save(doctor);
    }

    public java.util.List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
    }


}
