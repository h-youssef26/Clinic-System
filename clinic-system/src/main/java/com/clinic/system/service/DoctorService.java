package com.clinic.system.service;

import com.clinic.system.model.Doctor;
import com.clinic.system.model.User;
import com.clinic.system.repository.DoctorRepository;
import com.clinic.system.repository.UserRepository;
import com.clinic.system.repository.AppointmentRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;

    public DoctorService(DoctorRepository doctorRepository,
                         UserRepository userRepository,
                         AppointmentRepository appointmentRepository,
                         BCryptPasswordEncoder passwordEncoder) {
        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
        this.appointmentRepository = appointmentRepository;
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
        // Normalize email to lowercase
        String email = doctor.getEmail() != null ? doctor.getEmail().trim().toLowerCase() : "";
        if (email.isEmpty()) {
            throw new RuntimeException("Email is required");
        }

        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already registered");
        }

        // Validate password
        if (doctor.getPassword() == null || doctor.getPassword().trim().isEmpty()) {
            throw new RuntimeException("Password is required");
        }

        // Get the raw password and trim it
        String rawPassword = doctor.getPassword() != null ? doctor.getPassword().trim() : "";
        if (rawPassword.isEmpty()) {
            throw new RuntimeException("Password cannot be empty");
        }
        
        // Create User first
        User user = new User();
        user.setUsername(doctor.getName() != null ? doctor.getName().trim() : "");
        user.setEmail(email);
        // Encode the password - make sure we're using the raw password, not already encoded
        String encodedPassword = passwordEncoder.encode(rawPassword);
        user.setPassword(encodedPassword);
        user.setEnabled(true);
        user.setRole(User.Role.DOCTOR);

        User savedUser = userRepository.save(user);
        
        // Verify the password was encoded correctly by testing it
        boolean testMatch = passwordEncoder.matches(rawPassword, savedUser.getPassword());
        System.out.println("=== DOCTOR CREATION VERIFICATION ===");
        System.out.println("Raw password: " + rawPassword);
        System.out.println("Password matches after save: " + testMatch);
        if (!testMatch) {
            System.err.println("ERROR: Password encoding verification failed!");
        }
        
        // Debug: Log the created user
        System.out.println("=== DOCTOR CREATION ===");
        System.out.println("Doctor name: " + doctor.getName());
        System.out.println("Doctor email: " + email);
        System.out.println("User ID: " + savedUser.getId());
        System.out.println("User email: " + savedUser.getEmail());
        System.out.println("User role: " + savedUser.getRole());
        System.out.println("User enabled: " + savedUser.isEnabled());
        System.out.println("Password encoded (first 30): " + (savedUser.getPassword() != null && savedUser.getPassword().length() > 30 ? savedUser.getPassword().substring(0, 30) + "..." : savedUser.getPassword()));
        System.out.println("=== DOCTOR CREATED SUCCESSFULLY ===");

               // Link doctor to user
               doctor.setEmail(email);
               doctor.setUser(savedUser);
               doctor.setPassword(savedUser.getPassword()); // Store encoded password
               doctor.setCreatedAt(LocalDateTime.now());
               
               // Set default consultation fee if not provided
               if (doctor.getConsultationFee() == null || doctor.getConsultationFee() <= 0) {
                   doctor.setConsultationFee(100.0); // Default consultation fee
               }

               return doctorRepository.save(doctor);
    }

    @Transactional
    public void deleteDoctor(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        // Delete all appointments associated with this doctor first
        // This prevents foreign key constraint violations
        List<com.clinic.system.model.Appointment> appointments = appointmentRepository.findByDoctorId(id);
        appointments.forEach(appointment -> {
            appointmentRepository.delete(appointment);
        });

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

    public Doctor findByEmail(String email) {
        return doctorRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
    }


}
