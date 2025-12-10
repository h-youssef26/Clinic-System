package com.clinic.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class ClinicSystemApplication {

	public static void main(String[] args) {
        SpringApplication.run(ClinicSystemApplication.class, args);
	}

}
