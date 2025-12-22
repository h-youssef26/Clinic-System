package com.clinic.system.service;

import com.clinic.system.dto.LoginUserDto;
import com.clinic.system.dto.RegisterUserDto;
import com.clinic.system.dto.VerifyUserDto;
import com.clinic.system.model.User;
import com.clinic.system.repository.UserRepository;
import jakarta.mail.MessagingException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import org.springframework.beans.factory.annotation.Value;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    @Value("${app.auto-verify-users:false}")
    private boolean autoVerifyUsers;

    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            EmailService emailService
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public User signup(RegisterUserDto registerUserDto) {
        if ("DOCTOR".equalsIgnoreCase(registerUserDto.getRole())) {
            throw new RuntimeException("Doctors cannot sign up directly");
        }

        // Trim email to remove any whitespace
        String email = registerUserDto.getEmail() != null ? registerUserDto.getEmail().trim().toLowerCase() : "";
        String username = registerUserDto.getUsername() != null ? registerUserDto.getUsername().trim() : "";
        String roleStr = registerUserDto.getRole() != null ? registerUserDto.getRole().trim().toUpperCase() : "PATIENT";
        
        if (email.isEmpty()) {
            throw new RuntimeException("Email is required");
        }
        
        if (username.isEmpty()) {
            throw new RuntimeException("Username is required");
        }

        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already registered. Please use a different email or try logging in.");
        }

        // encode password
        String encodedPassword = passwordEncoder.encode(registerUserDto.getPassword());

        // Determine role: ADMIN or PATIENT (DOCTOR is not allowed)
        User.Role userRole;
        if ("ADMIN".equalsIgnoreCase(roleStr)) {
            userRole = User.Role.ADMIN;
        } else {
            userRole = User.Role.PATIENT;
        }

        User user = new User(
                username,
                email,
                encodedPassword,
                userRole
        );

        // generate verification code logic (optional)
        //user.setVerificationCode(generateVerificationCode());
        //user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(10));
        user.setEnabled(true); // require verification

        return userRepository.save(user);
    }


    public User authenticate(LoginUserDto loginUserDto) {
        // Trim email to remove any whitespace and convert to lowercase
        String email = loginUserDto.getEmail() != null ? loginUserDto.getEmail().trim().toLowerCase() : "";
        String password = loginUserDto.getPassword() != null ? loginUserDto.getPassword().trim() : "";
        
        if (email.isEmpty()) {
            throw new RuntimeException("Email is required");
        }
        
        if (password.isEmpty()) {
            throw new RuntimeException("Password is required");
        }
        
        // Find user by email (case-insensitive)
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    System.out.println("=== LOGIN FAILED: User not found ===");
                    System.out.println("Email searched: " + email);
                    return new RuntimeException("Invalid credentials");
                });
        
        // Debug logging
        System.out.println("=== LOGIN ATTEMPT ===");
        System.out.println("Email provided: " + email);
        System.out.println("User found: " + user.getEmail());
        System.out.println("User role: " + user.getRole());
        System.out.println("User enabled: " + user.isEnabled());
        System.out.println("User ID: " + user.getId());
        System.out.println("Password provided: '" + password + "'");
        System.out.println("Password provided (length): " + password.length());
        System.out.println("Password provided (bytes): " + java.util.Arrays.toString(password.getBytes()));
        System.out.println("Stored password (length): " + (user.getPassword() != null ? user.getPassword().length() : "null"));
        System.out.println("Stored password (first 30 chars): " + (user.getPassword() != null && user.getPassword().length() > 30 ? user.getPassword().substring(0, 30) + "..." : user.getPassword()));
        System.out.println("Stored password (full): " + user.getPassword());
        
        // Check if account is enabled
        if (!user.isEnabled()) {
            System.out.println("ERROR: Account not enabled");
            throw new RuntimeException("Account not verified");
        }

        // Manual password check (more reliable)
        String storedPassword = user.getPassword();
        if (storedPassword == null || storedPassword.isEmpty()) {
            System.out.println("ERROR: Stored password is null or empty!");
            throw new RuntimeException("Invalid credentials");
        }
        
        boolean passwordMatches = passwordEncoder.matches(password, storedPassword);
        System.out.println("Password matches: " + passwordMatches);
        
        if (!passwordMatches) {
            // Additional debug: try encoding the provided password to see if it matches
            String testEncoded = passwordEncoder.encode(password);
            System.out.println("Test encoding provided password (first 30): " + (testEncoded.length() > 30 ? testEncoded.substring(0, 30) + "..." : testEncoded));
            System.out.println("Stored encoded password (first 30): " + (storedPassword != null && storedPassword.length() > 30 ? storedPassword.substring(0, 30) + "..." : storedPassword));
            System.out.println("Direct comparison: " + testEncoded.equals(storedPassword));
            System.out.println("NOTE: BCrypt hashes are different each time, so direct comparison will always be false");
            
            // Try to reload the user from database to ensure we have the latest password
            User freshUser = userRepository.findById(user.getId()).orElse(null);
            if (freshUser != null) {
                System.out.println("Reloaded user password (first 30): " + (freshUser.getPassword() != null && freshUser.getPassword().length() > 30 ? freshUser.getPassword().substring(0, 30) + "..." : freshUser.getPassword()));
                boolean freshMatch = passwordEncoder.matches(password, freshUser.getPassword());
                System.out.println("Password matches with fresh user: " + freshMatch);
            }
            
            System.out.println("=== LOGIN FAILED: Password mismatch ===");
            throw new RuntimeException("Invalid credentials");
        }

        System.out.println("=== LOGIN SUCCESS ===");
        return user;
    }



    public void verifyUser(VerifyUserDto input) {
        Optional<User> optionalUser = userRepository.findByEmail(input.getEmail());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Verification code has expired");
            }
            if (user.getVerificationCode().equals(input.getVerificationCode())) {
                user.setEnabled(true);
                user.setVerificationCode(null);
                user.setVerificationCodeExpiresAt(null);
                userRepository.save(user);
            } else {
                throw new RuntimeException("Invalid verification code");
            }
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public void resendVerificationCode(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.isEnabled()) {
                throw new RuntimeException("Account is already verified");
            }
            user.setVerificationCode(generateVerificationCode());
            user.setVerificationCodeExpiresAt(LocalDateTime.now().plusHours(1));
            sendVerificationEmail(user);
            userRepository.save(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    private void sendVerificationEmail(User user) { //TODO: Update with company logo
        String subject = "Account Verification";
        String verificationCode = "VERIFICATION CODE " + user.getVerificationCode();
        String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Welcome to our app!</h2>"
                + "<p style=\"font-size: 16px;\">Please enter the verification code below to continue:</p>"
                + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                + "<h3 style=\"color: #333;\">Verification Code:</h3>"
                + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + verificationCode + "</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";

        try {
            emailService.sendVerificationEmail(user.getEmail(), subject, htmlMessage);
        } catch (MessagingException e) {
            // Handle email sending exception
            e.printStackTrace();
        }
    }
    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }
}