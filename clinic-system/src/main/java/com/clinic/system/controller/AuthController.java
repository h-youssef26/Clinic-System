package com.clinic.system.controller;
import com.clinic.system.dto.JwtResponse;
import com.clinic.system.dto.*;
import com.clinic.system.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import com.clinic.system.dto.LoginRequest;
import com.clinic.system.dto.RegisterPatientRequest;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            JwtResponse jwt = authService.authenticateUser(loginRequest);
            return ResponseEntity.ok(jwt);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    @PostMapping("/register/patient")
    public ResponseEntity<?> registerPatient(@Valid @RequestBody RegisterPatientRequest req) {
        String result = authService.registerPatient(req);
        if (result.startsWith("Error")) {
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/register/doctor")
    public ResponseEntity<?> registerDoctor(@RequestBody(required = false) RegisterPatientRequest req) {
        // if caller passes username, use it; otherwise return generic message
        String username = (req != null) ? req.getUsername() : null;
        String msg = authService.registerDoctorBlocked(username);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(msg);
    }
}
