/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Assignment.Trainer.Course;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author sony
 */
public class Course {
    private String id;
    private String courseName;
    private String courseInfo;
    private LocalDate schedule;
    private String trainerID;
    
    public Course(String id, String courseName, String courseInfo, LocalDate schedule, String trainerID) {
        this.id = id;
        this.courseName = courseName;
        this.courseInfo = courseInfo;
        this.schedule = schedule;
        this.trainerID = trainerID;
    }

    public String getId() {
        return id;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseInfo() {
        return courseInfo;
    }

    public LocalDate getSchedule() {
        return schedule;
    }

    public String getTrainerID() {
        return trainerID;
    }
    
}

   