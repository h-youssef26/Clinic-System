package com.clinic.system.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "medical_records")
public class MedicalRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @OneToMany(mappedBy = "medicalRecord", cascade = CascadeType.ALL)
    private List<MedicalRecordEntry> entries = new ArrayList<>();

    public MedicalRecord() {}
    public MedicalRecord(Patient patient) { this.patient = patient; }

    public void addEntry(MedicalRecordEntry entry) {
        entries.add(entry);
        entry.setMedicalRecord(this);
    }

    public void removeEntry(MedicalRecordEntry entry) {
        entries.remove(entry);
        entry.setMedicalRecord(null);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public List<MedicalRecordEntry> getEntries() { return entries; }
    public void setEntries(List<MedicalRecordEntry> entries) { this.entries = entries; }

}
