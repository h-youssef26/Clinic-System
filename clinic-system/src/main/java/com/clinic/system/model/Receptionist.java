package com.clinic.system.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.time.LocalDateTime;
import jakarta.persistence.OneToMany;
import java.util.List;
import java.util.ArrayList;

@Entity
@DiscriminatorValue("RECEPTIONIST")
public class Receptionist extends User {

    private String phoneNumber;
    private String deskNumber;

    @OneToMany(mappedBy = "receptionist")
    private List<Appointment> appointments = new ArrayList<>();

    @OneToMany(mappedBy = "issuedBy")
    private List<Invoice> invoices = new ArrayList<>();

    public Receptionist() {}

    public Receptionist(String username, String password, String userRole, LocalDateTime createdAt, String name,
                        String phoneNumber, String deskNumber) {
        super(username, password, createdAt, name);
        this.phoneNumber = phoneNumber;
        this.deskNumber = deskNumber;
    }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getDeskNumber() { return deskNumber; }
    public void setDeskNumber(String deskNumber) { this.deskNumber = deskNumber; }

    public List<Appointment> getAppointments() { return appointments; }
    public void setAppointments(List<Appointment> appointments) { this.appointments = appointments; }

    public List<Invoice> getInvoices() { return invoices; }
    public void setInvoices(List<Invoice> invoices) { this.invoices = invoices; }
}
