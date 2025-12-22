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

            // Get the actual username field (not email)
            String actualUsername = registeredUser.getActualUsername();
            // Build a response similar to LoginResponse, but without token
            LoginResponse response = new LoginResponse(
                    null,                      // no token
                    0,                         // no expiration
                    registeredUser.getEmail(),
                    actualUsername != null ? actualUsername : registeredUser.getEmail(), // Use actual username field
                    registeredUser.getRole() == User.Role.DOCTOR
                            ? registeredUser.getDoctor().getName()
                            : (actualUsername != null && !actualUsername.isEmpty() ? actualUsername : registeredUser.getEmail()),
                    registeredUser.getRole().name() // Add role
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

            // Get the actual username field (not email)
            String actualUsername = authenticatedUser.getActualUsername();
            // Get the name for the token and response
            String userName = authenticatedUser.getRole() == User.Role.DOCTOR
                    ? authenticatedUser.getDoctor().getName()
                    : (actualUsername != null && !actualUsername.isEmpty() ? actualUsername : authenticatedUser.getEmail());  // Use actual username, fallback to email if null

            // Generate JWT token with name included
            String jwtToken = jwtService.generateToken(authenticatedUser, userName);

            // Build LoginResponse - use actual username field, not getUsername() which returns email
            LoginResponse loginResponse = new LoginResponse(
                    jwtToken,
                    jwtService.getExpirationTime(), // or 3600000
                    authenticatedUser.getEmail(),
                    actualUsername != null ? actualUsername : authenticatedUser.getEmail(), // Use actual username field
                    userName,  // Use the same name
                    authenticatedUser.getRole().name() // Add role
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