package com.clinic.system.controller;

import com.clinic.system.model.Appointment;
import com.clinic.system.model.User;
import com.clinic.system.service.AppointmentService;
import com.clinic.system.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;
import com.clinic.system.model.Doctor;
import com.clinic.system.service.DoctorService;
import com.clinic.system.dto.AppointmentRequest;
import com.clinic.system.service.AppointmentService;
import com.clinic.system.model.Appointment.AppointmentStatus;
import com.clinic.system.repository.AppointmentRepository;



@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final UserService userService;
    private final DoctorService doctorService;
    private final AppointmentRepository appointmentRepository;


    public AppointmentController(AppointmentService appointmentService, UserService userService, DoctorService doctorService, AppointmentRepository appointmentRepository) {
        this.appointmentService = appointmentService;
        this.userService = userService;
        this.doctorService = doctorService;
        this.appointmentRepository = appointmentRepository;
    }

    // Patient can create appointment
    @PostMapping
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Appointment> createAppointment(@RequestBody AppointmentRequest request, Principal principal) {
        User patient = userService.findByEmail(principal.getName());
        Doctor doctor = doctorService.getDoctorById(request.getDoctorId());

        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setReason(request.getReason());
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setAmount(request.getAmount());
        appointment.setStatus(AppointmentStatus.PENDING);
        Appointment saved = appointmentService.createAppointment(appointment);
        return ResponseEntity.ok(saved);
    }

    // cancel appointment (soft delete - change status to CANCELLED)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Void> cancelAppointment(@PathVariable Long id, Principal principal) {
        try {
            User patient = userService.findByEmail(principal.getName());
            Appointment appointment = appointmentService.getAppointmentById(id);
            
            // Verify patient owns the appointment
            if (!appointment.getPatient().getId().equals(patient.getId())) {
                throw new RuntimeException("You are not allowed to cancel this appointment.");
            }
            
            // Only allow cancellation if appointment has been approved by admin
            if (appointment.getStatus() != Appointment.AppointmentStatus.APPROVED) {
                throw new RuntimeException("You can only cancel appointments that have been accepted by the admin. Current status: " + appointment.getStatus());
            }
            
            // Cancel the appointment (soft delete - changes status to CANCELLED)
            appointmentService.cancelAppointment(id, patient);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            throw e; // Let global exception handler deal with it
        }
    }

    // Patient can see their appointments
    @GetMapping("/my")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<List<Appointment>> getMyAppointments(Principal principal) {
        User patient = userService.findByEmail(principal.getName());
        return ResponseEntity.ok(appointmentService.getAppointmentsByPatient(patient));
    }

    // Update appointment (patient can only update their own)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Appointment> updateAppointment(@PathVariable Long id,
                                                         @RequestBody Appointment appointment,
                                                         Principal principal) {
        User patient = userService.findByEmail(principal.getName());
        Appointment existing = appointmentService.getAppointmentById(id);
        if (!existing.getPatient().getId().equals(patient.getId())) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(appointmentService.updateAppointment(id, appointment));
    }


    // Admin can see all appointments
    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    @GetMapping("/admin/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Appointment>> getAppointmentsByStatus(@PathVariable AppointmentStatus status) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByStatus(status));
    }

    @GetMapping("/doctor/my")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<List<Appointment>> getDoctorAppointments(Principal principal) {
        // Find the logged-in doctor by the email in JWT
        Doctor doctor = doctorService.findByEmail(principal.getName());

        // Fetch all appointments for this doctor
        List<Appointment> appointments = appointmentService.getAppointmentsByDoctor(doctor);
        return ResponseEntity.ok(appointments);
    }

    @PutMapping("/admin/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Appointment> approveAppointment(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.approveAppointment(id));
    }

    @PutMapping("/admin/{id}/deny")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Appointment> denyAppointment(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.denyAppointment(id));
    }

}
