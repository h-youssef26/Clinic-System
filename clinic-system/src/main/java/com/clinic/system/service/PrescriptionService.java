package com.clinic.system.service;

import com.clinic.system.model.Prescription;

import java.util.List;

public interface PrescriptionService {

    Prescription createPrescription(Prescription prescription);

    Prescription getPrescriptionById(Long id);

    List<Prescription> getAllPrescriptions();

    List<Prescription> getPrescriptionsByPatient(Long patientId);

    List<Prescription> getPrescriptionsByDoctor(Long doctorId);

    List<Prescription> getPrescriptionsByAppointment(Long appointmentId);

    Prescription updatePrescription(Long id, String medications);

    void deletePrescription(Long id);
}
