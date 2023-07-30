package com.example.iitkplanner.repository;

import com.example.iitkplanner.model.Course;
import com.example.iitkplanner.model.SelectedCourse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SelectedCourseRepository extends JpaRepository<SelectedCourse, Long> {

}
