package com.clinic.system.service.impl;

import com.clinic.system.model.Receptionist;
import com.clinic.system.model.Role;
import com.clinic.system.repository.ReceptionistRepository;
import com.clinic.system.repository.RoleRepository;
import com.clinic.system.service.ReceptionistService;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReceptionistServiceImpl implements ReceptionistService {

    private final ReceptionistRepository receptionistRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public ReceptionistServiceImpl(ReceptionistRepository receptionistRepository,
                                   RoleRepository roleRepository,
                                   PasswordEncoder passwordEncoder) {
        this.receptionistRepository = receptionistRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Receptionist createReceptionist(Receptionist receptionist) {
        if (receptionist.getPassword() != null) {
            receptionist.setPassword(passwordEncoder.encode(receptionist.getPassword()));
        }

        Role receptionistRole = roleRepository.findByName("ROLE_RECEPTIONIST").orElse(null);
        if (receptionistRole != null) {
            receptionist.getRoles().add(receptionistRole);
            receptionistRole.getUsers().add(receptionist);
        }

        return receptionistRepository.save(receptionist);
    }

    @Override
    public List<Receptionist> getAllReceptionists() {
        return receptionistRepository.findAll();
    }

    @Override
    public Receptionist getReceptionistById(Long id) {
        return receptionistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Receptionist not found with id: " + id));
    }

    @Transactional
    @Override
    public Receptionist updateReceptionist(Long id, Receptionist receptionist) {
        Receptionist existing = getReceptionistById(id);

        existing.setName(receptionist.getName());
        existing.setUsername(receptionist.getUsername());
        existing.setEmail(receptionist.getEmail());
        existing.setPhoneNumber(receptionist.getPhoneNumber());
        existing.setDeskNumber(receptionist.getDeskNumber());

        if (receptionist.getPassword() != null && !receptionist.getPassword().isEmpty()) {
            existing.setPassword(passwordEncoder.encode(receptionist.getPassword()));
        }

        return receptionistRepository.save(existing);
    }

    @Transactional
    @Override
    public void deleteReceptionist(Long id) {
        Receptionist existing = getReceptionistById(id);
        receptionistRepository.delete(existing);
    }
}
