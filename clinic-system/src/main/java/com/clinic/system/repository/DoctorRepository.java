package com.clinic.system.repository;

import com.clinic.system.model.Doctor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepository extends CrudRepository<Doctor, Long> {
    // No extra methods for now. CrudRepository provides:
    // save, findById, findAll, deleteById, etc.
}
