package com.example.iitkplanner.controller;

import com.example.iitkplanner.model.Course;
import com.example.iitkplanner.repository.CourseRepository;
import com.example.iitkplanner.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping("/add")
    public Course add(@RequestBody Course course){
        System.out.println(course.toString());
        courseService.saveCourse(course);
        return course;
    }

    @GetMapping("/getAll")
    public List<Course> getAllCourses() {
        System.out.println("Here Here");
        List<Course> courseList = courseService.getAllCourses();

        return courseService.getAllCourses();
    }

    @PutMapping("/{id}")
    public Course updateCourse(@PathVariable Long id, @RequestBody Course updatedCourse) {
        System.out.println("Hello");
        System.out.println(updatedCourse.toString());
        return courseService.updateCourse(id, updatedCourse);
    }

    @DeleteMapping("/{id}")
    public void deleteCourse(@PathVariable Long id) {
        System.out.println("Hello");
        courseService.deleteCourse(id);
    }
}
