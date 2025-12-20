package com.clinic.system.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private String token;
    private long expiresIn;
    private String email;
    private String username;
    private String name;

    // Existing constructor
    public LoginResponse(String token, long expiresIn) {
        this.token = token;
        this.expiresIn = expiresIn;
    }

    // Full constructor for login response
    public LoginResponse(String token, long expiresIn, String email, String username, String name){
        this.token = token;
        this.expiresIn = expiresIn;
        this.email = email;
        this.username = username;
        this.name = name;
    }
}
