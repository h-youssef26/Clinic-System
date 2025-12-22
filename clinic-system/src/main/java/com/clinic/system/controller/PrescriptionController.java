package com.clinic.system.controller;

import com.clinic.system.model.Prescription;
import com.clinic.system.model.User;
import com.clinic.system.model.Doctor;
import com.clinic.system.service.PrescriptionService;
import com.clinic.system.repository.UserRepository;
import com.clinic.system.repository.DoctorRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/prescriptions")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;

    public PrescriptionController(
            PrescriptionService prescriptionService,
            UserRepository userRepository,
            DoctorRepository doctorRepository) {
        this.prescriptionService = prescriptionService;
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
    }

    // Create prescription (DOCTOR only)
    @PostMapping
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<?> createPrescription(@RequestBody Prescription prescription) {
        try {
            // Get current authenticated user (doctor)
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            User currentUser = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Get doctor from current user
            Doctor doctor = doctorRepository.findByUser(currentUser)
                    .orElseThrow(() -> new RuntimeException("Doctor not found for current user"));

            // Set the doctor to the current doctor
            prescription.setDoctor(doctor);

            Prescription savedPrescription = prescriptionService.createPrescription(prescription);
            return ResponseEntity.ok(savedPrescription);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Get all prescriptions for current patient (PATIENT only)
    @GetMapping("/my-prescriptions")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<List<Prescription>> getMyPrescriptions() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            User currentUser = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            List<Prescription> prescriptions = prescriptionService.getPrescriptionsByPatient(currentUser);
            return ResponseEntity.ok(prescriptions);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Get all prescriptions for current doctor (DOCTOR only)
    @GetMapping("/doctor/my-prescriptions")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<List<Prescription>> getMyDoctorPrescriptions() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            User currentUser = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Doctor doctor = doctorRepository.findByUser(currentUser)
                    .orElseThrow(() -> new RuntimeException("Doctor not found for current user"));

            List<Prescription> prescriptions = prescriptionService.getPrescriptionsByDoctor(doctor);
            return ResponseEntity.ok(prescriptions);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Get prescription by ID (DOCTOR and PATIENT can view their own)
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'PATIENT')")
    public ResponseEntity<Prescription> getPrescriptionById(@PathVariable Long id) {
        try {
            Prescription prescription = prescriptionService.getPrescriptionById(id);
            
            // Check if current user has access to this prescription
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            User currentUser = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Check if user is the patient or the doctor
            boolean hasAccess = prescription.getPatient().getId().equals(currentUser.getId());
            
            if (!hasAccess && currentUser.getRole() == User.Role.DOCTOR) {
                Doctor doctor = doctorRepository.findByUser(currentUser).orElse(null);
                if (doctor != null) {
                    hasAccess = prescription.getDoctor().getId().equals(doctor.getId());
                }
            }

            if (!hasAccess) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            return ResponseEntity.ok(prescription);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Update prescription (DOCTOR only, and only their own)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<?> updatePrescription(@PathVariable Long id, @RequestBody Prescription updatedPrescription) {
        try {
            Prescription existingPrescription = prescriptionService.getPrescriptionById(id);
            
            // Verify the current doctor owns this prescription
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            User currentUser = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Doctor doctor = doctorRepository.findByUser(currentUser)
                    .orElseThrow(() -> new RuntimeException("Doctor not found for current user"));

            if (!existingPrescription.getDoctor().getId().equals(doctor.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only update your own prescriptions");
            }

            Prescription updated = prescriptionService.updatePrescription(id, updatedPrescription);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Delete prescription (DOCTOR can delete their own, PATIENT can delete prescriptions assigned to them)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'PATIENT')")
    public ResponseEntity<?> deletePrescription(@PathVariable Long id) {
        try {
            Prescription prescription = prescriptionService.getPrescriptionById(id);
            
            // Verify the current user has permission to delete this prescription
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            User currentUser = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            boolean hasPermission = false;

            // Check if user is the patient who owns this prescription
            if (currentUser.getRole() == User.Role.PATIENT) {
                hasPermission = prescription.getPatient().getId().equals(currentUser.getId());
            }
            // Check if user is the doctor who wrote this prescription
            else if (currentUser.getRole() == User.Role.DOCTOR) {
                Doctor doctor = doctorRepository.findByUser(currentUser)
                        .orElseThrow(() -> new RuntimeException("Doctor not found for current user"));
                hasPermission = prescription.getDoctor().getId().equals(doctor.getId());
            }

            if (!hasPermission) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only delete your own prescriptions");
            }

            prescriptionService.deletePrescription(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}

