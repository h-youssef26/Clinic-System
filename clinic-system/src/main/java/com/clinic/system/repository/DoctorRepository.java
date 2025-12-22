package com.clinic.system.repository;

import com.clinic.system.model.Doctor;
import com.clinic.system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.time.LocalDateTime;
@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    boolean existsByEmail(String email);
    Optional<Doctor> findByEmail(String email);
    Optional<Doctor> findByUser(User user);
}

