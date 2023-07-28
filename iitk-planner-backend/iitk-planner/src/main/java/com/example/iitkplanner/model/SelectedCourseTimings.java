package com.example.iitkplanner.model;
import jakarta.persistence.*;

@Entity
@Table(name = "selected_course_timings")
public class SelectedCourseTimings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "startTime")
    private String startTime;

    @Column(name = "endTime")
    private String endTime;

    @Column(name = "day")
    private String day;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private SelectedCourse selectedCourse;

    // Constructors

    public SelectedCourseTimings() {
    }

    public SelectedCourseTimings(String startTime, String endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setCourse(SelectedCourse selectedCourse) {
        this.selectedCourse = selectedCourse;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }


    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    // toString() method for logging

    @Override
    public String toString() {
        return "SelectedCourseTimings{" +
                "id=" + id +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}
