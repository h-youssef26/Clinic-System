package com.clinic.system.controller;

import com.clinic.system.model.DoctorAvailability;
import com.clinic.system.service.DoctorAvailabilityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/availabilities")
public class DoctorAvailabilityController {

    private final DoctorAvailabilityService availabilityService;

    public DoctorAvailabilityController(DoctorAvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    @PostMapping
    public ResponseEntity<DoctorAvailability> createAvailability(
            @RequestParam Long doctorId,
            @RequestBody DoctorAvailability availability
    ) {
        return ResponseEntity.ok(availabilityService.createAvailability(doctorId, availability));
    }

    @GetMapping
    public ResponseEntity<List<DoctorAvailability>> getAllAvailabilities() {
        return ResponseEntity.ok(availabilityService.getAllAvailabilities());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorAvailability> getAvailabilityById(@PathVariable Long id) {
        return ResponseEntity.ok(availabilityService.getAvailabilityById(id));
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<DoctorAvailability>> getDoctorAvailabilities(@PathVariable Long doctorId) {
        return ResponseEntity.ok(availabilityService.getAvailabilitiesForDoctor(doctorId));
    }

    @GetMapping("/doctor/{doctorId}/date/{date}")
    public ResponseEntity<List<DoctorAvailability>> getDoctorAvailabilitiesOnDate(
            @PathVariable Long doctorId,
            @PathVariable String date
    ) {
        LocalDate d = LocalDate.parse(date);
        return ResponseEntity.ok(availabilityService.getAvailabilitiesForDoctorOnDate(doctorId, d));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorAvailability> updateAvailability(
            @PathVariable Long id,
            @RequestBody DoctorAvailability availability
    ) {
        return ResponseEntity.ok(availabilityService.updateAvailability(id, availability));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAvailability(@PathVariable Long id) {
        availabilityService.deleteAvailability(id);
        return ResponseEntity.noContent().build();
    }
}
