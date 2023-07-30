package com.example.iitkplanner.dto;

import javax.validation.constraints.NotBlank;

public class OTPVerificationRequest {

    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Make sure that password is correct")
    private String password;

    @NotBlank(message = "OTP is required")
    private String otp;

    // Constructors

    public OTPVerificationRequest() {
    }

    public OTPVerificationRequest(String email, String otp, String password) {
        this.email = email;
        this.otp = otp;
        this.password = password;
    }

    // Getters

    public String getEmail() {
        return email;
    }

    public String getOtp() {
        return otp;
    }

    public String getPassword(){
        return password;
    }

    // Setters

    public void setEmail(String email) {
        this.email = email;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
