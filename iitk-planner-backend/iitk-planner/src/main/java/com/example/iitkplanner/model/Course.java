package com.example.iitkplanner.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "course")
public class Course {

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
//    private List<Timings> timings = new ArrayList<>();


    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Timings> timings;
    // Constructors

    public Course() {
    }

    public Course(String name, String courseCode, String professor, Long credits, List<Timings> timings) {
        this.name = name;
        this.courseCode = courseCode;
        this.professor = professor;
        this.credits = credits;
        this.timings = timings;
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

    public List<Timings> getTimings() {
        return timings;
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

    public void setTimings(List<Timings> updatedTimings) {
        if (timings == null) {
            timings = new ArrayList<>();
        }

        // Remove any timings that are not present in the updatedTimings list
        timings.removeIf(existingTiming -> !updatedTimings.contains(existingTiming));

        // Add new timings that are not already in the timings list
        for (Timings updatedTiming : updatedTimings) {
            if (!timings.contains(updatedTiming)) {
                timings.add(updatedTiming);
            }
        }
    }

    public void addTiming(Timings timing) {
        timings.add(timing);
    }

    public void removeTiming(Timings timing) {
        timings.remove(timing);
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
                .append(", timings=").append(timings.toString())
                .append('}');
        return builder.toString();
    }
}
