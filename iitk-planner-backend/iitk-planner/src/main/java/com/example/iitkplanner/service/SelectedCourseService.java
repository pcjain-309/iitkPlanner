package com.example.iitkplanner.service;

import com.example.iitkplanner.model.SelectedCourse;

import java.util.List;


public interface SelectedCourseService {

    public SelectedCourse saveCourse(SelectedCourse selectedCourse);

    List<SelectedCourse> getAllCourses();

    SelectedCourse updateCourse(Long id, SelectedCourse updatedCourse);

    void deleteCourse(Long id);
}
