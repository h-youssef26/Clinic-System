package com.clinic.system;

import com.clinic.system.service.DoctorService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClinicSystemApplication implements CommandLineRunner {

    private final DoctorService doctorService;

    // Spring will inject the DoctorService bean here
    public ClinicSystemApplication(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    public static void main(String[] args) {
        SpringApplication.run(ClinicSystemApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Fetch and save doctors from API on startup
        // This will skip duplicates if doctors already exist
        try {
            doctorService.fetchAndSaveDoctors();
        } catch (Exception e) {
            // Log error but don't fail startup if API is unavailable
            System.err.println("Warning: Could not fetch doctors on startup: " + e.getMessage());
        }
    }
}
