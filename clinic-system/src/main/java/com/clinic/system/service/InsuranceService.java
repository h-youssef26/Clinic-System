package com.clinic.system.service;

import com.clinic.system.model.Insurance;

import java.util.List;

public interface InsuranceService {

    Insurance createInsurance(Insurance insurance);

    Insurance updateInsurance(Long id, Insurance insurance);

    void deleteInsurance(Long id);

    Insurance getInsuranceById(Long id);

    List<Insurance> getAllInsurances();

    Insurance assignToPatient(Long insuranceId, Long patientId);

    List<Insurance> getInsurancesByPatient(Long patientId);
}
