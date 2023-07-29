package com.example.iitkplanner.security;

import com.example.iitkplanner.model.VerificationData;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class VerificationStore {

    private Map<String, VerificationData> verificationDataMap;

    public VerificationStore() {
        verificationDataMap = new HashMap<>();
    }

    public void saveVerificationData(String email, VerificationData verificationData) {
        verificationDataMap.put(email, verificationData);
    }

    public VerificationData getVerificationData(String email) {
        return verificationDataMap.get(email);
    }

    public void removeVerificationData(String email) {
        verificationDataMap.remove(email);
    }
}
