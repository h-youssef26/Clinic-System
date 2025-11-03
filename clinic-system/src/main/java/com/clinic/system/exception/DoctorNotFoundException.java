package com.clinic.system.exception;

public class DoctorNotFoundException extends RuntimeException {
    public DoctorNotFoundException(Long id) {
        super("Doctor not found with id: " + id);
    }
}
