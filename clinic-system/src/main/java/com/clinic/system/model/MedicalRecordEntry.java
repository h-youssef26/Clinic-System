package com.clinic.system.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "medical_record_entries")
public class MedicalRecordEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime entryDate;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String notes;

    @ManyToOne
    @JoinColumn(name = "nurse_id") // foreign key column
    private Nurse nurse;

    @ManyToOne
    @JoinColumn(name = "medical_record_id")
    private MedicalRecord medicalRecord;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor; // who made the entry

    public MedicalRecordEntry() {}

    @PrePersist
    protected void onCreate() {
        this.entryDate = LocalDateTime.now();
    }

    public MedicalRecordEntry(String title, String notes, Doctor doctor) {
        this.title = title;
        this.notes = notes;
        this.doctor = doctor;
        this.entryDate = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getEntryDate() { return entryDate; }
    public void setEntryDate(LocalDateTime entryDate) { this.entryDate = entryDate; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public MedicalRecord getMedicalRecord() { return medicalRecord; }
    public void setMedicalRecord(MedicalRecord medicalRecord) { this.medicalRecord = medicalRecord; }

    public Doctor getDoctor() { return doctor; }
    public void setDoctor(Doctor doctor) { this.doctor = doctor; }

    public Nurse getNurse() { return nurse; }
    public void setNurse(Nurse nurse) { this.nurse = nurse; }
}
