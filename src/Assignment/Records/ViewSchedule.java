/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Assignment.Records;

import java.sql.Date;

/**
 *
 * @author user
 */
public class ViewSchedule {
     private String courseID;
    private Date courseSche;

    public ViewSchedule(String courseID, Date courseSche) {
        this.courseID = courseID;
        this.courseSche = courseSche;
    }

    public String getCourseID() {
        return courseID;
    }

    public Date getcourseSche() {
        return courseSche;
    }
}
