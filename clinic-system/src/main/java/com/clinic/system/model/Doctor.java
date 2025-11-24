package com.clinic.system.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;

@Entity
@DiscriminatorValue("DOCTOR")
public class Doctor extends User {

    private String phoneNumber;
    private String specialization;
    private String gender;
    private int experienceYears;
    private String address;
    private LocalDate dateOfBirth;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Appointment> appointments = new ArrayList<>();

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Prescription> prescriptions = new ArrayList<>();

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<MedicalRecordEntry> medicalRecordEntries = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "doctor_clinic_service",
            joinColumns = @JoinColumn(name = "doctor_id"),
            inverseJoinColumns = @JoinColumn(name = "clinic_service_id")
    )
    private List<ClinicService> clinicServices = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    // Constructors
    public Doctor() {
    }

    public Doctor(String username, String password, String user_role, LocalDateTime createdAt, String name,
                  String phoneNumber, String specialization, String gender,
                  int experienceYears, String address, LocalDate dateOfBirth) {
        super(username, password, createdAt, name);
        this.phoneNumber = phoneNumber;
        this.specialization = specialization;
        this.gender = gender;
        this.experienceYears = experienceYears;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(int experienceYears) {
        this.experienceYears = experienceYears;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public List<ClinicService> getClinicServices() {
        return clinicServices;
    }

    public void setClinicServices(List<ClinicService> clinicServices) {
        this.clinicServices = clinicServices;
    }

    public List<Prescription> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(List<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
    }

    public List<MedicalRecordEntry> getMedicalRecordEntries() {
        return medicalRecordEntries;
    }

    public void setMedicalRecordEntries(List<MedicalRecordEntry> medicalRecordEntries) {
        this.medicalRecordEntries = medicalRecordEntries;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
}
