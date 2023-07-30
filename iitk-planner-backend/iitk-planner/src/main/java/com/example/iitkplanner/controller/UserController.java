package com.example.iitkplanner.controller;

import com.example.iitkplanner.model.SelectedCourse;
import com.example.iitkplanner.model.User;
import com.example.iitkplanner.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "https://iitk-planner.web.app/")
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

//    @GetMapping("/getUserInfo")
//    public ResponseEntity<User> getUserInfo(@AuthenticationPrincipal UserDetails userDetails) {
//        String email = userDetails.getUsername();
//
//        // Find the user by email
//        User user = userRepository.findByEmail(email);
//        if (user == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//
//        // Fetch the selected courses for the user
//        Hibernate.initialize(user.getSelectedCourses());
//
//        return ResponseEntity.ok(user);
//    }

    @GetMapping("/{userId}/selectedCourses")
    public ResponseEntity<List<SelectedCourse>> getSelectedCoursesByUserId(@PathVariable Long userId) {
        // Find the user by ID
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        // Get the selected courses of the user
        User user = optionalUser.get();
        List<SelectedCourse> selectedCourses = user.getSelectedCourses();

        return ResponseEntity.ok(selectedCourses);
    }
}

