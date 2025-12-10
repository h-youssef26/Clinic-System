package com.clinic.system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterPatientRequest {
    @NotBlank
    private String username;

    @NotBlank
    @Size(min = 6)
    private String password;

    @NotBlank
    private String name;

    private String phoneNumber;
    private String gender;
    private String address;
    private String dateOfBirth; // ISO yyyy-MM-dd optional

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;}
    public void setPassword(String password) {
        this.password = password;}
    public String getName() {
        return name;}
    public void setName(String name) {
        this.name = name;}
    public String getPhoneNumber() {
        return phoneNumber;}
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;}
    public String getGender() {
        return gender;}
    public void setGender(String gender) {
        this.gender = gender;}
    public String getAddress() {
        return address;}
    public void setAddress(String address) {
        this.address = address;}
}
