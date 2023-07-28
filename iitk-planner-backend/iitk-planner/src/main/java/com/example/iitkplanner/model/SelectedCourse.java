package com.example.iitkplanner.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "selected_course")
public class SelectedCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "course_code")
    private String courseCode;

    @Column(name = "professor")
    private String professor;

    @Column(name = "credits")
    private Long credits;

//    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Timings> SelectedCourseTimings = new ArrayList<>();


    @OneToMany(mappedBy = "selectedCourse", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SelectedCourseTimings> selectedCourseTimings;
    // Constructors

    public SelectedCourse() {
    }

    public SelectedCourse(String name, String courseCode, String professor, Long credits, List<SelectedCourseTimings> selectedCourseTimings) {
        this.name = name;
        this.courseCode = courseCode;
        this.professor = professor;
        this.credits = credits;
        this.selectedCourseTimings = selectedCourseTimings;
    }

    // Getters

    public Long getId() {
        return id;
    }

    public Long getCredits(){
        return credits;
    }

    public String getName() {
        return name;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getProfessor() {
        return professor;
    }

    public List<SelectedCourseTimings> getTimings() {
        return selectedCourseTimings;
    }


    // Setters

    public void setId(Long id) {
        this.id = id;
    }

    public void setCredits(Long credits){
        this.credits = credits;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public void setTimings(List<SelectedCourseTimings> updatedTimings) {
        if (selectedCourseTimings == null) {
            selectedCourseTimings = new ArrayList<>();
        }

        // Remove any SelectedCourseTimings that are not present in the updatedTimings list
        selectedCourseTimings.removeIf(existingTiming -> !updatedTimings.contains(existingTiming));

        // Add new SelectedCourseTimings that are not already in the SelectedCourseTimings list
        for (SelectedCourseTimings updatedTiming : updatedTimings) {
            if (!selectedCourseTimings.contains(updatedTiming)) {
                selectedCourseTimings.add(updatedTiming);
            }
        }
    }

    public void addTiming(SelectedCourseTimings selectedCourseTiming) {
        selectedCourseTimings.add(selectedCourseTiming);
    }

    public void removeTiming(SelectedCourseTimings selectedCourseTiming) {
        selectedCourseTimings.remove(selectedCourseTiming);
    }

    // toString() method for logging


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Course{")
                .append("id=").append(id)
                .append(", name='").append(name).append('\'')
                .append(", courseCode='").append(courseCode).append('\'')
                .append(", professor='").append(professor).append('\'')
                .append(", credits=").append(credits)
                .append(", SelectedCourseTimings=").append(selectedCourseTimings.toString())
                .append('}');
        return builder.toString();
    }

}
