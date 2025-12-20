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

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final UserService userService;
    private final DoctorService doctorService;


    public AppointmentController(AppointmentService appointmentService, UserService userService, DoctorService doctorService) {
        this.appointmentService = appointmentService;
        this.userService = userService;
        this.doctorService = doctorService;
    }

    // Patient can create appointment
    @PostMapping
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Appointment> createAppointment(@RequestBody AppointmentRequest request, Principal principal) {
        User patient = userService.findByEmail(principal.getName());

        // Get the doctor entity
        Doctor doctor = doctorService.getDoctorById(request.getDoctorId());

        // Create the appointment
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setReason(request.getReason());
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setStatus("Scheduled"); // default status

        Appointment saved = appointmentService.createAppointment(appointment);
        return ResponseEntity.ok(saved);
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

    // Delete appointment (patient can only delete their own)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id, Principal principal) {
        User patient = userService.findByEmail(principal.getName());
        Appointment existing = appointmentService.getAppointmentById(id);
        if (!existing.getPatient().getId().equals(patient.getId())) {
            return ResponseEntity.status(403).build();
        }
        appointmentService.deleteAppointment(id);
        return ResponseEntity.ok().build();
    }

    // Admin can see all appointments
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
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

}
