package com.example.iitkplanner.controller;

import com.example.iitkplanner.model.Course;
import com.example.iitkplanner.model.SelectedCourse;
import com.example.iitkplanner.repository.CourseRepository;
import com.example.iitkplanner.service.CourseService;
import com.example.iitkplanner.service.SelectedCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "https://iitk-planner.web.app")
@RestController
@RequestMapping("/selectedCourse")
public class SelectedCourseController {

    @Autowired
    private SelectedCourseService selectedCourseService;

    @PostMapping("/add")
    public SelectedCourse add(@RequestBody SelectedCourse course){
        System.out.println(course.toString());
        selectedCourseService.saveCourse(course);
        return course;
    }


    @GetMapping("/{id}")
    public ResponseEntity<SelectedCourse> getSelectedCourseById(@PathVariable Long id) {
        SelectedCourse selectedCourse = selectedCourseService.getSelectedCourseById(id);
        if (selectedCourse == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(selectedCourse);
    }

    @GetMapping("/getAll")
    public List<SelectedCourse> getAllCourses() {
        System.out.println("Here Here");
        List<SelectedCourse> courseList = selectedCourseService.getAllCourses();

        return selectedCourseService.getAllCourses();
    }

    @PutMapping("/{id}")
    public SelectedCourse updateCourse(@PathVariable Long id, @RequestBody SelectedCourse updatedCourse) {
        System.out.println(updatedCourse.toString());
        return selectedCourseService.updateCourse(id, updatedCourse);
    }

    @DeleteMapping("/{id}")
    public void deleteCourse(@PathVariable Long id) {
        System.out.println("Hello");
        selectedCourseService.deleteCourse(id);
    }
}
