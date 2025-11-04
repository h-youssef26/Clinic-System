package com.clinic.system.controller;

import com.clinic.system.model.Appointment;
import com.clinic.system.service.AppointmentService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
    public List<Appointment> getAllAppointments() {
        return appointmentService.getAllAppointments();
    }

    @GetMapping("/{id}")
    public Appointment getAppointmentById(@PathVariable Long id) {
        return appointmentService.getAppointmentById(id);
    }

    @PostMapping
    public Appointment createAppointment(@RequestBody Appointment appointment) {
        return appointmentService.saveAppointment(appointment);
    }

    @PutMapping("/{id}")
    public Appointment updateAppointment(@PathVariable Long id, @RequestBody Appointment appointment) {
        return appointmentService.updateAppointment(id, appointment);
    }

    @DeleteMapping("/{id}")
    public void deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
    }

    @PostMapping("/book")
    public ResponseEntity<Appointment> bookAppointment(
            @RequestParam Long patientId,
            @RequestParam Long doctorId,
            @RequestParam String date,
            @RequestParam String time,
            @RequestParam(required = false) String notes) {

        LocalDate appointmentDate = LocalDate.parse(date);
        LocalTime appointmentTime = LocalTime.parse(time);

        Appointment appointment = appointmentService.bookAppointment(
                patientId, doctorId, appointmentDate, appointmentTime, notes);
        return ResponseEntity.ok(appointment);
    }

    @GetMapping("/doctor/{doctorId}/schedule")
    public ResponseEntity<List<Appointment>> getDoctorSchedule(@PathVariable Long doctorId) {
        return ResponseEntity.ok(appointmentService.getDoctorSchedule(doctorId));
    }

    @GetMapping("/doctor/{doctorId}/schedule/{date}")
    public ResponseEntity<List<Appointment>> getDoctorScheduleByDate(
            @PathVariable Long doctorId,
            @PathVariable String date) {
        LocalDate parsedDate = LocalDate.parse(date);
        return ResponseEntity.ok(appointmentService.getDoctorScheduleByDate(doctorId, parsedDate));
    }

    @PostMapping("/doctor/{doctorId}/prescribe/{appointmentId}")
    public ResponseEntity<Appointment> addPrescription(
            @PathVariable Long doctorId,
            @PathVariable Long appointmentId,
            @RequestParam String prescriptionText) {

        Appointment updated = appointmentService.addPrescription(appointmentId, doctorId, prescriptionText);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/patient/{patientId}/report")
    public ResponseEntity<List<Appointment>> getPatientReport(@PathVariable Long patientId) {
        return ResponseEntity.ok(appointmentService.getPatientAppointments(patientId));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Appointment> cancelAppointment(
            @PathVariable Long id,
            @RequestParam Long patientId) {

        Appointment appointment = appointmentService.cancelAppointment(id, patientId);
        return ResponseEntity.ok(appointment);
    }

    @PutMapping("/{appointmentId}/reschedule")
    public ResponseEntity<Appointment> rescheduleAppointment(
            @PathVariable Long appointmentId,
            @RequestParam Long patientId,
            @RequestParam String newDate,   // format: "yyyy-MM-dd"
            @RequestParam String newTime    // format: "HH:mm"
    ) {
        LocalDate date = LocalDate.parse(newDate);
        LocalTime time = LocalTime.parse(newTime);

        Appointment updatedAppointment = appointmentService.rescheduleAppointment(
                appointmentId, patientId, date, time
        );

        return ResponseEntity.ok(updatedAppointment);
    }

}
