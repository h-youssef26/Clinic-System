package com.clinic.system.service.impl;

import com.clinic.system.model.Administrator;
import com.clinic.system.model.Role;
import com.clinic.system.repository.AdministratorRepository;
import com.clinic.system.repository.RoleRepository;
import com.clinic.system.service.AdministratorService;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdministratorServiceImpl implements AdministratorService {

    private final AdministratorRepository administratorRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AdministratorServiceImpl(AdministratorRepository administratorRepository,
                                    RoleRepository roleRepository,
                                    PasswordEncoder passwordEncoder) {
        this.administratorRepository = administratorRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Administrator createAdministrator(Administrator admin) {
        if (admin.getPassword() != null) {
            admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        }

        // Give ROLE_ADMIN automatically if it exists
        Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElse(null);
        if (adminRole != null) {
            admin.getRoles().add(adminRole);
            adminRole.getUsers().add(admin);
        }

        return administratorRepository.save(admin);
    }

    @Override
    public List<Administrator> getAllAdministrators() {
        return administratorRepository.findAll();
    }

    @Override
    public Administrator getAdministratorById(Long id) {
        return administratorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Administrator not found with id: " + id));
    }

    @Transactional
    @Override
    public Administrator updateAdministrator(Long id, Administrator admin) {
        Administrator existing = getAdministratorById(id);

        existing.setName(admin.getName());
        existing.setUsername(admin.getUsername());
        existing.setEmail(admin.getEmail());
        existing.setPhoneNumber(admin.getPhoneNumber());
        existing.setOffice(admin.getOffice());

        if (admin.getPassword() != null && !admin.getPassword().isEmpty()) {
            existing.setPassword(passwordEncoder.encode(admin.getPassword()));
        }

        return administratorRepository.save(existing);
    }

    @Transactional
    @Override
    public void deleteAdministrator(Long id) {
        Administrator existing = getAdministratorById(id);
        administratorRepository.delete(existing);
    }
}
