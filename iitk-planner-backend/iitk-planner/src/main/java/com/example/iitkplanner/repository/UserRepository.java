package com.example.iitkplanner.repository;

import com.example.iitkplanner.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Custom query to find a user by email
    User findByEmail(String email);

//    User findById(Long id);

    // Add more custom queries as needed for specific user-related operations
}
