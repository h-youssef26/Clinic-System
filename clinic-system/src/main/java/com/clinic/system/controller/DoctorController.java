package com.clinic.system.controller;

import com.clinic.system.model.Doctor;
import com.clinic.system.service.DoctorService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.List;

@RestController
@RequestMapping("/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    // ADMIN + DOCTOR can view
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public List<Doctor> getAllDoctors() {
        return doctorService.getAllDoctors();
    }

    // ADMIN + DOCTOR can view
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public Doctor getDoctorById(@PathVariable Long id) {
        return doctorService.getDoctorById(id);
    }

    // ADMIN ONLY: fetch from mock API
    @PostMapping("/fetch")
    @PreAuthorize("hasRole('ADMIN')")
    public String fetchDoctors() {
        doctorService.fetchAndSaveDoctors();
        return "Doctors fetched and saved successfully!";
    }

    // ADMIN ONLY: create doctor manually
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createDoctor(@RequestBody Doctor doctor) {
        try {
            Doctor savedDoctor = doctorService.saveDoctor(doctor);
            return ResponseEntity.ok(savedDoctor);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    // Update doctor (ADMIN ONLY)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Doctor updateDoctor(@PathVariable Long id, @RequestBody Doctor doctor) {
        return doctorService.updateDoctor(id, doctor);
    }


    // ADMIN ONLY: delete doctor
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
    }
}
