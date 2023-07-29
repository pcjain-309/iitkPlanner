package com.example.iitkplanner.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOTP(String recipientEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();

        System.out.println(recipientEmail);
        message.setTo(recipientEmail);
        message.setSubject("One-Time Password (OTP) Verification");
        message.setText("Your OTP for verification is: " + otp);

        message.setFrom("iitk-planner@outlook.com");

        mailSender.send(message);
    }
}
