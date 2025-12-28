package com.clinic.system.controller;

import com.clinic.system.model.Payment;
import com.clinic.system.model.User;
import com.clinic.system.model.Doctor;
import com.clinic.system.repository.UserRepository;
import com.clinic.system.service.PaymentService;
import com.clinic.system.service.DoctorService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final UserRepository userRepository;
    private final DoctorService doctorService;

    public PaymentController(PaymentService paymentService, UserRepository userRepository, DoctorService doctorService) {
        this.paymentService = paymentService;
        this.userRepository = userRepository;
        this.doctorService = doctorService;
    }

    // Get all payments (ADMIN only)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public ResponseEntity<List<Payment>> getAllPayments() {
        List<Payment> payments = paymentService.getAllPayments();
        // Force initialization of lazy-loaded relationships
        payments.forEach(payment -> {
            if (payment.getAppointment() != null) {
                if (payment.getAppointment().getPatient() != null) {
                    payment.getAppointment().getPatient().getActualUsername(); // Force load
                }
                if (payment.getAppointment().getDoctor() != null) {
                    payment.getAppointment().getDoctor().getName(); // Force load
                }
            }
        });
        return ResponseEntity.ok(payments);
    }

    // Get pending payments (ADMIN only) - for cash payments that need approval
    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Payment>> getPendingPayments() {
        return ResponseEntity.ok(paymentService.getPaymentsByStatus("pending"));
    }

    // Get payments by method (ADMIN only)
    @GetMapping("/method/{method}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Payment>> getPaymentsByMethod(@PathVariable String method) {
        return ResponseEntity.ok(paymentService.getPaymentsByMethod(method));
    }

    // Get patient's pending payments (PATIENT only)
    @GetMapping("/my-pending")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<List<Payment>> getMyPendingPayments() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            User currentUser = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Get all payments and filter by patient's appointments
            List<Payment> allPayments = paymentService.getAllPayments();
            List<Payment> patientPayments = allPayments.stream()
                    .filter(payment -> payment.getAppointment().getPatient().getId().equals(currentUser.getId()))
                    .toList();

            return ResponseEntity.ok(patientPayments);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).build();
        }
    }

    // Get all patient's payments (PATIENT only)
    @GetMapping("/my")
    @PreAuthorize("hasAuthority('ROLE_PATIENT')")
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public ResponseEntity<List<Payment>> getMyPayments() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            User currentUser = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            List<Payment> payments = paymentService.getPaymentsByPatientId(currentUser.getId());
            // Force initialization of lazy-loaded relationships
            payments.forEach(payment -> {
                if (payment.getAppointment() != null) {
                    if (payment.getAppointment().getPatient() != null) {
                        payment.getAppointment().getPatient().getActualUsername(); // Force load
                    }
                    if (payment.getAppointment().getDoctor() != null) {
                        payment.getAppointment().getDoctor().getName(); // Force load
                    }
                }
            });
            return ResponseEntity.ok(payments);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).build();
        }
    }

    // Get doctor's payments (DOCTOR only)
    @GetMapping("/doctor/my")
    @PreAuthorize("hasRole('DOCTOR')")
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public ResponseEntity<List<Payment>> getDoctorPayments() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            Doctor doctor = doctorService.findByEmail(email);
            
            if (doctor == null) {
                return ResponseEntity.status(404).build();
            }

            List<Payment> payments = paymentService.getPaymentsByDoctorId(doctor.getId());
            // Force initialization of lazy-loaded relationships
            payments.forEach(payment -> {
                if (payment.getAppointment() != null) {
                    if (payment.getAppointment().getPatient() != null) {
                        payment.getAppointment().getPatient().getActualUsername(); // Force load
                    }
                    if (payment.getAppointment().getDoctor() != null) {
                        payment.getAppointment().getDoctor().getName(); // Force load
                    }
                }
            });
            return ResponseEntity.ok(payments);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).build();
        }
    }

    // Create payment (PATIENT only) - for Visa payments
    @PostMapping
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<?> createPayment(@RequestBody PaymentRequest request) {
        try {
            Payment payment = paymentService.createPayment(
                    request.getAppointmentId(),
                    request.getPaymentMethod(),
                    request.getCardLast4()
            );
            return ResponseEntity.ok(payment);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    // Mark payment as paid (ADMIN only) - for cash payments approval
    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> approvePayment(@PathVariable Long id) {
        try {
            Payment payment = paymentService.markPaymentAsPaid(id);
            return ResponseEntity.ok(payment);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    // Deny payment (ADMIN only) - mark as failed
    @PutMapping("/{id}/deny")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> denyPayment(@PathVariable Long id) {
        try {
            Payment payment = paymentService.denyPayment(id);
            return ResponseEntity.ok(payment);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    // Get payment by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PATIENT', 'DOCTOR')")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Long id) {
        try {
            Payment payment = paymentService.getPaymentById(id);
            return ResponseEntity.ok(payment);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).build();
        }
    }

    // Delete payment (ADMIN only)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        try {
            paymentService.deletePayment(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).build();
        }
    }

    // DTO for payment creation request
    public static class PaymentRequest {
        private Long appointmentId;
        private String paymentMethod;
        private String cardLast4;

        public Long getAppointmentId() {
            return appointmentId;
        }

        public void setAppointmentId(Long appointmentId) {
            this.appointmentId = appointmentId;
        }

        public String getPaymentMethod() {
            return paymentMethod;
        }

        public void setPaymentMethod(String paymentMethod) {
            this.paymentMethod = paymentMethod;
        }

        public String getCardLast4() {
            return cardLast4;
        }

        public void setCardLast4(String cardLast4) {
            this.cardLast4 = cardLast4;
        }
    }
}


