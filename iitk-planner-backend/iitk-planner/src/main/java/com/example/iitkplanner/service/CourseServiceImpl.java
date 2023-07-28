package com.example.iitkplanner.service;

import com.example.iitkplanner.model.Course;
import com.example.iitkplanner.model.Timings;
import com.example.iitkplanner.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService{

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public Course saveCourse(Course course) {
        for (Timings timing : course.getTimings()) {
            timing.setCourse(course);
        }
        System.out.println(course.toString());
        return courseRepository.save(course);
    }

    @Override
    public List<Course> getAllCourses() {
//        System.out.println(courseRepository.findAll());
        return courseRepository.findAll();
    }

    @Override
    public Course getCourseByCourseCode(String courseCode){
        return courseRepository.findByCourseCode(courseCode);
    }

    public Course updateCourse(Long id, Course updatedCourse) {
        Optional<Course> existingCourseOptional = courseRepository.findById(id);
        if (existingCourseOptional.isPresent()) {
            Course existingCourse = existingCourseOptional.get();
            existingCourse.setName(updatedCourse.getName());
            existingCourse.setCourseCode(updatedCourse.getCourseCode());
            existingCourse.setProfessor(updatedCourse.getProfessor());
            existingCourse.setCredits(updatedCourse.getCredits());
            existingCourse.setTimings(updatedCourse.getTimings());

            for (Timings timing : existingCourse.getTimings()) {
                timing.setCourse(existingCourse);
            }
            System.out.println("Hello");
            System.out.println(existingCourse.toString());
            return courseRepository.save(existingCourse);
        } else {
            throw new IllegalArgumentException("Course with ID " + id + " not found.");
        }
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }
}
