package com.example.iitkplanner.dto;

import javax.validation.constraints.NotBlank;

public class OTPVerificationRequest {

    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "OTP is required")
    private String otp;

    // Constructors

    public OTPVerificationRequest() {
    }

    public OTPVerificationRequest(String email, String otp) {
        this.email = email;
        this.otp = otp;
    }

    // Getters

    public String getEmail() {
        return email;
    }

    public String getOtp() {
        return otp;
    }

    // Setters

    public void setEmail(String email) {
        this.email = email;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
