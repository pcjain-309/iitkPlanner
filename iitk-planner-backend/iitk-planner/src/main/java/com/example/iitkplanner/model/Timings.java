package com.example.iitkplanner.model;
import jakarta.persistence.*;

@Entity
@Table(name = "timings")
public class Timings {

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
    private Course course;

    // Constructors

    public Timings() {
    }

    public Timings(String startTime, String endTime) {
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

    public void setCourse(Course course) {
        this.course = course;
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
        return "Timings{" +
                "id=" + id +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}
