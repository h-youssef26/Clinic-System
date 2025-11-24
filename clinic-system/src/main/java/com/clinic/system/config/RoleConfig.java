package com.clinic.system.config;

import com.clinic.system.model.Role;
import com.clinic.system.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoleConfig {

    @Bean
    CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {

            createRoleIfNotFound("ROLE_ADMIN", roleRepository);
            createRoleIfNotFound("ROLE_DOCTOR", roleRepository);
            createRoleIfNotFound("ROLE_NURSE", roleRepository);
            createRoleIfNotFound("ROLE_RECEPTIONIST", roleRepository);
            createRoleIfNotFound("ROLE_PATIENT", roleRepository);

        };
    }

    private void createRoleIfNotFound(String name, RoleRepository repo) {
        if (repo.findByName(name).isEmpty()) {
            repo.save(new Role(name));
            System.out.println("Created role: " + name);
        }
    }
}
