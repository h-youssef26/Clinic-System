package com.clinic.system.repository;

import com.clinic.system.model.Payment;
import com.clinic.system.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByAppointment(Appointment appointment);
    Optional<Payment> findByAppointmentId(Long appointmentId);
    List<Payment> findByStatus(String status);
    List<Payment> findByPaymentMethod(String paymentMethod);
}


