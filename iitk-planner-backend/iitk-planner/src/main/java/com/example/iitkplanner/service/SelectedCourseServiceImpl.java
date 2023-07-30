package com.example.iitkplanner.service;

import com.example.iitkplanner.model.SelectedCourse;
import com.example.iitkplanner.model.SelectedCourseTimings;
import com.example.iitkplanner.repository.SelectedCourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SelectedCourseServiceImpl implements SelectedCourseService{

    @Autowired
    private SelectedCourseRepository selectedCourseRepository;

    @Override
    public SelectedCourse saveCourse(SelectedCourse selectedCourse) {
        for (SelectedCourseTimings selectedCourseTiming : selectedCourse.getTimings()) {
            selectedCourseTiming.setCourse(selectedCourse);
        }
        System.out.println(selectedCourse.toString());
        return selectedCourseRepository.save(selectedCourse);
    }

    @Override
    public List<SelectedCourse> getAllCourses() {
//        System.out.println(courseRepository.findAll());
        return selectedCourseRepository.findAll();
    }

    public SelectedCourse updateCourse(Long id, SelectedCourse updatedCourse) {
        Optional<SelectedCourse> existingCourseOptional = selectedCourseRepository.findById(id);
        if (existingCourseOptional.isPresent()) {
            SelectedCourse existingCourse = existingCourseOptional.get();
            existingCourse.setName(updatedCourse.getName());
            existingCourse.setCourseCode(updatedCourse.getCourseCode());
            existingCourse.setProfessor(updatedCourse.getProfessor());
            existingCourse.setCredits(updatedCourse.getCredits());
            existingCourse.setTimings(updatedCourse.getTimings());

            for (SelectedCourseTimings selectedCourseTiming : existingCourse.getTimings()) {
                selectedCourseTiming.setCourse(existingCourse);
            }
            System.out.println("Hello");
            System.out.println(existingCourse.toString());
            return selectedCourseRepository.save(existingCourse);
        } else {
            throw new IllegalArgumentException("Course with ID " + id + " not found.");
        }
    }

    public SelectedCourse getSelectedCourseById(Long id) {
        Optional<SelectedCourse> selectedCourseOptional = selectedCourseRepository.findById(id);
        return selectedCourseOptional.orElse(null);
    }

    public void deleteCourse(Long id) {
        selectedCourseRepository.deleteById(id);
    }



}
