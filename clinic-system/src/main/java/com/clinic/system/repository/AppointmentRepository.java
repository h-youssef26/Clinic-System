package com.clinic.system.repository;

import com.clinic.system.model.Appointment;
import com.clinic.system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatient(User patient);
    List<Appointment> findByDoctorId(Long doctorId);
    List<Appointment> findByStatus(Appointment.AppointmentStatus status);
    boolean existsByDoctorIdAndDateTime(Long doctorId, LocalDateTime dateTime);

}
