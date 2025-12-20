package com.clinic.system.controller;


import com.clinic.system.dto.LoginUserDto;
import com.clinic.system.dto.RegisterUserDto;
import com.clinic.system.dto.VerifyUserDto;
import com.clinic.system.model.User;
import com.clinic.system.responses.LoginResponse;
import com.clinic.system.service.AuthenticationService;
import com.clinic.system.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;

    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody RegisterUserDto registerUserDto) {
        try {
            // Create the user
            User registeredUser = authenticationService.signup(registerUserDto);

            // Build a response similar to LoginResponse, but without token
            LoginResponse response = new LoginResponse(
                    null,                      // no token
                    0,                         // no expiration
                    registeredUser.getEmail(),
                    registeredUser.getUsername(),
                    registeredUser.getRole() == User.Role.DOCTOR
                            ? registeredUser.getDoctor().getName()
                            : registeredUser.getUsername()
            );

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            // Email already exists or other conflicts
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginUserDto loginUserDto) {
        try {
            // Authenticate user (throws RuntimeException if email not found or password wrong)
            User authenticatedUser = authenticationService.authenticate(loginUserDto);

            // Generate JWT token
            String jwtToken = jwtService.generateToken(authenticatedUser);

            // Build LoginResponse
            LoginResponse loginResponse = new LoginResponse(
                    jwtToken,
                    jwtService.getExpirationTime(), // or 3600000
                    authenticatedUser.getEmail(),
                    authenticatedUser.getUsername(),
                    authenticatedUser.getRole() == User.Role.DOCTOR
                            ? authenticatedUser.getDoctor().getName()
                            : authenticatedUser.getUsername()  // fallback for non-doctor
            );

            return ResponseEntity.ok(loginResponse);

        } catch (RuntimeException e) {
            // Handles cases like: email not found, wrong password, etc.
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }



    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestBody VerifyUserDto verifyUserDto) {
        try {
            authenticationService.verifyUser(verifyUserDto);
            return ResponseEntity.ok("Account verified successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/resend")
    public ResponseEntity<?> resendVerificationCode(@RequestParam String email) {
        try {
            authenticationService.resendVerificationCode(email);
            return ResponseEntity.ok("Verification code sent");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}