/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Assignment.Trainee.Enrollment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author user
 */
public class Enrollment {
    private String courseID;
    private String courseName;
    private String trainerName;
    private String traineeID;
    private String courseInfo;
    private Date courseSche;
    ArrayList<String> courseIDList = new ArrayList<String>();
    
    public Enrollment(String courseID, String courseName, String trainerName, String courseInfo, Date courseSche) {
        this.courseID = courseID;
        this.courseName = courseName;
        this.trainerName = trainerName;
        this.courseInfo = courseInfo;
        this.courseSche = courseSche;
    }
    
     public Enrollment(String courseID, String courseName, Date courseSche){
        this.courseID = courseID;
        this.courseName = courseName;
        this.courseSche = courseSche;
    }
    
    public Enrollment(){
    
    }

    public String getCourseID() {
        return courseID;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getTrainerName() {
        return trainerName;
    }

    public Date getCourseSche() {
        return courseSche;
    }
    
    public String getCourseInfo() {
        return courseInfo;
    }
    
    public ArrayList<String> getList(){
        return courseIDList;
    }

}
