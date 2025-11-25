package com.clinic.system.service;

import com.clinic.system.model.DoctorAvailability;

import java.time.LocalDate;
import java.util.List;

public interface DoctorAvailabilityService {
    DoctorAvailability createAvailability(Long doctorId, DoctorAvailability availability);
    DoctorAvailability updateAvailability(Long id, DoctorAvailability availability);
    void deleteAvailability(Long id);
    DoctorAvailability getAvailabilityById(Long id);
    List<DoctorAvailability> getAllAvailabilities();
    List<DoctorAvailability> getAvailabilitiesForDoctor(Long doctorId);
    List<DoctorAvailability> getAvailabilitiesForDoctorOnDate(Long doctorId, LocalDate date);
}
