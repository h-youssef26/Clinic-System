package com.clinic.system.service.impl;

import com.clinic.system.exception.DoctorNotFoundException;
import com.clinic.system.model.Doctor;
import com.clinic.system.model.DoctorAvailability;
import com.clinic.system.repository.DoctorAvailabilityRepository;
import com.clinic.system.repository.DoctorRepository;
import com.clinic.system.service.DoctorAvailabilityService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DoctorAvailabilityServiceImpl implements DoctorAvailabilityService {

    private final DoctorAvailabilityRepository availabilityRepository;
    private final DoctorRepository doctorRepository;

    public DoctorAvailabilityServiceImpl(DoctorAvailabilityRepository availabilityRepository,
                                         DoctorRepository doctorRepository) {
        this.availabilityRepository = availabilityRepository;
        this.doctorRepository = doctorRepository;
    }

    @Override
    public DoctorAvailability createAvailability(Long doctorId, DoctorAvailability availability) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new DoctorNotFoundException(doctorId));
        availability.setDoctor(doctor);
        return availabilityRepository.save(availability);
    }

    @Override
    public List<DoctorAvailability> getAllAvailabilities() {
        return availabilityRepository.findAll();
    }

    @Override
    public DoctorAvailability getAvailabilityById(Long id) {
        return availabilityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Availability not found with id: " + id));
    }

    @Override
    public List<DoctorAvailability> getAvailabilitiesForDoctor(Long doctorId) {
        return availabilityRepository.findByDoctorId(doctorId);
    }

    @Override
    public List<DoctorAvailability> getAvailabilitiesForDoctorOnDate(Long doctorId, LocalDate date) {
        return availabilityRepository.findByDoctorIdAndDate(doctorId, date);
    }

    @Transactional
    @Override
    public DoctorAvailability updateAvailability(Long id, DoctorAvailability availability) {
        DoctorAvailability existing = getAvailabilityById(id);
        existing.setDate(availability.getDate());
        existing.setStartTime(availability.getStartTime());
        existing.setEndTime(availability.getEndTime());
        existing.setAvailable(availability.isAvailable());
        return availabilityRepository.save(existing);
    }

    @Transactional
    @Override
    public void deleteAvailability(Long id) {
        DoctorAvailability existing = getAvailabilityById(id);
        availabilityRepository.delete(existing);
    }
}
