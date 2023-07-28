package com.example.iitkplanner.repository;

import com.example.iitkplanner.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Course findByCourseCode(String courseCode);
    // You can add custom query methods here if needed
}
