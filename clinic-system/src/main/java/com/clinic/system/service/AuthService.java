package com.clinic.system.service;


import com.clinic.system.model.*;
import com.clinic.system.dto.LoginRequest;
import com.clinic.system.repository.*;
import com.clinic.system.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.clinic.system.security.JwtUtil;
import com.clinic.system.dto.JwtResponse;
import com.clinic.system.dto.RegisterPatientRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Transactional
    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        String jwt = jwtUtil.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .collect(Collectors.toList());

        return new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getName(), roles);
    }

    @Transactional
    public String registerPatient(RegisterPatientRequest req) {
        if (userRepository.existsByUsername(req.getUsername())) {
            return "Error: Username is already taken";
        }

        User user = new com.clinic.system.model.Patient(); // cast later to Patient
        user.setUsername(req.getUsername());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setName(req.getName());
        user.setCreatedAt(LocalDateTime.now());

        // assign ROLE_PATIENT
        Role patientRole = roleRepository.findByName("ROLE_PATIENT")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_PATIENT", "Patient role")));
        user.getRoles().add(patientRole);

        // save user (as Patient instance)
        Patient patient = new Patient();
        patient.setUsername(req.getUsername());
        patient.setPassword(passwordEncoder.encode(req.getPassword()));
        patient.setName(req.getName());
        patient.setCreatedAt(LocalDateTime.now());
        patient.setPhoneNumber(req.getPhoneNumber());
        patient.setGender(req.getGender());
        patient.setAddress(req.getAddress());
        if (req.getDateOfBirth() != null) {
            patient.setDateOfBirth(LocalDate.parse(req.getDateOfBirth()));
        }
        patient.getRoles().add(patientRole);

        patientRepository.save(patient);
        return "Patient registered successfully";
    }

    public String registerDoctorBlocked(String username) {
        if (userRepository.existsByUsername(username)) {
            return "Doctor account already exists. Please log in.";
        }
        return "Doctors cannot sign up. Please contact admin to create a doctor account.";
    }
}