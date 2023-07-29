package com.example.iitkplanner.controller;
import com.example.iitkplanner.dto.OTPVerificationRequest;
import com.example.iitkplanner.dto.UserRegistrationRequest;
import com.example.iitkplanner.model.User;
import com.example.iitkplanner.model.VerificationData;
import com.example.iitkplanner.repository.UserRepository;
import com.example.iitkplanner.security.JWTGenerator;
import com.example.iitkplanner.security.VerificationStore;
import com.example.iitkplanner.service.EmailService;
import com.example.iitkplanner.service.UserService;
import com.example.iitkplanner.util.OTPGenerator;
import com.example.iitkplanner.util.PasswordEncoder;
//import org.apache.catalina.User;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private VerificationStore verificationStore;

    @Autowired
    private UserRepository userRepository;


    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        // Create a secure random key with the HS256 algorithm and key size of 256 bits
        secretKey = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
    }


    @Autowired
    private JWTGenerator jwtGenerator;
//verificationStore.removeVerificationData(email);


    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRegistrationRequest request) {
        // Validate the registration request (e.g., email format, password strength)
        // Hash the user's password for secure storage
        String hashedPassword = PasswordEncoder.hashPassword(request.getPassword());

        // Save the user's details (email and hashed password) to the database
        User user = new User(request.getEmail(), hashedPassword);
        userService.saveUser(user);

        // Generate and send OTP to the user's email
        String otp = OTPGenerator.generateOTP();
        emailService.sendOTP(request.getEmail(), otp);

        // Save the OTP and user details in a temporary store (e.g., Redis) for verification
        VerificationData verificationData = new VerificationData(request.getEmail(), otp);
        verificationStore.saveVerificationData(verificationData.getEmail(), verificationData);

        return ResponseEntity.ok("Registration successful. Please check your email for OTP verification.");
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyOTP(@RequestBody OTPVerificationRequest request) {
        System.out.println("In Verify OTP");
        String email = request.getEmail();
        String otp = request.getOtp();

        // Retrieve the stored OTP for the given email from the temporary store (e.g., Redis)
        VerificationData verificationData = verificationStore.getVerificationData(email);

        if (verificationData == null || !verificationData.getOtp().equals(otp)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid OTP. Please try again.");
        }

        // OTP verification successful, remove the data from the temporary store
        verificationStore.removeVerificationData(email);

        // Generate an authentication token for the user (you can use JWT or any other token-based approach)
        String authToken = generateToken(email);

        return ResponseEntity.ok(authToken);
    }

    @PostMapping("/auth/login")
    public Map<String, String> login(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");

        User user = userRepository.findByEmail(email);
        if (user == null) {
            return Map.of("error", "Invalid email or password.");
        }

        // Compare the provided password with the hashed password in the database
        if (!BCrypt.checkpw(password, user.getPassword())) {
            return Map.of("error", "Invalid email or password.");
        }

        // If the password matches, generate a JWT
        String token = generateToken(email);

        // Send the token to the frontend
        return Map.of("authToken", token);

    }

    @PostMapping("/saveUserInfo")
    public ResponseEntity<String> saveUserInfo(@RequestBody Map<String, String> userInfoRequest) {
        String email = userInfoRequest.get("email");
        String name = userInfoRequest.get("name");
        String department = userInfoRequest.get("department");
        String batch = userInfoRequest.get("batch");

        // Find the user by email
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found.");
        }

        // Save the additional user information
        user.setName(name);
        user.setDepartment(department);
        user.setBatch(batch);
        userService.saveUser(user);

        return ResponseEntity.ok("User information saved successfully.");
    }


    private String generateToken(String email) {
        long expirationTimeInMilliseconds = 3600000; // 1 hour (adjust as needed)

        // Build the JWT token with the secure key
        return Jwts.builder()
                .setSubject(email)
                .setExpiration(new Date(System.currentTimeMillis() + expirationTimeInMilliseconds))
                .signWith(secretKey)
                .compact();
    }


}
