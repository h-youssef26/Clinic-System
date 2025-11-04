package com.clinic.system.repository;

import com.clinic.system.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatientId(Long patientId);
    List<Appointment> findByDoctorId(Long doctorId);
    List<Appointment> findByDoctorIdAndDate(Long doctorId, LocalDate date);
    boolean existsByDoctorIdAndDateAndTimeAndStatusNot(Long doctorId, LocalDate date, LocalTime time, String status);

}

