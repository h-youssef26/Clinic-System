package com.clinic.system.controller;

import com.clinic.system.model.Administrator;
import com.clinic.system.service.AdministratorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admins")
public class AdministratorController {

    private final AdministratorService administratorService;

    public AdministratorController(AdministratorService administratorService) {
        this.administratorService = administratorService;
    }

    @PostMapping
    public ResponseEntity<Administrator> createAdministrator(@RequestBody Administrator admin) {
        return ResponseEntity.ok(administratorService.createAdministrator(admin));
    }

    @GetMapping
    public ResponseEntity<List<Administrator>> getAllAdministrators() {
        return ResponseEntity.ok(administratorService.getAllAdministrators());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Administrator> getAdministratorById(@PathVariable Long id) {
        return ResponseEntity.ok(administratorService.getAdministratorById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Administrator> updateAdministrator(@PathVariable Long id,
                                                             @RequestBody Administrator admin) {
        return ResponseEntity.ok(administratorService.updateAdministrator(id, admin));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdministrator(@PathVariable Long id) {
        administratorService.deleteAdministrator(id);
        return ResponseEntity.noContent().build();
    }
}
