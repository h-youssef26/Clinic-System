package com.clinic.system.controller;

import com.clinic.system.model.Receptionist;
import com.clinic.system.service.ReceptionistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/receptionists")
public class ReceptionistController {

    private final ReceptionistService receptionistService;

    public ReceptionistController(ReceptionistService receptionistService) {
        this.receptionistService = receptionistService;
    }

    @PostMapping
    public ResponseEntity<Receptionist> createReceptionist(@RequestBody Receptionist receptionist) {
        return ResponseEntity.ok(receptionistService.createReceptionist(receptionist));
    }

    @GetMapping
    public ResponseEntity<List<Receptionist>> getAllReceptionists() {
        return ResponseEntity.ok(receptionistService.getAllReceptionists());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Receptionist> getReceptionistById(@PathVariable Long id) {
        return ResponseEntity.ok(receptionistService.getReceptionistById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Receptionist> updateReceptionist(@PathVariable Long id,
                                                           @RequestBody Receptionist receptionist) {
        return ResponseEntity.ok(receptionistService.updateReceptionist(id, receptionist));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReceptionist(@PathVariable Long id) {
        receptionistService.deleteReceptionist(id);
        return ResponseEntity.noContent().build();
    }
}
