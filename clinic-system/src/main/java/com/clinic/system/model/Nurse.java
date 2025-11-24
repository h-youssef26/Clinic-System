package com.clinic.system.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import java.util.List;
import java.util.ArrayList;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

@Entity
@DiscriminatorValue("NURSE")

public class Nurse extends User {

    private String phoneNumber;
    private LocalDate dateOfBirth;
    private int experienceYears;

    @OneToMany(mappedBy = "nurse", cascade = CascadeType.ALL)
    private List<MedicalRecordEntry> medicalRecordEntries = new ArrayList<>();

    @OneToMany(mappedBy = "nurse", cascade = CascadeType.ALL)
    private List<Appointment> appointments = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    public Nurse() {}

    public Nurse(String username, String password, String userRole, LocalDateTime createdAt, String name,
                 String phoneNumber, LocalDate dateOfBirth, int experienceYears) {
        super(username, password, createdAt, name);
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.experienceYears = experienceYears;
    }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public int getExperienceYears() { return experienceYears; }
    public void setExperienceYears(int experienceYears) { this.experienceYears = experienceYears; }
    public List<MedicalRecordEntry> getMedicalRecordEntries() { return medicalRecordEntries; }
    public void setMedicalRecordEntries(List<MedicalRecordEntry> medicalRecordEntries) { this.medicalRecordEntries = medicalRecordEntries; }

    public List<Appointment> getAppointments() { return appointments; }
    public void setAppointments(List<Appointment> appointments) { this.appointments = appointments; }

    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }
}
