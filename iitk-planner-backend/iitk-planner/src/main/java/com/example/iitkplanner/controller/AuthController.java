package com.example.iitkplanner.controller;
import com.example.iitkplanner.dto.OTPVerificationRequest;
import com.example.iitkplanner.dto.UserRegistrationRequest;
import com.example.iitkplanner.model.SelectedCourse;
import com.example.iitkplanner.model.SelectedCourseTimings;
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
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.util.*;


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

        // Save the user's details (email and hashed password) to the database

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
        String hashedPassword = PasswordEncoder.hashPassword(request.getPassword());


        // Retrieve the stored OTP for the given email from the temporary store (e.g., Redis)
        VerificationData verificationData = verificationStore.getVerificationData(email);

        if (verificationData == null || !verificationData.getOtp().equals(otp)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid OTP. Please try again.");
        }

        User user = new User(request.getEmail(), hashedPassword);
        userService.saveUser(user);        // OTP verification successful, remove the data from the temporary store
        verificationStore.removeVerificationData(email);

        // Generate an authentication token for the user (you can use JWT or any other token-based approach)
        String authToken = generateToken(email);

        return ResponseEntity.ok(authToken);
    }

    @PostMapping("/login")
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
    public ResponseEntity<String> saveUserInfo(@RequestBody Map<String, Object> userInfoRequest) {
        System.out.println(userInfoRequest);
        String email = (String) userInfoRequest.get("email");
        String name = (String) userInfoRequest.get("name");
        String department = (String) userInfoRequest.get("department");
        String batch = (String) userInfoRequest.get("batch");
        List<Map<String, Object>> selectedCourses = (List<Map<String, Object>>) userInfoRequest.get("selectedCourse");

        // Find the user by email
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found.");
        }

        // Save the additional user information
        user.setName(name);
        user.setDepartment(department);
        user.setBatch(batch);

        // Save the selected courses for the user
        List<SelectedCourse> courses = new ArrayList<>();

        for (Map<String, Object> courseData : selectedCourses) {
            String courseName = (String) courseData.get("name");
            String courseCode = (String) courseData.get("courseCode");
            String professor = (String) courseData.get("professor");
            Integer credits = (Integer) courseData.get("credits");


            SelectedCourse course = new SelectedCourse();
            course.setName(courseName);
            course.setCourseCode(courseCode);
            course.setProfessor(professor);
            course.setCredits(credits);
            course.setUser(user);

            // Extract timings for the course
            List<Map<String, String>> timings = (List<Map<String, String>>) courseData.get("timings");
            List<SelectedCourseTimings> selectedCourseTimings = new ArrayList<>();

            for (Map<String, String> timingData : timings) {
                String startTime = timingData.get("startTime");
                String endTime = timingData.get("endTime");
                String day = timingData.get("day");

                SelectedCourseTimings timing = new SelectedCourseTimings();
                timing.setStartTime(startTime);
                timing.setEndTime(endTime);
                timing.setDay(day);

                timing.setCourse(course);

                // Add other timing details and mappings as needed
                selectedCourseTimings.add(timing);
            }

            // Set the timings for the course
            course.setTimings(selectedCourseTimings);
//            course.setUser();



            // Add other course details and mappings as needed
            courses.add(course);
        }

