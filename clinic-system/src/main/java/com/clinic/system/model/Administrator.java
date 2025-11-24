package com.clinic.system.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.time.LocalDateTime;
import jakarta.persistence.OneToMany;
import java.util.List;
import java.util.ArrayList;

@Entity
@DiscriminatorValue("ADMIN")
public class Administrator extends User {

    private String phoneNumber;
    private String office;

    @OneToMany(mappedBy = "administrator")
    private List<Department> departments = new ArrayList<>();

    @OneToMany(mappedBy = "createdBy")
    private List<User> managedUsers = new ArrayList<>();

    public Administrator() {}

    public Administrator(String username, String password, String userRole, LocalDateTime createdAt, String name,
                         String phoneNumber, String office) {
        super(username, password, createdAt, name);
        this.phoneNumber = phoneNumber;
        this.office = office;
    }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getOffice() { return office; }
    public void setOffice(String office) { this.office = office; }

    public List<Department> getDepartments() { return departments; }
    public void setDepartments(List<Department> departments) { this.departments = departments; }

    public List<User> getManagedUsers() { return managedUsers; }
    public void setManagedUsers(List<User> managedUsers) { this.managedUsers = managedUsers; }
}
