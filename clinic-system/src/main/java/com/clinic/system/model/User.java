// package com.clinic.system.model;

// import jakarta.persistence.*;
// import java.time.LocalDateTime;
// import java.util.HashSet;
// import java.util.Set;

// @Entity
// @Inheritance(strategy = InheritanceType.JOINED)
// @DiscriminatorColumn(name = "dtype")
// public abstract class User {

//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     private String username;
//     private String password;
//     private String name;
//     private LocalDateTime createdAt;
//     private LocalDateTime updatedAt;

//     @PrePersist
//     protected void onCreate() {
//         this.createdAt = LocalDateTime.now();
//         this.updatedAt = LocalDateTime.now();
//     }

//     @PreUpdate
//     protected void onUpdate() {
//         this.updatedAt = LocalDateTime.now();
//     }

//     @ManyToMany(fetch = FetchType.EAGER)
//     @JoinTable(
//             name = "user_roles",
//             joinColumns = @JoinColumn(name = "user_id"),
//             inverseJoinColumns = @JoinColumn(name = "role_id")
//     )
//     private Set<Role> roles = new HashSet<>();
//     @ManyToOne
//     @JoinColumn(name = "created_by_id")
//     private Administrator createdBy;


//     public User() {
//     }

//     public User(String username, String password, LocalDateTime createdAt, String name) {
//         this.username = username;
//         this.password = password;
//         this.createdAt = createdAt;
//         this.name = name;
//     }

//     // Getters and setters
//     public Long getId() {
//         return id;
//     }

//     public void setId(Long id) {
//         this.id = id;
//     }

//     public String getUsername() {
//         return username;
//     }

//     public void setUsername(String username) {
//         this.username = username;
//     }

//     public String getPassword() {
//         return password;
//     }

//     public void setPassword(String password) {
//         this.password = password;
//     }

//     public LocalDateTime getCreatedAt() {
//         return createdAt;
//     }

//     public void setCreatedAt(LocalDateTime createdAt) {
//         this.createdAt = createdAt;
//     }

//     public String getName() {
//         return name;
//     }

//     public void setName(String name) {
//         this.name = name;
//     }

//     public LocalDateTime getUpdatedAt() {
//         return updatedAt;
//     }

//     public void setUpdatedAt(LocalDateTime updatedAt) {
//         this.updatedAt = updatedAt;
//     }

//     public Set<Role> getRoles() {
//         return roles;
//     }

//     public void setRoles(Set<Role> roles) {
//         this.roles = roles;
//     }

//     public Administrator getCreatedBy() {
//         return createdBy;
//     }

//     public void setCreatedBy(Administrator createdBy) {
//         this.createdBy = createdBy;
//     }
// }


package com.clinic.system.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Can be null if you don’t want login for some users
    @Column(unique = true)
    private String username;

    // We hide password so it is never returned in JSON responses
    @JsonIgnore
    private String password;

    private String name;

    @Column(unique = true)
    private String email;

    private LocalDateTime createdAt;

    // Who created this user (an Administrator)
    @ManyToOne
    @JoinColumn(name = "created_by_id")
    @JsonIgnore
    private Administrator createdBy;

    @ManyToMany
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    public User() {
    }

    public User(String username, String password, LocalDateTime createdAt, String name) {
        this.username = username;
        this.password = password;
        this.createdAt = createdAt;
        this.name = name;
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    // ---------- Getters & Setters ----------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Administrator getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Administrator createdBy) {
        this.createdBy = createdBy;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
