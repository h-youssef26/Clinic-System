package com.clinic.system.repository;

import com.clinic.system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // add custom queries if needed, e.g. findByUsername
}