//        user.setSelectedCourses(courses);

        user.getSelectedCourses().addAll(courses);
        // Save the updated user entity
        userService.saveUser(user);

        return ResponseEntity.ok("User information saved successfully.");
    }

    @GetMapping("/user/selectedCourses")
    public ResponseEntity<List<SelectedCourse>> getUserSelectedCourses(HttpServletRequest request) {

        System.out.println("In the getUserSelectedCourse from auth token");
        // Read the authentication token from the request headers
        String authToken = request.getHeader("Authorization");

        // Check if the authentication token is valid and extract the user's email
        String email;
        try {
            email = extractEmailFromToken(authToken);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        System.out.println(email);

        // Find the user by email
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        System.out.println(email);
        // Get the selected courses of the user
        List<SelectedCourse> selectedCourses = user.getSelectedCourses();

        System.out.println(selectedCourses.toString());
        return ResponseEntity.ok(selectedCourses);
    }

    @PostMapping("/addCourse")
    public ResponseEntity<String> addCourseToUser(@RequestBody Map<String, Object> courseData, HttpServletRequest request) {
        // Extract the authorization token from the request header
        String authToken = request.getHeader("Authorization");

        // Parse the email from the authToken
        String email = extractEmailFromToken(authToken);

        // Find the user by email
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found.");
        }

        // Create a SelectedCourse object and set its properties using the courseData map
        SelectedCourse course = new SelectedCourse();
        course.setName((String) courseData.get("name"));
        course.setCourseCode((String) courseData.get("courseCode"));
        course.setProfessor((String) courseData.get("professor"));
        course.setCredits((Integer) courseData.get("credits"));
        course.setUser(user);

        // Extract timings from the courseData map
        List<Map<String, String>> timingsData = (List<Map<String, String>>) courseData.get("timings");
        List<SelectedCourseTimings> selectedCourseTimings = new ArrayList<>();

        for (Map<String, String> timingData : timingsData) {
            String startTime = timingData.get("startTime");
            String endTime = timingData.get("endTime");
            String day = timingData.get("day");

            SelectedCourseTimings timing = new SelectedCourseTimings();
            timing.setStartTime(startTime);
            timing.setEndTime(endTime);
            timing.setDay(day);

            timing.setCourse(course);

            selectedCourseTimings.add(timing);
        }

        course.setTimings(selectedCourseTimings);

        // Add the course to the user's selected courses
        user.getSelectedCourses().add(course);

        // Save the updated user entity
        userService.saveUser(user);

        return ResponseEntity.ok("Course information saved successfully.");
    }

    @PostMapping("/updateCourse")
    public ResponseEntity<String> updateCourseDetails(@RequestBody Map<String, Object> courseDetails, HttpServletRequest request ) {

        String authToken = request.getHeader("Authorization");
        String email = extractEmailFromToken(authToken); // Implement this method to extract the email from the token

        // Step 2: Find the user in the database using the email
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found.");
        }

        // Step 3: Extract the course details from the request
        String courseName = (String) courseDetails.get("name");
        String courseCode = (String) courseDetails.get("courseCode");

        // Step 4: Find the existing course of the user by course name and course code
        SelectedCourse existingCourse = null;
        for (SelectedCourse course : user.getSelectedCourses()) {
            if (course.getCourseCode().equals(courseCode)) {
                existingCourse = course;
                break;
            }
        }

        if (existingCourse == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found for the user.");
        }

        // Step 5: Update the existing course details
        String professor = (String) courseDetails.get("professor");

        Integer credits = (Integer) courseDetails.get("credits");
        existingCourse.setProfessor(professor);
        existingCourse.setName(courseName);
        existingCourse.setCredits(credits);

        // Update other course details as needed
        // ...

        // Step 6: Update course timings
        List<Map<String, String>> timings = (List<Map<String, String>>) courseDetails.get("timings");
        List<SelectedCourseTimings> selectedCourseTimings = new ArrayList<>();

        for (Map<String, String> timingData : timings) {
            String startTime = timingData.get("startTime");
            String endTime = timingData.get("endTime");
            String day = timingData.get("day");

            SelectedCourseTimings timing = new SelectedCourseTimings();
            timing.setStartTime(startTime);
            timing.setEndTime(endTime);
            timing.setDay(day);
            timing.setCourse(existingCourse);

            selectedCourseTimings.add(timing);
        }

        existingCourse.setTimings(selectedCourseTimings);

        // Step 7: Save the updated user entity to the database
        userService.saveUser(user);

        return ResponseEntity.ok("Course details updated successfully.");
    }

    @GetMapping("/isRegistered/{email}")
    public ResponseEntity<Boolean> isUserRegistered(@PathVariable String email) {
        // Check if the user with the given email is registered
        User user = userRepository.findByEmail(email);

        // Check if the user with the given email exists
        boolean isRegistered = (user != null);

        return ResponseEntity.ok(isRegistered);
    }



    private String generateToken(String email) {
        long expirationTimeInMilliseconds = 3600000; // 1 hour (adjust as needed)

        // Build the JWT token with the secure key
        return Jwts.builder()
                .claim("email", email) // Include the email as a claim in the token
                .setExpiration(new Date(System.currentTimeMillis() + expirationTimeInMilliseconds))
                .signWith(secretKey)
                .compact();
    }


    // Helper method to extract the user's email from the authentication token
    private String extractEmailFromToken(String token) {
        try {
            // Parse the token using the secret key
            String newToken = token.substring(7);
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(newToken)
                    .getBody();

            // Extract and return the email from the claims
            return claims.get("email", String.class);
        } catch (Exception e) {
            // If there's an error while parsing the token, return null or handle the exception as needed
            e.printStackTrace();
            return null;
        }
    }



}
