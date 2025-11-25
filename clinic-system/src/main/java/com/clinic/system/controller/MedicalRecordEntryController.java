package com.clinic.system.controller;

import com.clinic.system.model.MedicalRecordEntry;
import com.clinic.system.service.MedicalRecordEntryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class MedicalRecordEntryController {

    private final MedicalRecordEntryService entryService;

    public MedicalRecordEntryController(MedicalRecordEntryService entryService) {
        this.entryService = entryService;
    }

    // Create entry for a record
    @PostMapping("/api/records/{recordId}/entries")
    public ResponseEntity<MedicalRecordEntry> createEntry(
            @PathVariable Long recordId,
            @RequestParam Long doctorId,
            @RequestParam(required = false) Long nurseId,
            @RequestBody MedicalRecordEntry entryData
    ) {
        return ResponseEntity.ok(entryService.createEntry(recordId, doctorId, nurseId, entryData));
    }

    // Get all entries for a specific record
    @GetMapping("/api/records/{recordId}/entries")
    public ResponseEntity<List<MedicalRecordEntry>> getEntriesByRecord(@PathVariable Long recordId) {
        return ResponseEntity.ok(entryService.getEntriesByRecord(recordId));
    }

    // Get single entry
    @GetMapping("/api/entries/{id}")
    public ResponseEntity<MedicalRecordEntry> getEntryById(@PathVariable Long id) {
        return ResponseEntity.ok(entryService.getEntryById(id));
    }

    // Update single entry
    @PutMapping("/api/entries/{id}")
    public ResponseEntity<MedicalRecordEntry> updateEntry(
            @PathVariable Long id,
            @RequestBody MedicalRecordEntry entryData
    ) {
        return ResponseEntity.ok(entryService.updateEntry(id, entryData));
    }

    // Delete entry
    @DeleteMapping("/api/entries/{id}")
    public ResponseEntity<Void> deleteEntry(@PathVariable Long id) {
        entryService.deleteEntry(id);
        return ResponseEntity.noContent().build();
    }
}
