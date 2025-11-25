package com.clinic.system.controller;

import com.clinic.system.model.Prescription;
import com.clinic.system.service.PrescriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @PostMapping
    public ResponseEntity<Prescription> createPrescription(@RequestBody Prescription prescription) {
        return ResponseEntity.ok(prescriptionService.createPrescription(prescription));
    }

    @GetMapping
    public ResponseEntity<List<Prescription>> getAllPrescriptions() {
        return ResponseEntity.ok(prescriptionService.getAllPrescriptions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Prescription> getPrescriptionById(@PathVariable Long id) {
        return ResponseEntity.ok(prescriptionService.getPrescriptionById(id));
    }

    @GetMapping("/by-patient/{patientId}")
    public ResponseEntity<List<Prescription>> getByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(prescriptionService.getPrescriptionsByPatient(patientId));
    }

    @GetMapping("/by-doctor/{doctorId}")
    public ResponseEntity<List<Prescription>> getByDoctor(@PathVariable Long doctorId) {
        return ResponseEntity.ok(prescriptionService.getPrescriptionsByDoctor(doctorId));
    }

    @GetMapping("/by-appointment/{appointmentId}")
    public ResponseEntity<List<Prescription>> getByAppointment(@PathVariable Long appointmentId) {
        return ResponseEntity.ok(prescriptionService.getPrescriptionsByAppointment(appointmentId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Prescription> updatePrescription(
            @PathVariable Long id,
            @RequestParam String medications
    ) {
        return ResponseEntity.ok(prescriptionService.updatePrescription(id, medications));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrescription(@PathVariable Long id) {
        prescriptionService.deletePrescription(id);
        return ResponseEntity.noContent().build();
    }
}
