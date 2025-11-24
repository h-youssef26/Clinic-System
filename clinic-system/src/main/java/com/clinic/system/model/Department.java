package com.clinic.system.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "departments")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
    private List<ClinicService> services = new ArrayList<>();

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
    private List<Doctor> doctors = new ArrayList<>();

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
    private List<Nurse> nurses = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "administrator_id") // foreign key column
    private Administrator administrator;

    public Department() {}
    public Department(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<Doctor> getDoctors() { return doctors; }
    public void setDoctors(List<Doctor> doctors) { this.doctors = doctors; }

    public List<ClinicService> getServices() { return services; }
    public void setServices(List<ClinicService> services) { this.services = services; }
    public List<Nurse> getNurses() { return nurses; }
    public void setNurses(List<Nurse> nurses) { this.nurses = nurses;}
    public Administrator getAdministrator() { return administrator; }
    public void setAdministrator(Administrator administrator) { this.administrator = administrator; }
}
