package com.clinic.system.config;


import com.clinic.system.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class ApplicationConfiguration {
    private final UserRepository userRepository;
    public ApplicationConfiguration(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    UserDetailsService userDetailsService() {
        return username -> {
            // username is actually the email (from User.getUsername() which returns email)
            // Normalize to lowercase for consistent lookup
            String email = username != null ? username.trim().toLowerCase() : "";
            return userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        };
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }
}