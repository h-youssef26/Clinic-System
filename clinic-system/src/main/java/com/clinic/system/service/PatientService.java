package com.clinic.system.service;

import com.clinic.system.model.Patient;
import java.util.List;

public interface PatientService {

    List<Patient> getAllPatients();

    Patient getPatientById(Long id);

    Patient savePatient(Patient patient);

    Patient updatePatient(Long id, Patient patient);

    void deletePatient(Long id);
}
