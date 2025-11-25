package com.clinic.system.service.impl;

import com.clinic.system.model.Insurance;
import com.clinic.system.model.Patient;
import com.clinic.system.repository.InsuranceRepository;
import com.clinic.system.repository.PatientRepository;
import com.clinic.system.service.InsuranceService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InsuranceServiceImpl implements InsuranceService {

    private final InsuranceRepository insuranceRepository;
    private final PatientRepository patientRepository;

    public InsuranceServiceImpl(InsuranceRepository insuranceRepository,
                                PatientRepository patientRepository) {
        this.insuranceRepository = insuranceRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    public Insurance createInsurance(Insurance insurance) {
        return insuranceRepository.save(insurance);
    }

    @Override
    public List<Insurance> getAllInsurances() {
        return insuranceRepository.findAll();
    }

    @Override
    public Insurance getInsuranceById(Long id) {
        return insuranceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Insurance not found with id: " + id));
    }

    @Transactional
    @Override
    public Insurance updateInsurance(Long id, Insurance insurance) {
        Insurance existing = getInsuranceById(id);

        existing.setProviderName(insurance.getProviderName());
        existing.setPolicyNumber(insurance.getPolicyNumber());
        existing.setCoverageDetails(insurance.getCoverageDetails());

        return insuranceRepository.save(existing);
    }

    @Transactional
    @Override
    public void deleteInsurance(Long id) {
        Insurance existing = getInsuranceById(id);
        insuranceRepository.delete(existing);
    }

    @Transactional
    @Override
    public Insurance assignToPatient(Long insuranceId, Long patientId) {
        Insurance insurance = getInsuranceById(insuranceId);
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + patientId));

        if (!insurance.getPatients().contains(patient)) {
            insurance.getPatients().add(patient);
        }
        if (!patient.getInsurances().contains(insurance)) {
            patient.getInsurances().add(insurance);
        }

        // saving insurance is enough; cascade may handle the join table
        return insuranceRepository.save(insurance);
    }

    @Override
    public List<Insurance> getInsurancesByPatient(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + patientId));
        return patient.getInsurances();
    }
}
