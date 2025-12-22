package com.clinic.system.service;

import com.clinic.system.model.Payment;
import com.clinic.system.model.Appointment;
import com.clinic.system.repository.PaymentRepository;
import com.clinic.system.repository.AppointmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final AppointmentRepository appointmentRepository;

    public PaymentService(PaymentRepository paymentRepository, AppointmentRepository appointmentRepository) {
        this.paymentRepository = paymentRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @Transactional
    public Payment createPayment(Long appointmentId, String paymentMethod, String cardLast4) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        if (appointment.getAmount() == null || appointment.getAmount() <= 0) {
            throw new RuntimeException("Appointment amount must be greater than 0");
        }

        // Check if payment already exists
        Payment existingPayment = paymentRepository.findByAppointmentId(appointmentId).orElse(null);
        if (existingPayment != null) {
            throw new RuntimeException("Payment already exists for this appointment");
        }

        Payment payment = new Payment();
        payment.setAppointment(appointment);
        payment.setAmount(appointment.getAmount());
        payment.setPaymentMethod(paymentMethod);
        payment.setStatus("pending");
        payment.setCardLast4(cardLast4);

        Payment saved = paymentRepository.save(payment);

        // Update appointment payment status
        appointment.setPaymentStatus("pending");
        appointmentRepository.save(appointment);

        return saved;
    }

    @Transactional
    public Payment markPaymentAsPaid(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        payment.setStatus("paid");
        payment.setPaymentDate(LocalDateTime.now());

        Payment saved = paymentRepository.save(payment);

        // Update appointment payment status
        Appointment appointment = payment.getAppointment();
        appointment.setPaymentStatus("paid");
        appointmentRepository.save(appointment);

        return saved;
    }

    @Transactional
    public Payment denyPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        payment.setStatus("failed");
        payment.setPaymentDate(LocalDateTime.now());

        Payment saved = paymentRepository.save(payment);

        // Update appointment payment status
        Appointment appointment = payment.getAppointment();
        appointment.setPaymentStatus("failed");
        appointmentRepository.save(appointment);

        return saved;
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public List<Payment> getPaymentsByStatus(String status) {
        return paymentRepository.findByStatus(status);
    }

    public List<Payment> getPaymentsByMethod(String paymentMethod) {
        return paymentRepository.findByPaymentMethod(paymentMethod);
    }

    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
    }

    public Payment getPaymentByAppointmentId(Long appointmentId) {
        return paymentRepository.findByAppointmentId(appointmentId)
                .orElseThrow(() -> new RuntimeException("Payment not found for appointment"));
    }

    @Transactional
    public void deletePayment(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        // Update appointment payment status
        Appointment appointment = payment.getAppointment();
        appointment.setPaymentStatus("pending");
        appointmentRepository.save(appointment);

        paymentRepository.delete(payment);
    }
}


