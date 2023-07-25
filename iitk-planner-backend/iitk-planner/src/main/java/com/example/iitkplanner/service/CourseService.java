package com.example.iitkplanner.service;

import com.example.iitkplanner.model.Course;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CourseService {
    public Course saveCourse(Course course);

    List<Course> getAllCourses();

    Course updateCourse(Long id, Course updatedCourse);

    void deleteCourse(Long id);
}
