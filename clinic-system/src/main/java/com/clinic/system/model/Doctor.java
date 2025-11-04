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

    // Constructors
    public Doctor() {}

    public Doctor(String username, String password, String role, LocalDateTime createdAt, String name,
                  String phoneNumber, String specialization, String gender,
                  int experienceYears, String address, LocalDate dateOfBirth) {
        super(username, password, role, name);
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

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public int getExperienceYears() { return experienceYears; }
    public void setExperienceYears(int experienceYears) { this.experienceYears = experienceYears; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
}
