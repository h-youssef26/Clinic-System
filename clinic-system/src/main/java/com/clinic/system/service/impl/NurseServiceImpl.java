package com.clinic.system.service.impl;

import com.clinic.system.model.Nurse;
import com.clinic.system.repository.NurseRepository;
import com.clinic.system.service.NurseService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NurseServiceImpl implements NurseService {

    private final NurseRepository nurseRepository;

    public NurseServiceImpl(NurseRepository nurseRepository) {
        this.nurseRepository = nurseRepository;
    }

    @Override
    public Nurse createNurse(Nurse nurse) {
        return nurseRepository.save(nurse);
    }

    @Override
    public List<Nurse> getAllNurses() {
        return nurseRepository.findAll();
    }

    @Override
    public Nurse getNurseById(Long id) {
        return nurseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nurse not found with id: " + id));
    }

    @Transactional
    @Override
    public Nurse updateNurse(Long id, Nurse nurse) {
        Nurse existing = getNurseById(id);

        existing.setName(nurse.getName());
        existing.setPhoneNumber(nurse.getPhoneNumber());
        existing.setDateOfBirth(nurse.getDateOfBirth());
        existing.setExperienceYears(nurse.getExperienceYears());

        return nurseRepository.save(existing);
    }

    @Transactional
    @Override
    public void deleteNurse(Long id) {
        Nurse existing = getNurseById(id);
        nurseRepository.delete(existing);
    }
}
