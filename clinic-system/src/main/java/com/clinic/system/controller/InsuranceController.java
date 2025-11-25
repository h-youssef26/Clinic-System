package com.clinic.system.controller;

import com.clinic.system.model.Insurance;
import com.clinic.system.service.InsuranceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/insurances")
public class InsuranceController {

    private final InsuranceService insuranceService;

    public InsuranceController(InsuranceService insuranceService) {
        this.insuranceService = insuranceService;
    }

    @PostMapping
    public ResponseEntity<Insurance> createInsurance(@RequestBody Insurance insurance) {
        return ResponseEntity.ok(insuranceService.createInsurance(insurance));
    }

    @GetMapping
    public ResponseEntity<List<Insurance>> getAllInsurances() {
        return ResponseEntity.ok(insuranceService.getAllInsurances());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Insurance> getInsuranceById(@PathVariable Long id) {
        return ResponseEntity.ok(insuranceService.getInsuranceById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Insurance> updateInsurance(
            @PathVariable Long id,
            @RequestBody Insurance insurance
    ) {
        return ResponseEntity.ok(insuranceService.updateInsurance(id, insurance));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInsurance(@PathVariable Long id) {
        insuranceService.deleteInsurance(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{insuranceId}/assign-to-patient/{patientId}")
    public ResponseEntity<Insurance> assignToPatient(
            @PathVariable Long insuranceId,
            @PathVariable Long patientId
    ) {
        return ResponseEntity.ok(insuranceService.assignToPatient(insuranceId, patientId));
    }

    @GetMapping("/by-patient/{patientId}")
    public ResponseEntity<List<Insurance>> getInsurancesByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(insuranceService.getInsurancesByPatient(patientId));
    }
}
