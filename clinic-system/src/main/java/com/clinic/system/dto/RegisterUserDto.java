package com.clinic.system.dto;

import lombok.Getter;
import lombok.Setter;
import com.clinic.system.model.User;
@Getter
@Setter
public class RegisterUserDto {
    private String email;
    private String password;
    private String username;
    private String role;}