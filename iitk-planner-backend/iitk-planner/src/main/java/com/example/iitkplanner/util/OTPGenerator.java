package com.example.iitkplanner.util;

import java.security.SecureRandom;

public class OTPGenerator {

    private static final String OTP_CHARACTERS = "0123456789";
    private static final int OTP_LENGTH = 6;

    public static String generateOTP() {
        StringBuilder otp = new StringBuilder(OTP_LENGTH);
        SecureRandom random = new SecureRandom();

        for (int i = 0; i < OTP_LENGTH; i++) {
            int index = random.nextInt(OTP_CHARACTERS.length());
            otp.append(OTP_CHARACTERS.charAt(index));
        }

        return otp.toString();
    }
}

