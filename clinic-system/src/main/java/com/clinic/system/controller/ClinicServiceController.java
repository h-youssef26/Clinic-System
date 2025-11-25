package com.clinic.system.controller;

import com.clinic.system.model.ClinicService;
import com.clinic.system.service.ClinicServiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
public class ClinicServiceController {

    private final ClinicServiceService clinicServiceService;

    public ClinicServiceController(ClinicServiceService clinicServiceService) {
        this.clinicServiceService = clinicServiceService;
    }

    // Create service and assign to department
    @PostMapping
    public ResponseEntity<ClinicService> createService(
            @RequestParam Long departmentId,
            @RequestBody ClinicService service
    ) {
        return ResponseEntity.ok(clinicServiceService.createService(service, departmentId));
    }

    @GetMapping
    public ResponseEntity<List<ClinicService>> getAllServices() {
        return ResponseEntity.ok(clinicServiceService.getAllServices());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClinicService> getServiceById(@PathVariable Long id) {
        return ResponseEntity.ok(clinicServiceService.getServiceById(id));
    }

    @GetMapping("/by-department/{departmentId}")
    public ResponseEntity<List<ClinicService>> getServicesByDepartment(@PathVariable Long departmentId) {
        return ResponseEntity.ok(clinicServiceService.getServicesByDepartment(departmentId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClinicService> updateService(
            @PathVariable Long id,
            @RequestParam(required = false) Long departmentId,
            @RequestBody ClinicService service
    ) {
        return ResponseEntity.ok(clinicServiceService.updateService(id, service, departmentId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        clinicServiceService.deleteService(id);
        return ResponseEntity.noContent().build();
    }
}
