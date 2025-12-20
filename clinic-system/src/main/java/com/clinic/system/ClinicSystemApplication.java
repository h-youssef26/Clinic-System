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
        // Now this works because doctorService is an instance, not static
        doctorService.fetchAndSaveDoctors();
    }
}
