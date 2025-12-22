package com.clinic.system.controller;

import com.clinic.system.model.Doctor;
import com.clinic.system.service.DoctorService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.List;
import com.clinic.system.dto.DoctorNameDto;
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

    @GetMapping("/list")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<List<DoctorNameDto>> getDoctorsList() {
        try {
            List<Doctor> doctors = doctorService.getAllDoctors();
            
            // Ensure we always return a list, even if empty
            if (doctors == null) {
                doctors = new java.util.ArrayList<>();
            }

            // Map to DTO with ID, name, and consultation fee
            List<DoctorNameDto> doctorNames = doctors.stream()
                    .map(d -> {
                        DoctorNameDto dto = new DoctorNameDto();
                        dto.setName(d.getName() != null ? d.getName() : "Unknown");
                        dto.setId(d.getId());
                        dto.setConsultationFee(d.getConsultationFee() != null ? d.getConsultationFee() : 100.0);
                        return dto;
                    })
                    .toList();

            return ResponseEntity.ok(doctorNames);
        } catch (Exception e) {
            e.printStackTrace();
            // Return empty list on error instead of null
            return ResponseEntity.ok(new java.util.ArrayList<>());
        }
    }

}
